package com.ulflander.mining;

import java.util.regex.Pattern;

/**
 * Contains some useful Regular Expression patterns and some handy lists
 * of chars for various tasks.
 */
public final class Patterns {

    /**
     * Private constructor.
     */
    private Patterns() {

    }

    /**
     * Pattern that split a text into paragraphs.
     */
    public static final Pattern CHAPTER_TO_PARAGRAPH =
        Pattern.compile("(?<=(\n))([ \t]*$)+", Pattern.MULTILINE);

    /**
     * Chars that can't be at he beginning or the end of a token.
     */
    public static final String TOKEN_CLEANER_ENDPOINTS =
        ".-;:!?&()[]{}";

    /**
     * String usable for a RegExp that cheks if there is at least one lowercase
     * letter in a text, in any latin language.
     */
    public static final String ABSTRACT_TEXT_LOWERCASE =
        ".*\\p{javaLowerCase}+.*";

    /**
     * String usable for a RegExp that checks if there is only uppercase
     * letters in a text, in any latin language.
     */
    public static final Pattern IS_TEXT_UPPERCASE =
            Pattern.compile("^\\p{javaUpperCase}+$");

    /**
     * Pattern to recognize an email address.
     */
    public static final Pattern TOKEN_REC_EMAIL =
            Pattern.compile("^.+@.+\\.[a-zA-Z]+$");

    /**
     * Pattern to check capitalization.
     */
    public static final Pattern TOKEN_REC_CAPITALIZED =
            Pattern.compile("^[A-Z][a-z]+$");

    /**
     * Pattern to recognize a twitter username.
     */
    public static final Pattern TOKEN_REC_TWITTER_USER =
            Pattern.compile("^@[a-zA-Z0-9_]+$");

    /**
     * Pattern to recognize a hashTag.
     */
    public static final Pattern TOKEN_REC_HASHTAG =
            Pattern.compile("^#[a-zA-Z0-9_]+$");

    /**
     * Pattern to recognize a number.
     */
    public static final Pattern TOKEN_REC_NUMBER =
            Pattern.compile("^[0-9]+$");

    /**
     * Pattern to recognize a money amount.
     */
    public static final Pattern TOKEN_REC_MONEY_AMOUNT =
            Pattern.compile("^[0-9\\.,$€£kmb]+$");

    /**
     * Pattern to recognize an hour.
     */
    public static final Pattern TOKEN_REC_HOUR =
            Pattern.compile("[0-9]{1,2}:[0-9]{1,2}$");

    /**
     * Pattern to recognize some currency chars.
     */
    public static final Pattern TOKEN_REC_MONEY_CURRENCY_CHARS =
            Pattern.compile("[$€£]+");

    /**
     * Pattern to recognize an IPV4 address.
     */
    public static final Pattern TOKEN_REC_IPADDRESS =
        Pattern.compile(
            "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
            + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
            + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
            + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

    /**
     * Pattern that recognize an url.
     */
    public static final Pattern IS_URL =
        Pattern.compile("^(https?|ftp)"
            + "://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");


}
