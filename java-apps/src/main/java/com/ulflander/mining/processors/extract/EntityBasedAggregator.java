package com.ulflander.mining.processors.extract;

import com.ulflander.app.model.Document;
import com.ulflander.app.model.Entity;
import com.ulflander.app.model.Token;
import com.ulflander.mining.nlp.BasicTokenizer;
import com.ulflander.mining.processors.Processor;
import com.ulflander.mining.processors.ProcessorDepthControl;

import java.util.HashMap;

/**
 * Aggregates surrounding tokens if one of eligible entities make sense
 * with it, consolidate entity in this case.
 *
 * Created by Ulflander on 5/18/14.
 */
public class EntityBasedAggregator extends Processor {


    /**
     * Tokenizer for entity label.
     */
    private BasicTokenizer tokenizer = new BasicTokenizer();

    /**
     * Confidence increment.
     */
    public static final Float CONFIDENCE_INCREMENT = 0.2f;

    @Override
    public final void init() {
        this.setDepthControl(ProcessorDepthControl.TOKEN);
        this.setInitialized(true);
    }

    @Override
    public final String describe() {
        return "Aggregate tokens based on eligible entities";
    }

    /**
     * Tokens to be merged backward, and how many token should be merged.
     */
    private HashMap<Token, Integer> toMergeBackward;

    /**
     * Tokens to be merged forward, and how many token should be merged.
     */
    private HashMap<Token, Integer> toMergeForward;

    @Override
    public final void extractDocument(final Document doc) {
        toMergeBackward = new HashMap<Token, Integer>();
        toMergeForward = new HashMap<Token, Integer>();
    }

    @Override
    public final void onProcessed(final Document doc) {

        Token other;
        Token initial;

        for (Token t: toMergeBackward.keySet()) {
            initial = t;
            int i = toMergeBackward.get(t);
            while (i > 0) {
                other = initial.getPrevious();
                for (Entity e: other.getEntities()) {
                    e.setConfidence(e.getConfidence() / 2);
                }
                t.getSentence().mergeToPrevious(initial);
                initial = other;
                i--;
            }
        }

        for (Token t: toMergeForward.keySet()) {
            int i = toMergeForward.get(t);
            while (i > 0) {
                for (Entity e: t.getNext().getEntities()) {
                    e.setConfidence(e.getConfidence() / 2);
                }
                t.getSentence().mergeToPrevious(t.getNext());
                i--;
            }
        }
    }

    @Override
    public final void extractToken(final Token token) {
        for (Entity ee: token.getEntities()) {
            String label = ee.getLabel();
            if (label == null) {
                continue;
            }

            String target;
            String surface = token.getSurface();

            int i = label.indexOf(surface);
            if (i == -1 || label.length() == surface.length()) {
                continue;
            }

            // Going forward
            if (i == 0) {
                target = label.substring(surface.length()).trim();
                seekForward(token, ee, tokenizer.tokenize(target));
            // Going backward
            } else {
                target = label.substring(0, i - 1).trim();
                seekBackward(token, ee, tokenizer.tokenize(target));
            }
        }
    }

    /**
     * Seek backward for tokens that looks like entity label subpart.
     *
     * @param token Token
     * @param ee Entity
     * @param surface Part of entity label to seek before token
     */
    private void seekBackward(final Token token,
                                    final Entity ee,
                                    final String[] surface) {
        int l = surface.length;
        Token prev = token;
        boolean found = false;
        while (l > 0 && prev.hasPrevious()) {
            prev = prev.getPrevious();
            l--;

            if (prev.getSurface().equals(surface[l])) {
                found = true;
            } else {
                found = false;
            }
        }

        if (found && l == 0) {
            ee.setConfidence(ee.getConfidence() + CONFIDENCE_INCREMENT);
            toMergeBackward.put(token, surface.length);
        } else {
            ee.setConfidence(ee.getConfidence() - CONFIDENCE_INCREMENT);
        }
    }

    /**
     * Seek forward for tokens that looks like entity label subpart.
     *
     * @param token Token
     * @param ee Entity
     * @param surface Part of entity label to seek after token
     */
    private void seekForward(final Token token,
                                   final Entity ee,
                                   final String[] surface) {
        int i = 0;
        int l = surface.length;
        Token next = token;
        boolean found = false;
        while (i < l && next.hasNext()) {
            next = next.getNext();

            if (next.getSurface().equals(surface[i])) {
                found = true;
            } else {
                found = false;
            }

            i++;
        }

        if (found && i == l) {
            ee.setConfidence(ee.getConfidence() + CONFIDENCE_INCREMENT);
            toMergeForward.put(token, l);
        } else {
            ee.setConfidence(ee.getConfidence() - CONFIDENCE_INCREMENT);
        }
    }
}
