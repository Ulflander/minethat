package com.ulflander.mining.processors.extract;

import com.ulflander.app.model.Language;
import com.ulflander.app.model.Token;
import com.ulflander.app.model.TokenType;
import com.ulflander.mining.Patterns;
import com.ulflander.mining.processors.Processor;
import com.ulflander.mining.processors.Requires;
import com.ulflander.utils.UlfStringUtils;
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
    private static final int LOW_REGEXP_SCORE = 2;

    /**
     * Minor score for token types found by regexps.
     */
    private static final int MINOR_REGEXP_SCORE = 1;

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
     * Minimum value of a decimal that is probably a year.
     */
    private static final int MIN_PROBABLE_YEAR = 1995;

    /**
     * Maximum value of a decimal that is probably a year.
     */
    private static final int MAX_PROBABLE_YEAR = 2015;

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
            "isUppercased",
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
        if (test(Patterns.TOKEN_REC_IPADDRESS, token.getSurface())) {
            token.score(TokenType.IPV4, DEFAULT_REGEXP_SCORE);
        }
    }


    /**
     * Check whether given token is an email, set type if it is.
     *
     * @param token Token to check
     */
    public final void isHashTag(final Token token) {
        if (test(Patterns.TOKEN_REC_HASHTAG, token.getSurface())) {
            token.score(TokenType.HASHTAG, DEFAULT_REGEXP_SCORE);
        }
    }


    /**
     * Check whether given token is an email, set type if it is.
     *
     * @param token Token to check
     */
    public final void isTwitterUsername(final Token token) {

        if (test(Patterns.TOKEN_REC_TWITTER_USER, token.getSurface())) {
            token.score(TokenType.TWITTER_USERNAME, DEFAULT_REGEXP_SCORE);
        }
    }


    /**
     * Check whether given token is an email, set type if it is.
     *
     * @param token Token to check
     */
    public final void isEmail(final Token token) {

        if (test(Patterns.TOKEN_REC_EMAIL, token.getSurface())) {
            token.score(TokenType.EMAIL, DEFAULT_REGEXP_SCORE);
        }
    }


    /**
     * Check whether given token is a money amount, set type if it is.
     *
     * @param token Token to check
     */
    public final void isMoneyAmount(final Token token) {

        /*
            First if only a number, we give a very low score
         */
        if (test(Patterns.TOKEN_REC_NUMBER, token.getSurface())
            || token.getType() == TokenType.NUMERIC) {
            token.score(TokenType.MONEY_AMOUNT, MINOR_REGEXP_SCORE);

        /*
            If number + chars that could be some money currencies or
            quantifiers, a normal score.
         */
        } else if (test(Patterns.TOKEN_REC_MONEY_AMOUNT, token.getSurface())) {
                token.score(TokenType.MONEY_AMOUNT, DEFAULT_REGEXP_SCORE);

        /*
            If only a currency char, low score.
         */
        } else if (test(Patterns.TOKEN_REC_MONEY_CURRENCY_CHARS,
                token.getSurface())) {
            token.score(TokenType.MONEY_AMOUNT, DEFAULT_REGEXP_SCORE);
        }
    }


    /**
     * Check whether given token is capitalized. If it is then it increase
     * some token type scores (mainly named entities: persons, organizations...)
     *
     * @param token Token to check
     */
    public final void isCapitalized(final Token token) {

        String str = token.getSurface();

        /*
            Capitalization rule can't apply if most of the
            token is uppercased: it has no meaning anymore, we seek
            here strict capitalization.
         */
        if (UlfStringUtils.isMostlyUppercase(str)) {
            return;
        }

        /*
            If token is capitalized, we consider all concepts
            as reinforced.
         */
        if (test(Patterns.TOKEN_REC_CAPITALIZED, str)) {
            token.consolidate(TokenType.PERSON, LOW_REGEXP_SCORE);
            token.consolidate(TokenType.PERSON_PART, LOW_REGEXP_SCORE);
            token.consolidate(TokenType.LOCATION, LOW_REGEXP_SCORE);
            token.consolidate(TokenType.LOCATION_PART, LOW_REGEXP_SCORE);
            token.consolidate(TokenType.ORGANIZATION, LOW_REGEXP_SCORE);

        /*
            If not capitalized, then less chances that they are named
            entities.
         */
        } else {
            token.consolidate(TokenType.PERSON, -LOW_REGEXP_SCORE);
            token.consolidate(TokenType.PERSON_PART, -LOW_REGEXP_SCORE);
            token.consolidate(TokenType.LOCATION, -LOW_REGEXP_SCORE);
            token.consolidate(TokenType.LOCATION_PART, -LOW_REGEXP_SCORE);
            token.consolidate(TokenType.ORGANIZATION, -LOW_REGEXP_SCORE);
        }
    }

    /**
     * Check whether given token is capitalized. If it is then it increase
     * some token type scores (mainly named entities: persons, organizations...)
     *
     * @param token Token to check
     */
    public final void isUppercased(final Token token) {

        String str = token.getSurface();

        /*
            Capitalization rule can't apply if most of the
            token is uppercased: it has no meaning anymore, we seek
            here strict capitalization.
         */
        if (UlfStringUtils.isMostlyUppercase(str)) {
            token.consolidate(TokenType.ORGANIZATION, LOW_REGEXP_SCORE);
            token.score(TokenType.ACRONYM, LOW_REGEXP_SCORE);
        }
    }

    /**
     * Check whether given token is a number likely to be part of a date.
     *
     * @param token Token to check
     */
    public final void isDate(final Token token) {

        /*
            If a number
         */
        if (test(Patterns.TOKEN_REC_NUMBER, token.getSurface())) {

            int val = Integer.valueOf(token.getSurface());
            /*
                and could be a month: low score of 1 ( there's a high
                probability to find numbers between 1 && 31 for money amounts as
                well or any other quantification).
             */
            if (val > 0 && val <= MAX_MONTH) {
                token.score(TokenType.DATE_PART, LOW_REGEXP_SCORE);
            }

            /*
                For years, there is a bit more chance that numbers between
                1901 and 2050 are actually a year.
             */
            if (val >= MIN_YEAR && val <= MAX_YEAR) {
                token.score(TokenType.DATE_PART, LOW_REGEXP_SCORE);
            }

            /*
                For years, there is a bit more chance that numbers between
                1901 and 2050 are actually a year.
             */
            if (val >= MIN_PROBABLE_YEAR && val <= MAX_PROBABLE_YEAR) {
                token.score(TokenType.DATE_PART, LOW_REGEXP_SCORE);
            }

        /*
            If like an hour.
         */
        } else if (test(Patterns.TOKEN_REC_HOUR, token.getSurface())) {
            token.score(TokenType.DATE_PART, DEFAULT_REGEXP_SCORE);

        /*
            mid-2013
         */
        } else if (test(Patterns.TOKEN_REC_DATE_YEAR_PREFIXED,
                token.getSurface())) {
            token.score(TokenType.DATE_PART, DEFAULT_REGEXP_SCORE);

        /*
            AM/PM
         */
        } else if (current().getLanguage() == Language.EN) {
            String c = token.getClean();

            if (c.equals("am") || c.equals("pm")) {
                token.score(TokenType.DATE_PART, LOW_REGEXP_SCORE);
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
