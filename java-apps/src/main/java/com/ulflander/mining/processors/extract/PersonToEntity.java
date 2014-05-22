package com.ulflander.mining.processors.extract;

import com.ulflander.app.model.Token;
import com.ulflander.app.model.TokenType;
import com.ulflander.mining.processors.Processor;
import com.ulflander.mining.processors.ProcessorDepthControl;

/**
 * If token is obviously a person, but lookup didn't return any result, create
 * raw entities for them.
 *
 * Created by Ulflander on 5/19/14.
 */
public class PersonToEntity extends Processor {


    @Override
    public final void init() {
        this.setDepthControl(ProcessorDepthControl.TOKEN);
        this.setInitialized(true);
    }

    @Override
    public final String describe() {
        return "Person to entity.";
    }

    @Override
    public final void extractToken(final Token token) {
        if (token.hasEntity()
                || !token.hasScore()
                || !token.hasScore(TokenType.PERSON)) {
            return;
        }

    }
}
