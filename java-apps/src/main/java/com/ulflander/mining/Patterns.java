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

    ///////////////////////////////////
    // EXTRACT
    ///////////////////////////////////
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
     * Pattern to recognize an email address.
     */
    public static final Pattern TOKEN_REC_EMAIL =
            Pattern.compile("^.+@.+\\.[a-zA-Z]+$");

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

    ///////////////////////////////////
    // AUGMENT
    ///////////////////////////////////

}
