package com.ulflander.mining.processors.extract;

import com.ulflander.app.model.Chapter;
import com.ulflander.app.model.Document;
import com.ulflander.app.model.Entity;
import com.ulflander.app.model.EntityType;
import com.ulflander.app.model.Paragraph;
import com.ulflander.app.model.Sentence;
import com.ulflander.app.model.Token;
import com.ulflander.app.model.TokenType;
import com.ulflander.mining.processors.Processor;
import com.ulflander.mining.processors.ProcessorDepthControl;
import com.ulflander.mining.services.DbPediaLookupResult;
import com.ulflander.utils.UlfStringUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Consolidate entities confidence based on top confidence of entity, on
 * frequency of entities, on ref count if DbPedia result and levenshtein
 * distance with entity label.
 *
 * Created by Ulflander on 5/18/14.
 */
public class EntityConsolidation extends Processor {

    /**
     * Low levenshtein distance.
     */
    public static final int LOW_DISTANCE = 10;

    /**
     * Medium levenshtein distance.
     */
    public static final int MEDIUM_DISTANCE = 30;

    /**
     * Low distance confidence score.
     */
    public static final float LOW_DISTANCE_SCORE = 0.2f;

    /**
     * Low distance confidence score.
     */
    public static final float MEDIUM_DISTANCE_SCORE = -0.1f;

    /**
     * High distance confidence score.
     */
    public static final float HIGH_DISTANCE_SCORE = -0.4f;




    /**
     * Confidence increment.
     */
    public static final Float CONFIDENCE_INCREMENT = 0.1f;

    @Override
    public final void init() {
        this.setDepthControl(ProcessorDepthControl.TOKEN);
        this.setInitialized(true);
    }

    @Override
    public final String describe() {
        return "Consolidate entities based on entity frequency.";
    }

    /**
     * List of top entities.
     */
    private Set<String> top;

    /**
     * Frequency of entities.
     */
    private HashMap<String, Integer> frequency;

    @Override
    public final void extractDocument(final Document doc) {
        top = new HashSet<String>();
        frequency = new HashMap<String, Integer>();
    }

    @Override
    public final void onProcessed(final Document doc) {

        // Compute average frequency
        int total = 0;
        for (Integer i: frequency.values()) {
            total += i;
        }

        float average = (1f * total) / frequency.size();

        for (Chapter c: doc.getChapters()) {
            for (Paragraph p: c.getParagraphs()) {
                for (Sentence s: p.getSentences()) {
                    for (Token t: s.getTokens()) {
                        Entity most = t.getMostConfidentEntity();
                        for (Entity e: t.getEntities()) {
                            if (top.contains(e.getId()) && !most.equals(e)) {
                                e.setConfidence(e.getConfidence()
                                        + CONFIDENCE_INCREMENT);
                            }

                            if (frequency.get(e.getId()) > average) {
                                e.setConfidence(e.getConfidence()
                                        + CONFIDENCE_INCREMENT);
                            } else {
                                e.setConfidence(e.getConfidence()
                                    - CONFIDENCE_INCREMENT);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public final void extractToken(final Token token) {
        dbPediaRefCountToConfidence(token);

        Entity e = token.getMostConfidentEntity();
        if (e != null) {
            top.add(e.getId());
        }

        for (Entity ee: token.getEntities()) {
            if (frequency.containsKey(ee.getId())) {
                frequency.put(ee.getId(), frequency.get(ee.getId()) + 1);
            } else {
                frequency.put(ee.getId(), 1);
            }
        }
    }


    /**
     * Set confidence of entities based on ref count, and also check for
     * acronym similarity that could enforce confidence.
     *
     * @param token Token that contains entities to set confidence on
     */
    private void dbPediaRefCountToConfidence(final Token token) {
        int total = 0;
        int listSize = 0;
        int max = 0;
        Collection<Entity> list = token.getEntities();

        for (Entity e: list) {
            if (DbPediaLookupResult.class.isInstance(e)) {
                int rc = ((DbPediaLookupResult) e).getRefCount();
                total += rc;
                listSize += 1;

                if (rc > max) {
                    max = rc;
                }
            }
        }

        if (listSize == 0) {
            return;
        }

        int average = total / listSize;

        String definition = null;
        if (token.hasScore(TokenType.ACRONYM)) {
            Entity raw =
                    token.getFirstEntityByType(EntityType.ACRONYM_DEFINITION);
            if (raw != null) {
                definition = raw.getValue();
            }
        }

        for (Entity e: list) {

            if (DbPediaLookupResult.class.isInstance(e)) {
                DbPediaLookupResult ee = (DbPediaLookupResult) e;

                // One for max
                if (ee.getRefCount() == max) {
                    e.setConfidence(e.getConfidence() + CONFIDENCE_INCREMENT);
                } else {
                    e.setConfidence(e.getConfidence() - CONFIDENCE_INCREMENT);
                }

                // One for average
                if (ee.getRefCount() < average) {
                    e.setConfidence(e.getConfidence() - CONFIDENCE_INCREMENT);
                }

                // One for definition being equals to label (acronyms)
                if (definition != null && ee.getLabel().equals(definition)) {
                    e.setConfidence(e.getConfidence() + CONFIDENCE_INCREMENT);

                    // One for surface token being equals to label
                } else if (ee.getLabel().equals(token.getSurface())) {
                    e.setConfidence(e.getConfidence() + CONFIDENCE_INCREMENT);
                } else {
                    e.setConfidence(e.getConfidence() - CONFIDENCE_INCREMENT);
                }

                int l;
                if (definition != null) {
                    l = UlfStringUtils.distance(definition, ee.getLabel());
                } else {
                    l = UlfStringUtils.distance(token.getSurface(),
                            ee.getLabel());
                }

                if (l < LOW_DISTANCE) {
                    e.setConfidence(e.getConfidence() + LOW_DISTANCE_SCORE);
                } else if (l < MEDIUM_DISTANCE) {
                    e.setConfidence(e.getConfidence() + MEDIUM_DISTANCE_SCORE);
                } else {
                    e.setConfidence(e.getConfidence() + HIGH_DISTANCE_SCORE);
                }
            }
        }
    }
}
