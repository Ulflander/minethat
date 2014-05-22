package com.ulflander.mining.processors.extract;

import com.ulflander.app.model.Sentence;
import com.ulflander.app.model.Token;
import com.ulflander.app.model.TokenType;
import com.ulflander.mining.processors.Processor;
import com.ulflander.mining.processors.ProcessorDepthControl;

import java.util.ArrayList;
import java.util.List;

/**
 * Aggregates tokens.
 *
 * Created by Ulflander on 4/29/14.
 */
public class TokenAggregator extends Processor {

    @Override
    public final void init() {
        this.setDepthControl(ProcessorDepthControl.SENTENCE);
        this.setInitialized(true);
    }

    @Override
    public final String describe() {
        return "Aggregates tokens";
    }

    @Override
    public final void extractSentence(final Sentence sentence) {

        int i = 0;
        List<Token> toMerge = new ArrayList<Token>();
        Token prev;
        for (Token t: sentence.getTokens()) {

            prev = t.getPrevious();

            if (isSkipable(t) || isSkipable(prev)) {
                continue;
            }

            // Basic, try to merge with previous:
            // must have a score and share a score with previous
            if (t.hasScore()
                && prev != null
                && t.shareSomeScore(prev)) {

                // Check descriptor score versus other scores.
                int descriptorScore = 0,
                    entityScore = 0;

                for (TokenType tt: prev.getScores().keySet()) {
                    if (tt == TokenType.DATE_DESCRIPTOR
                        || tt == TokenType.LOCATION_DESCRIPTOR
                        || tt == TokenType.PERSON_DESCRIPTOR
                        || tt == TokenType.ORGANIZATION_DESCRIPTOR) {
                        descriptorScore += prev.getScore(tt);
                    } else {
                        entityScore += prev.getScore(tt);
                    }
                }

                // If more descriptor than anything else, then pass.
                if (descriptorScore > entityScore) {
                    continue;
                }


                toMerge.add(t);
            }
        }

        for (Token t: toMerge) {
            prev = t.getPrevious();
            sentence.mergeToPrevious(t);

            if (prev.hasScore(TokenType.PERSON_PART)) {
                prev.score(TokenType.PERSON,
                        prev.getScore(TokenType.PERSON_PART));
                prev.discriminate(TokenType.PERSON_PART);
            }
        }
    }

    /**
     * Check if a token must be skipped (token like parenthesis).
     *
     * @param t Token
     * @return True if token should be skipped, false otherwise
     */
    private boolean isSkipable(final Token t) {

        // Check for special types to skip
        return t == null
                || t.getType() == TokenType.OPEN_PARENTHESIS
                || t.getType() == TokenType.CLOSE_PARENTHESIS
                || t.getType() == TokenType.OPEN_PARENTHESIS
                || t.getType() == TokenType.CLOSE_PARENTHESIS
                || t.getType() == TokenType.OPEN_PARENTHESIS
                || t.getType() == TokenType.CLOSE_PARENTHESIS;

    }
}
