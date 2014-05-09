package com.ulflander.mining.processors.extract;

import com.ulflander.app.model.Token;
import com.ulflander.app.model.TokenType;
import com.ulflander.mining.nlp.PennPOSTag;
import com.ulflander.mining.processors.Processor;
import com.ulflander.mining.processors.ProcessorDepthControl;
import com.ulflander.mining.processors.Requires;

/**
 * This processors infers that if a token is a keyword, and another token
 * next to it has no type, then it could be the same type of token.
 *
 *
 *
 * Created by Ulflander on 4/14/14.
 */
@Requires(processors = {
        "extract.DocumentTokenizer"
})
public class TokenInferConsolidation extends Processor {

    @Override
    public final void init() {
        this.setInitialized(true);
        this.setDepthControl(ProcessorDepthControl.TOKEN);
    }

    @Override
    public final String describe() {
        return "Infer type of sibling keywords based on token type.";
    }


    @Override
    public final void extractToken(final Token token) {

        if (token.getType() != TokenType.KEYWORD) {
            return;
        }


        if (!token.hasScore() && token.hasPrevious()
                && token.isCapitalized()
                && token.getPrevious().getType() == TokenType.KEYWORD) {

            for (TokenType tt: token.getPrevious().getScores().keySet()) {
                token.infer(tt, token.getPrevious().getScore(tt) / 3);
            }

        }

        if (!token.hasScore() && token.hasNext()
                && token.isCapitalized()
                && token.getNext().getType() == TokenType.KEYWORD) {

            for (TokenType tt: token.getNext().getScores().keySet()) {
                token.infer(tt, token.getNext().getScore(tt) / 2);
            }

        }

        if (token.hasNext()) {
            Token next = token.getNext();
            int max = 4;
            while (next != null && max > 0 && next.getType() == TokenType.OPERATOR) {
                next = next.getNext();
                max--;
            }

            if (max == 0 || next == null) {
                return;
            }

            if (next.getType() == TokenType.KEYWORD &&
                next.getTag() == PennPOSTag.NOUN_PROPER_SINGULAR) {

                if (token.hasUniqueScore(TokenType.PERSON_DESCRIPTOR)) {
                    next.infer(TokenType.PERSON_PART,
                            token.getScore(TokenType.PERSON_DESCRIPTOR));

                } else if (token.hasUniqueScore(
                        TokenType.LOCATION_DESCRIPTOR)) {
                    next.infer(TokenType.LOCATION_PART,
                            token.getScore(TokenType.LOCATION_DESCRIPTOR));

                } else if (token.hasUniqueScore(
                        TokenType.ORGANIZATION_DESCRIPTOR)) {
                    next.infer(TokenType.ORGANIZATION,
                            token.getScore(TokenType.ORGANIZATION_DESCRIPTOR));
                }

            }

        }
    }

}
