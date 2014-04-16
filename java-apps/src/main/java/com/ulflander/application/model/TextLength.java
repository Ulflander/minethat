package com.ulflander.application.model;

/**
 * Lenght indicator of a text.
 *
 * TextLength applies to Documents, Chapters, Paragraphs and Sentences.
 * It's based on the number of token in the given text.
 *
 * @author Ulflander <xlaumonier@gmail.com>
 * @since 2/23/14
 */
public final class TextLength {

    /**
     * Private constructor.
     */
    private TextLength() {

    }

    /**
     * Maximum number of tokens to consider text as tiny.
     */
    public static final int MAX_TOKEN_TINY_TEXT = 15;

    /**
     * Maximum number of tokens to consider text as short.
     */
    public static final int MAX_TOKEN_SHORT_TEXT = 50;

    /**
     * Maximum number of tokens to consider text as medium.
     */
    public static final int MAX_TOKEN_MEDIUM_TEXT = 200;

    /**
     * Long text.
     */
    public static final int LONG = 4;

    /**
     * Medium text.
     */
    public static final int MEDIUM = 3;

    /**
     * Short text.
     */
    public static final int SHORT = 2;

    /**
     * Tiny text.
     */
    public static final int TINY = 1;

    /**
     * Get length for given number of tokens.
     *
     * @param n Number of tokens
     * @return Length indicator
     */
    public static int getLength(final int n) {

        if (n < MAX_TOKEN_TINY_TEXT) {
            return TextLength.TINY;
        } else if (n < MAX_TOKEN_SHORT_TEXT) {
            return TextLength.SHORT;
        } else if (n < MAX_TOKEN_MEDIUM_TEXT) {
            return TextLength.MEDIUM;
        }

        return TextLength.LONG;

    }
}
