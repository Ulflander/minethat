package com.ulflander.mining;

import org.apache.commons.lang3.StringUtils;

/**
 * Performs some cleaning on raw texts from file URL...
 *
 * @author Ulflander <xlaumonier@gmail.com>
 * @since 2/27/14
 */
public final class TextCleaner {

    /**
     * Private constructor.
     */
    private TextCleaner() {

    }

    /**
     * Trim each line of a string.
     *
     * @param str Raw HTML page
     * @return Return a clean HTML string from given string
     */
    public static String trimLines(final String str) {
        String[] clean = str.split("\n");

        for (int i = 0, l = clean.length; i < l; i++) {
            clean[i] = clean[i].trim();
        }
        return StringUtils.join(clean, "\n");
    }

    /**
     * Clean multiple spaces to one space.
     *
     * @param str Text to cleanup from multiple spaces
     * @return Cleaned string
     */
    public static String cleanSpaces(final String str) {
        return str.replaceAll(" +", " ");
    }
}
