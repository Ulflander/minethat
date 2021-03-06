package com.ulflander.mining.processors.extract;

import com.ulflander.app.model.Chapter;
import com.ulflander.app.model.Document;
import com.ulflander.app.model.Entity;
import com.ulflander.app.model.Paragraph;
import com.ulflander.app.model.Sentence;
import com.ulflander.app.model.Token;
import com.ulflander.app.model.TokenType;
import com.ulflander.mining.processors.Processor;
import com.ulflander.mining.processors.ProcessorDepthControl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Attempt to link name parts with already found entities.
 *
 * Created by Ulflander on 5/18/14.
 */
public class UnknownPersonToEntity extends Processor {

    /**
     * Parts of person names.
     */
    private HashMap<String, Entity> personParts;

    /**
     * Parts of person names.
     */
    private HashMap<String, String> surfaces;

    /**
     * List of ambiguous parts.
     */
    private List<String> ambiguous;

    /**
     * Increment confidence in case of finding.
     */
    public static final float CONFIDENCE_INCREMENT = 0.1f;

    @Override
    public final void init() {
        this.setDepthControl(ProcessorDepthControl.TOKEN);
        this.setInitialized(true);
    }

    @Override
    public final String describe() {
        return "Try to link name parts with already found entities";
    }

    @Override
    public final void extractDocument(final Document doc) {
        personParts = new HashMap<String, Entity>();
        ambiguous = new ArrayList<String>();
        surfaces = new HashMap<String, String>();
    }

    @Override
    public final void extractToken(final Token token) {

        if (!token.hasScore(TokenType.PERSON)) {
            return;
        }


        Entity e = token.getMostConfidentEntity();

        if (e == null) {
            return;
        }

        String[] parts = token.getClean().split(" ");

        for (String part: parts) {
            if (part.length() == 1) {
                continue;
            }

            if (!surfaces.containsKey(part)
                    || surfaces.get(part).equals(token.getClean())) {
                personParts.put(part, e);
                // Manage ambiguity
            } else {
                ambiguous.add(part);
            }

            surfaces.put(part, token.getClean());
        }
    }

    @Override
    public final void onProcessed(final Document doc) {
        for (Chapter chap : doc.getChapters()) {
            for (Paragraph paragraph : chap.getParagraphs()) {
                for (Sentence sentence : paragraph.getSentences()) {
                    for (Token t: sentence.getTokens()) {
                        reprocess(t);
                    }
                }
            }
        }
    }

    /**
     * Reprocess each token and attempt to link it to an entity.
     *
     * @param t Token to reprocess
     */
    private void reprocess(final Token t) {

        if (t.getType() != TokenType.KEYWORD) {
            return;
        }

        // If we got a unambiguous result, set it directly
        if (personParts.containsKey(t.getClean())
                && !ambiguous.contains(t.getClean())) {

            t.addEntity(personParts.get(t.getClean()));
        }
    }
}
