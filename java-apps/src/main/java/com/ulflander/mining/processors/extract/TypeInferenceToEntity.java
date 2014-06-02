package com.ulflander.mining.processors.extract;

import com.ulflander.app.model.Entity;
import com.ulflander.app.model.EntitySource;
import com.ulflander.app.model.Token;
import com.ulflander.app.model.TokenType;
import com.ulflander.mining.processors.Processor;
import com.ulflander.mining.processors.ProcessorDepthControl;

/**
 * When no entity, but token types with strong evidence of being a type,
 * transforms it into raw entity.
 *
 * Created by Ulflander on 5/18/14.
 */
public class TypeInferenceToEntity extends Processor {

    /**
     * Minimum score to allow inference.
     */
    public static final int MIN_SCORE = 10;

    /**
     * Default confidence for newly created entities.
     */
    public static final float DEFAULT_CONFIDENCE = 0.36f;


    @Override
    public final void init() {
        this.setDepthControl(ProcessorDepthControl.TOKEN);
        this.setInitialized(true);
    }

    @Override
    public final String describe() {
        return "Infer existence of entity based on types";
    }

    @Override
    public final void extractToken(final Token token) {

        if (token.getAggregated() || token.hasEntity()) {
            return;
        }

        int types = token.getScores().size(),
            totalScore = 0,
            bestScore = 0,
            s;

        if (types == 0) {
            return;
        }

        TokenType best = null;

        for (TokenType tt: token.getScores().keySet()) {
            s = token.getScore(tt);
            totalScore += s;
            if (s > bestScore) {
                best = tt;
                bestScore = s;
            }
        }

        if (best == null) {
            return;
        }

        if (bestScore > MIN_SCORE
                && bestScore >= (totalScore / types)) {

            Entity e = new Entity();
            e.setSource(EntitySource.RAW);
            e.setValue(token.getClean());
            e.setConfidence(DEFAULT_CONFIDENCE);
            e.addClass(TokenType.toRDFClasse(best));
            e.setType(TokenType.toEntityType(best));
            e.setLabel(token.getSurface());

            token.addEntity(e);
        }
    }

}
