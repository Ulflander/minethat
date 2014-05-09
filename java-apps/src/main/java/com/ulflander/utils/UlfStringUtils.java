package com.ulflander.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * A holder for string utility functions.
 *
 * @author Ulflander <xlaumonier@gmail.com>
 * @since 2/21/14
 */
public final class UlfStringUtils {

    /**
     * Logger.
     */
    private static final Logger LOGGER =
        LogManager.getLogger(UlfStringUtils.class);

    /**
     * Private constructor.
     */
    private UlfStringUtils() {
        //not called
    }

    /**
     * Generate a string from an InputStream.
     *
     * @param is Stream to generate string from
     * @return Generated string
     */
    public static String getStringFromInputStream(final InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        String line;

        try {
            br = new BufferedReader(
                new InputStreamReader(is, Charset.forName("UTF-8")));

            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            LOGGER.error("Unable to read string", e);

        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    LOGGER.error("Unable to close "
                        + "BufferedReader", e);
                }
            }
        }

        return sb.toString();

    }

    /**
     * Check if there is more uppercase letters than lowercased ones.
     *
     * Take in account foreign CJKV (Chinese, Japanese, Korean and Vietnamese)
     * ideographs.
     *
     * @param str String to test
     * @return True of string is mostly uppercase, false otherwise
     */
    public static boolean isMostlyUppercase(final String str) {
        int up = 0,
            low = 0,
            extra = 0,
            i,
            l = str.length();

        for (i = 0; i < l; i++) {
            if (Character.isIdeographic(str.charAt(i))) {
                extra++;
            } else if (Character.isUpperCase(str.charAt(i))) {
                up++;
            } else if (Character.isLowerCase(str.charAt(i))) {
                low++;
            } else {
                extra++;
            }
        }

        // For CJKV (Chinese, Japanese, Korean and Vietnamese) ideographs
        // or strings contained mostly symbols that are not letters,
        // always return false
        if (extra > up + low) {
            return false;
        }


        return up > low;
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
