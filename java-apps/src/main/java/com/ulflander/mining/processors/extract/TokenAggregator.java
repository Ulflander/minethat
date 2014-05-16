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
            sentence.mergeToPrevious(t);
        }

    }
}
