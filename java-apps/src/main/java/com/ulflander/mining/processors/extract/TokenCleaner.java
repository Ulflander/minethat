package com.ulflander.mining.processors.extract;

import com.ulflander.app.model.Language;
import com.ulflander.app.model.Sentence;
import com.ulflander.app.model.Token;
import com.ulflander.mining.Patterns;
import com.ulflander.mining.processors.Processor;
import com.ulflander.mining.processors.ProcessorDepthControl;
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
        setDepthControl(ProcessorDepthControl.SENTENCE);
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
     * Clean all tokens from some endpoint chars, also for english language,
     * TokenCleaner strips accents from sentences.
     *
     * @see com.ulflander.mining.Patterns
     * @param sentence Sentence to run processor on
     */
    @Override
    public final void extractSentence(final Sentence sentence) {
        for (Token t: sentence.getTokens()) {

            t.setSurface(StringUtils.strip(
                    t.getSurface(), Patterns.TOKEN_CLEANER_ENDPOINTS));

            if (sentence.getLanguage() == Language.EN) {
                t.setClean(StringUtils.stripAccents(t.getClean()));
            }
        }
    }

}
