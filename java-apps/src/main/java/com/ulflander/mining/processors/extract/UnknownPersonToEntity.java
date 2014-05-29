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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * If some token may be an entity, let's lookup various services and retrieve
 * entities.
 *
 * Created by Ulflander on 5/18/14.
 */
public class UnknownPersonToEntity extends Processor {


    HashMap<String, Collection<Entity>> personParts;

    List<String> ambiguous;

    @Override
    public final void init() {
        this.setDepthControl(ProcessorDepthControl.TOKEN);
        this.setInitialized(true);
    }

    @Override
    public final String describe() {
        return "Lookup various services (DBPedia..) and check for entities.";
    }

    @Override
    public void extractDocument(Document doc) {
        personParts = new HashMap<String, Collection<Entity>>();
        ambiguous = new ArrayList<String>();
    }

    @Override
    public final void extractToken(final Token token) {

        Collection<Entity> ents = token.getEntities();

        if (ents == null || ents.size() == 0) {
            return;
        }

        String[] parts = token.getClean().split(" ");

        for (String part: parts) {
            if (part.length() == 1) {
                continue;
            }

            if (!personParts.containsKey(part)) {
                personParts.put(part, ents);

                // Manage ambiguity
            } else {
                ambiguous.add(part);
            }
        }
    }

    @Override
    public void onProcessed(Document doc) {
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

        // Need to have PERSON_PART but not PERSON
        if (!t.hasScore(TokenType.PERSON_PART)
                || t.hasScore(TokenType.PERSON)) {
            return;
        }

        // If we got a unambiguous result, set it directly
        if (personParts.containsKey(t.getClean())
                && !ambiguous.contains(t.getClean())) {

            Collection<Entity> ents = personParts.get(t.getClean());
            for (Entity e: ents) {
                t.addEntity(e);
            }
        }

        // TODO: manage ambiguity
    }
}
