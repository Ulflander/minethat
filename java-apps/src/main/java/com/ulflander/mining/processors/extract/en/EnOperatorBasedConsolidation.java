package com.ulflander.mining.processors.extract.en;

import com.ulflander.app.model.Token;
import com.ulflander.app.model.TokenConsolidationDirection;
import com.ulflander.app.model.TokenType;
import com.ulflander.mining.processors.ILocalizedProcessor;
import com.ulflander.mining.processors.Processor;
import com.ulflander.mining.processors.ProcessorDepthControl;
import com.ulflander.mining.processors.Requires;

/**
 * Consolidate some entity token type based on operators.
 *
 *
 * Created by Ulflander on 4/26/14.
 */
@Requires(processors = {
        "extract.POSTagger"
})
public class EnOperatorBasedConsolidation extends Processor
        implements ILocalizedProcessor {

    /**
     * Default score.
     */
    private static final int DEFAULT_SCORE = 3;

    /**
     * Default radius.
     */
    private static final int DEFAULT_RADIUS = 3;

    /**
     * Initialize the processor.
     */
    @Override
    public final void init() {
        setDepthControl(ProcessorDepthControl.TOKEN);
        setInitialized(true);
    }

    /**
     * Provides a description of this Processor.
     *
     * @return Description of this processor
     */
    @Override
    public final String describe() {
        return "Consolidate token type based on operators before.";
    }

    /**
     * Get languages supported by this processor.
     *
     * @return Array of language codes
     */
    @Override
    public final String[] getLanguages() {
        return new String[]{"en"};
    }

    /**
     * For each token, if token is a particular operator,
     * it will consolidate sibling tokens types.
     *
     * @param token Token to run processor on
     */
    @Override
    public final void extractToken(final Token token) {

        if (token.getType() != TokenType.OPERATOR) {
            return;
        }

        String s = token.getClean();

        if (s.equals("on") || s.equals("since")) {
            token.consolidate(TokenType.DATE_PART, DEFAULT_SCORE,
                    DEFAULT_RADIUS, TokenConsolidationDirection.FORWARD);
        }

        else if (s.equals("by")) {
            token.consolidate(TokenType.PERSON_PART, DEFAULT_SCORE,
                    DEFAULT_RADIUS, TokenConsolidationDirection.FORWARD);
        }

        else if (s.equals("in")) {
            token.consolidate(TokenType.LOCATION_PART, DEFAULT_SCORE,
                    DEFAULT_RADIUS, TokenConsolidationDirection.FORWARD);

            token.consolidate(TokenType.LOCATION_DESCRIPTOR, DEFAULT_SCORE,
                    DEFAULT_RADIUS, TokenConsolidationDirection.FORWARD);

            token.consolidate(TokenType.DATE_PART, DEFAULT_SCORE,
                    DEFAULT_RADIUS, TokenConsolidationDirection.FORWARD);
        }

        else if (s.equals("of") || s.equals("to") || s.equals("from")) {
            token.consolidate(TokenType.LOCATION_PART, DEFAULT_SCORE,
                    DEFAULT_RADIUS, TokenConsolidationDirection.FORWARD);

            token.consolidate(TokenType.LOCATION_DESCRIPTOR, DEFAULT_SCORE,
                    DEFAULT_RADIUS, TokenConsolidationDirection.FORWARD);

            token.consolidate(TokenType.PERSON_PART, DEFAULT_SCORE,
                    DEFAULT_RADIUS, TokenConsolidationDirection.FORWARD);

            token.consolidate(TokenType.PERSON_DESCRIPTOR, DEFAULT_SCORE,
                    DEFAULT_RADIUS, TokenConsolidationDirection.FORWARD);

            token.consolidate(TokenType.ORGANIZATION, DEFAULT_SCORE,
                    DEFAULT_RADIUS, TokenConsolidationDirection.FORWARD);

            token.consolidate(TokenType.ORGANIZATION_DESCRIPTOR, DEFAULT_SCORE,
                    DEFAULT_RADIUS, TokenConsolidationDirection.FORWARD);

            token.consolidate(TokenType.DATE_PART, DEFAULT_SCORE,
                    DEFAULT_RADIUS, TokenConsolidationDirection.FORWARD);

            token.consolidate(TokenType.DATE_DESCRIPTOR, DEFAULT_SCORE,
                    DEFAULT_RADIUS, TokenConsolidationDirection.FORWARD);

            token.consolidate(TokenType.MONEY_AMOUNT, DEFAULT_SCORE,
                    DEFAULT_RADIUS, TokenConsolidationDirection.FORWARD);
        }
    }
}
