package com.ulflander.mining.processors.extract;

import com.ulflander.app.model.Entity;
import com.ulflander.app.model.EntitySource;
import com.ulflander.app.model.EntityType;
import com.ulflander.app.model.Token;
import com.ulflander.app.model.TokenType;
import com.ulflander.mining.processors.Processor;
import com.ulflander.mining.processors.ProcessorDepthControl;

/**
 * Extract twitter usernames.
 *
 * Created by Ulflander on 5/18/14.
 */
public class TwitterSpecificsToEntity extends Processor {

    /**
     * Minimum score to allow inference.
     */
    public static final int MIN_SCORE = 3;

    /**
     * Default confidence for newly created entities.
     */
    public static final float DEFAULT_CONFIDENCE = 0.5f;


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

        if (token.getScores().size() == 1
                && token.hasScore(TokenType.TWITTER_USERNAME)) {
            Entity e = new Entity();
            e.setSource(EntitySource.RAW);
            e.setValue(token.getClean());
            e.setConfidence(DEFAULT_CONFIDENCE);
            e.addClass("http://schema.org/Thing");
            e.setType(EntityType.TWITTER_USERNAME);
            e.setLabel(token.getSurface());

            token.addEntity(e);

        }

    }

}
