package com.ulflander.mining.processors.extract;

import com.ulflander.application.model.Sentence;
import com.ulflander.application.model.Token;
import com.ulflander.application.model.TokenType;
import com.ulflander.mining.processors.ILocalizedProcessor;
import com.ulflander.mining.processors.Processor;
import com.ulflander.mining.processors.ProcessorDepthControl;

import java.util.HashMap;

/**
 * Processor that consolidates token type.
 *
 * @author Ulflander <xlaumonier@gmail.com>
 * @since 2/21/14
 */
public class TokenGuessConsolidation extends Processor
    implements ILocalizedProcessor {

    /**
     * Initialize the processor.
     */
    @Override
    public final void init() {
        setDepthControl(ProcessorDepthControl.SENTENCE);
        setInitialized(true);
    }

    /**
     * Provides a description of this Processor.
     *
     * @return Description of this processor
     */
    @Override
    public final String describe() {
        return "Consolidate token type";
    }

    /**
     * Get languages supported by this processor.
     *
     * @return Array of language codes
     */
    @Override
    public final String[] getLanguages() {
        return new String[]{"en", "fr", "es"};
    }

    /**
     * Run processor on a Sentence.
     *
     * @param sentence Sentence to run processor on
     */
    @Override
    public final void extractSentence(final Sentence sentence) {
        Token token = sentence.getTokenAt(0);
        Token next;
        TokenType tt;
        HashMap<TokenType, Integer> scores;

        while (token != null) {
            next = token.getNext();
            if (next != null) {
                next.consolidate(token.getScores(), 1);
                token.consolidate(next.getScores(), 1);
            }
            token = next;
        }
    }

    /**
     * Consolidate token type.
     *
     * @param token Token to consolidate
     * @return Token reference
     */
    public final Token consolidate(final Token token) {
        return token;
    }
}
