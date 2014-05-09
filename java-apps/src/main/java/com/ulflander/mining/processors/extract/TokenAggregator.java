package com.ulflander.mining.processors.extract;

import com.ulflander.app.model.Sentence;
import com.ulflander.app.model.Token;
import com.ulflander.app.model.TokenType;
import com.ulflander.mining.processors.Processor;
import com.ulflander.mining.processors.ProcessorDepthControl;

/**
 * Aggregates tokens.
 *
 * Created by Ulflander on 4/29/14.
 */
public class TokenAggregator extends Processor {

    @Override
    public void init() {
        this.setDepthControl(ProcessorDepthControl.SENTENCE);
        this.setInitialized(true);
    }

    @Override
    public String describe() {
        return "Aggregates tokens";
    }

    @Override
    public void extractSentence(Sentence sentence) {

        int i = 0;
        Token prev;
        for (Token t: sentence.getTokens()) {

            // Basic, try to merge with previous:
            // must have a score and share a score with previous
            if (t.hasScore()
                && (prev = t.getPrevious()) != null
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

                System.out.println("Would merge " + t.getPrevious().getClean()
                        + " with " + t.getClean());
            }

        }

    }
}
