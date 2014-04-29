package com.ulflander.mining.processors.extract;

import com.ulflander.mining.Patterns;
import com.ulflander.app.model.Token;
import com.ulflander.mining.processors.Processor;
import com.ulflander.mining.processors.Requires;
import org.apache.commons.lang3.StringUtils;

/**
 * Processor that clean a token.
 *
 * @author Ulflander <xlaumonier@gmail.com>
 * @since 2/21/14
 */
@Requires(processors = {
    "extract.DocumentTokenizer"
})
public class TokenCleaner extends Processor {

    /**
     * Initialize the processor.
     */
    @Override
    public final void init() {
        setInitialized(true);
    }


    /**
     * Provides a description of this Processor.
     *
     * @return Description of this processor
     */
    @Override
    public final String describe() {
        return "Clean tokens by removing some chars "
            + "at the beginning or the end of the raw string";
    }

    /**
     * Run processor on a Token.
     *
     * @param token Token to run processor on
     */
    @Override
    public final void extractToken(final Token token) {
        token.setRaw(StringUtils.strip(
            token.getRaw(), Patterns.TOKEN_CLEANER_ENDPOINTS));
    }


}
