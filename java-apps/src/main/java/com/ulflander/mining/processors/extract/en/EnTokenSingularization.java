package com.ulflander.mining.processors.extract.en;

import com.ulflander.app.model.Token;
import com.ulflander.app.model.TokenType;
import com.ulflander.mining.processors.ILocalizedProcessor;
import com.ulflander.mining.processors.Processor;
import com.ulflander.mining.processors.Requires;
import com.ulflander.utils.EnInflector;

import java.util.ArrayList;

/**
 * Processor that singularize english tokens.
 *
 * @author Ulflander <xlaumonier@gmail.com>
 * @since 2/21/14
 */
@Requires(processors = {
    "extract.DocumentSplitter",
    "extract.LanguageDetector",
    "extract.TokenCleaner",
    "extract.TokenRegExpGuesser"
})
public class EnTokenSingularization extends Processor
    implements ILocalizedProcessor {

    /**
     * Minimum letters in a token to run singularization.
     */
    private static final int MIN_TOKEN_LETTERS = 3;

    /**
     * Exceptions.
     */
    private ArrayList<String> except =
        new ArrayList<String>();

    /**
     * Initialize the processor (add some exceptions).
     */
    @Override
    public final void init() {

        except.add("this");
        except.add("those");

        setInitialized(true);
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
     * Provides a description of this Processor.
     *
     * @return Description of this processor
     */
    @Override
    public final String describe() {
        return "Singularize english words, "
            + "operates only on token with type WORD";
    }

    /**
     * Singularize tokens.
     *
     * @param token Token to run processor on
     */
    @Override
    public final void extractToken(final Token token) {
        if (token.getType() != TokenType.WORD
            && token.getType() != TokenType.KEYWORD) {
            return;
        }

        String raw = token.getSurface().toLowerCase();

        if (raw.length() < MIN_TOKEN_LETTERS || except.contains(raw)) {
            return;
        }

        String str = EnInflector.singularize(raw);
        if (str != null && !raw.equals(str)) {
            token.setSingular(str);
        }
    }

}
