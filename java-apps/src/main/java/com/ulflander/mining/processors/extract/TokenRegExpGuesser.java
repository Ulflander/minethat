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
    "extract.TokenCorpusGuesser"
})
public class TokenRegExpGuesser extends Processor {

    /**
     * Default score for token types found by regexps.
     */
    private static final int DEFAULT_REGEXP_SCORE = 5;

    /**
     * Low score for token types found by regexps.
     */
    private static final int LOW_REGEXP_SCORE = 1;

    /**
     * Maximum value of a decimal that could be a month.
     */
    private static final int MAX_MONTH = 31;

    /**
     * Minimum value of a decimal that could be a year.
     */
    private static final int MIN_YEAR = 1901;

    /**
     * Maximum value of a decimal that could be a year.
     */
    private static final int MAX_YEAR = 2050;

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
     * Methods to apply.
     */
    private String[] methods = new String[]{
            "isIPV4",
            "isEmail",
            "isCapitalized",
            "isHashTag",
            "isTwitterUsername",
            "isDate",
            "isMoneyAmount"
    };

    /**
     * Run processor on a Token.
     *
     * @param token Token to run processor on
     */
    @Override
    public final void extractToken(final Token token) {

        if (token.getType() == TokenType.COMMA) {
            return;
        }

        for (int i = 0, l = methods.length; i < l; i++) {
            try {
                getClass().getMethod(methods[i], Token.class)
                                            .invoke(this, token);
            } catch (Exception e) {
                LOGGER.error("Unable to run method " + methods[i]
                    + " for token recognition", e);
            }
        }
    }

    /**
     * Check whether given token is an IPV4, set type if it is.
     *
     * @param token Token to check
     */
    public final void isIPV4(final Token token) {
        if (test(Patterns.TOKEN_REC_IPADDRESS, token.getRaw())) {
            token.score(TokenType.IPV4, DEFAULT_REGEXP_SCORE);
        }
    }


    /**
     * Check whether given token is an email, set type if it is.
     *
     * @param token Token to check
     */
    public final void isHashTag(final Token token) {
        if (test(Patterns.TOKEN_REC_HASHTAG, token.getRaw())) {
            token.score(TokenType.HASHTAG, DEFAULT_REGEXP_SCORE);
        }
    }


    /**
     * Check whether given token is an email, set type if it is.
     *
     * @param token Token to check
     */
    public final void isTwitterUsername(final Token token) {

        if (test(Patterns.TOKEN_REC_TWITTER_USER, token.getRaw())) {
            token.score(TokenType.TWITTER_USERNAME, DEFAULT_REGEXP_SCORE);
        }
    }


    /**
     * Check whether given token is an email, set type if it is.
     *
     * @param token Token to check
     */
    public final void isEmail(final Token token) {

        if (test(Patterns.TOKEN_REC_EMAIL, token.getRaw())) {
            token.score(TokenType.EMAIL, DEFAULT_REGEXP_SCORE);
        }
    }


    /**
     * Check whether given token is a money amount, set type if it is.
     *
     * @param token Token to check
     */
    public final void isMoneyAmount(final Token token) {

        if (test(Patterns.TOKEN_REC_NUMBER, token.getRaw())) {
            token.score(TokenType.MONEY_AMOUNT, LOW_REGEXP_SCORE);
        } else if (test(Patterns.TOKEN_REC_MONEY_AMOUNT, token.getRaw())) {
            token.score(TokenType.MONEY_AMOUNT, DEFAULT_REGEXP_SCORE);
        }
    }


    /**
     * Check whether given token is capitalized. If it is then it increase
     * some token type scores.
     *
     * @param token Token to check
     */
    public final void isCapitalized(final Token token) {

        if (test(Patterns.TOKEN_REC_CAPITALIZED, token.getRaw())) {
            token.consolidate(TokenType.PERSON, LOW_REGEXP_SCORE);
            token.consolidate(TokenType.PERSON_PART, LOW_REGEXP_SCORE);
            token.consolidate(TokenType.LOCATION, LOW_REGEXP_SCORE);
            token.consolidate(TokenType.LOCATION_PART, LOW_REGEXP_SCORE);
            token.consolidate(TokenType.ORGANIZATION, LOW_REGEXP_SCORE);
        }
    }

    /**
     * Check whether given token is a number likely to be part of a date.
     *
     * @param token Token to check
     */
    public final void isDate(final Token token) {

        if (test(Patterns.TOKEN_REC_NUMBER, token.getRaw())) {

            int val = Integer.valueOf(token.getRaw());
            if (val > 0 && val < MAX_MONTH) {
                token.score(TokenType.DATE_PART, 1);
            }
            if (val > MIN_YEAR && val < MAX_YEAR) {
                token.score(TokenType.DATE_PART, 2);
            }

        }
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
