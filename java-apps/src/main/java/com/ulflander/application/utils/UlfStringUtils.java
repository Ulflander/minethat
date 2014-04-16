package com.ulflander.application.utils;

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
}
