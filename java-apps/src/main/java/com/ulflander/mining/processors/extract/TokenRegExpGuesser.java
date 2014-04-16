package com.ulflander.mining.processors.extract;

import com.ulflander.application.model.Token;
import com.ulflander.application.model.TokenType;
import com.ulflander.mining.Patterns;
import com.ulflander.mining.processors.Processor;
import com.ulflander.mining.processors.Requires;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.regex.Pattern;

/**
 * Processor that attempt to give a type to tokens.
 *
 * @author Ulflander <xlaumonier@gmail.com>
 * @since 2/21/14
 */
@Requires(processors = {
    "extract.TokenCleaner"
})
public class TokenRegExpGuesser extends Processor {

    /**
     * Default score for token types found by regexps.
     */
    private static final int DEFAULT_REGEXP_SCORE = 2;

    /**
     * Logger.
     */
    private static final Logger LOGGER =
        LogManager.getLogger(TokenRegExpGuesser.class);

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
        return "Guess the type of a token between "
            + "various possibilities: WORD, EMAIL, IP...";
    }

    /**
     * Run processor on a Token.
     *
     * @param token Token to run processor on
     */
    @Override
    public final void extractToken(final Token token) {
        String[] methods = new String[]{
                "isIPV4",
                "isEmail",
                "isHashTag",
                "isTwitterUsername"
        };

        for (int i = 0, l = methods.length; i < l; i++) {
            Boolean res = false;
            try {
                res = (Boolean) getClass().getMethod(methods[i], Token.class)
                                            .invoke(this, token);
            } catch (Exception e) {
                LOGGER.error("Unable to run method " + methods[i]
                    + " for token recognition", e);
            }
            if (res) {
                return;
            }
        }
    }

    /**
     * Check whether given token is an IPV4, set type if it is.
     *
     * @param token Token to check
     * @return True if token is an IPV4, false otherwise
     */
    public final Boolean isIPV4(final Token token) {

        if (test(Patterns.TOKEN_REC_IPADDRESS, token.getRaw())) {
            token.score(TokenType.IPV4, DEFAULT_REGEXP_SCORE);
            return true;
        }

        return false;
    }


    /**
     * Check whether given token is an email, set type if it is.
     *
     * @param token Token to check
     * @return True if token is an email, false otherwise
     */
    public final Boolean isHashTag(final Token token) {

        if (test(Patterns.TOKEN_REC_HASHTAG, token.getRaw())) {
            token.score(TokenType.HASHTAG, DEFAULT_REGEXP_SCORE);
            return true;
        }

        return false;
    }


    /**
     * Check whether given token is an email, set type if it is.
     *
     * @param token Token to check
     * @return True if token is an email, false otherwise
     */
    public final Boolean isTwitterUsername(final Token token) {

        if (test(Patterns.TOKEN_REC_TWITTER_USER, token.getRaw())) {
            token.score(TokenType.TWITTER_USERNAME, DEFAULT_REGEXP_SCORE);
            return true;
        }

        return false;
    }


    /**
     * Check whether given token is an email, set type if it is.
     *
     * @param token Token to check
     * @return True if token is an email, false otherwise
     */
    public final Boolean isEmail(final Token token) {

        if (test(Patterns.TOKEN_REC_EMAIL, token.getRaw())) {
            token.score(TokenType.EMAIL, DEFAULT_REGEXP_SCORE);
            return true;
        }

        return false;
    }


    /**
     * Test a pattern onto a string.
     *
     * @param p Pattern
     * @param raw String to test
     * @return True if pattern has matches, false otherwise
     */
    private Boolean test(final Pattern p, final String raw) {
        return p.matcher(raw).matches();
    }


}
