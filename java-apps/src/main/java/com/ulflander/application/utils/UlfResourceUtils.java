package com.ulflander.application.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.util.Scanner;

/**
 * Provide useful functions for resources retrieval.
 *
 * @author Ulflander <xlaumonier@gmail.com>
 * @since 2/21/14
 */
public final class UlfResourceUtils {

    /**
     * Logger.
     */
    private static final Logger LOGGER =
        LogManager.getLogger(UlfResourceUtils.class);

    /**
     * Private constructor.
     */
    private UlfResourceUtils() {
        //not called
    }

    /**
     * Get a resource as String by name.
     *
     * @param filename Name of resource file
     * @return Resoucre as a string
     */
    public static String get(final String filename) {
        Scanner s = new Scanner(getStream(filename), "UTF-8")
            .useDelimiter("\\A");

        if (s.hasNext()) {
            return s.next();
        }
        LOGGER.error("Resource not found: " + filename);
        return "";
    }

    /**
     * Get a resource as InputStream by name.
     *
     * @param filename Name of resource file
     * @return Resoucre as an InputStream
     */
    public static InputStream getStream(final String filename) {
        return Thread.currentThread()
            .getContextClassLoader()
            .getResourceAsStream(filename);
    }
}
