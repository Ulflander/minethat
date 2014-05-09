package com.ulflander.mining.processors.extract;

import com.ulflander.app.model.Sentence;
import com.ulflander.app.model.Token;
import com.ulflander.app.model.TokenType;
import com.ulflander.mining.processors.ILocalizedProcessor;
import com.ulflander.mining.processors.Processor;
import com.ulflander.mining.processors.ProcessorDepthControl;
import com.ulflander.mining.processors.Requires;

/**
 * Run on a Sentence: consolidate token scores based on
 * the fact surrounding tokens have the same type.
 *
 *
 * @author Ulflander <xlaumonier@gmail.com>
 * @since 2/21/14
 */
@Requires(processors = {
        "extract.DocumentTokenizer"
})
public class TokenSiblingsConsolidation extends Processor
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
        return "Consolidate token type based on siblings tokens";
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

    @Override
    public final void extractSentence(final Sentence sentence) {
        Token token = sentence.getTokenAt(0);
        Token next;
        Token prev;

        while (token != null) {
            next = token.getNext();
            if (next != null) {
                next.consolidate(token.getScores(), 1);
                token.consolidate(next.getScores(), 1);


                // Special case for LOCATION_PART
                // If next is a comma, lookup next one
                if (token.hasScore(TokenType.LOCATION_PART)
                        && next.getType() == TokenType.COMMA
                        && next.hasNext()) {
                    token.consolidate(TokenType.LOCATION_PART, 2);
                    next.getNext().consolidate(TokenType.LOCATION_PART, 2);
                }




                // Special case for NAME PART
                // If length is 1, and previous previous one
                // is possibly a name, and next is possibly a name
                // and all are KEYWORDS
                // then consolidate name parts
                if (token.size() == 1
                        && token.getType() == TokenType.KEYWORD
                        && next.getType() == TokenType.KEYWORD) {
                    prev = token.getPrevious();
                    if (prev != null
                            && prev.getType() == TokenType.KEYWORD) {

                        token.consolidate(TokenType.PERSON_PART, 2, 2);
                    }
                }
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
