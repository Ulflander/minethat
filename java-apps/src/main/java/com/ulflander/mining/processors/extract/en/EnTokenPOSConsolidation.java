package com.ulflander.mining.processors.extract.en;

import com.ulflander.app.model.Token;
import com.ulflander.app.model.TokenType;
import com.ulflander.mining.nlp.PennPOSTag;
import com.ulflander.mining.processors.ILocalizedProcessor;
import com.ulflander.mining.processors.Processor;
import com.ulflander.mining.processors.ProcessorDepthControl;
import com.ulflander.mining.processors.Requires;

/**
 * Processor that consolidates token type based on POST tag.
 *
 * @author Ulflander <xlaumonier@gmail.com>
 * @since 2/21/14
 */
@Requires(processors = {
        "extract.POSTagger"
})
public class EnTokenPOSConsolidation extends Processor
    implements ILocalizedProcessor {

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
        return "Consolidate token type";
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

    @Override
    public final void extractToken(final Token token) {

        PennPOSTag tag = token.getTag();
        TokenType curr = token.getType();

        if (curr == TokenType.VERB
            || curr == TokenType.OPERATOR
            || (tag == PennPOSTag.ADJECTIVE
                || tag == PennPOSTag.ADJECTIVE_COMPARATIVE
                || tag == PennPOSTag.ADJECTIVE_SUPERLATIVE
                || tag == PennPOSTag.CONJUNCTION_SUBORDINATING
                || tag == PennPOSTag.CONJUNCTION_COORDINATING
                || tag == PennPOSTag.DETERMINER
                || tag == PennPOSTag.DETERMINER_WH
                || tag == PennPOSTag.EXISTENTIAL_THERE)) {

            token.discriminate(TokenType.ORGANIZATION);
            token.discriminate(TokenType.PERSON_PART);
            token.discriminate(TokenType.PERSON);
            token.discriminate(TokenType.LOCATION);
            token.discriminate(TokenType.LOCATION_PART);

        } else if ((tag == PennPOSTag.NOUN_PROPER_PLURAL
                || tag == PennPOSTag.NOUN_PROPER_SINGULAR)) {

            token.consolidate(TokenType.PERSON_PART, 1);
            token.consolidate(TokenType.PERSON, 1);
            token.consolidate(TokenType.LOCATION, 1);
            token.consolidate(TokenType.LOCATION_PART, 1);
            token.consolidate(TokenType.ORGANIZATION, 1);

        } else if ((tag == PennPOSTag.NOUN
                || tag == PennPOSTag.NOUN_PLURAL)) {

            token.consolidate(TokenType.PERSON_PART, -1);
            token.consolidate(TokenType.PERSON, -1);
            token.consolidate(TokenType.LOCATION, -1);
            token.consolidate(TokenType.LOCATION_PART, -1);
            token.consolidate(TokenType.ORGANIZATION, -1);

        }

    }
}
