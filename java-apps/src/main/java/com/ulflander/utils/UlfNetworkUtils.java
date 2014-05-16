package com.ulflander.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

/**
 * @author Ulflander <xlaumonier@gmail.com>
 * @since 2/26/14
 */
public final class UlfNetworkUtils {

    /**
     * Logger.
     */
    private static final Logger LOGGER =
        LogManager.getLogger(UlfNetworkUtils.class);

    /**
     * Private constructor.
     */
    private UlfNetworkUtils() {

    }

    /**
     * Get content as string from URL.
     *
     * @param url URL as string of webpage to retrieve
     * @return Webpage as a string
     */
    public static String getContent(final String url) {
        URL u;

        try {
            u = new URL(url);
        } catch (MalformedURLException e) {
            LOGGER.error("URL " + url + " is malformed", e);
            return "";
        }

        return getContent(u);
    }

    /**
     * Get content as string from URL.
     *
     * @param u URL of webpage to retrieve
     * @return Webpage as a string
     */
    public static String getContent(final URL u) {
        return getContent(u, null);
    }

    /**
     * Get content as string from URL given headers.
     *
     * @param u URL of webpage to retrieve
     * @param h Hashmap of key/value headers
     * @return Webpage as a string
     */
    public static String getContent(final URL u,
                                    final HashMap<String, String> h) {
        String out = "";
        InputStream is;


        try {
            is = u.openStream();
        } catch (IOException e) {
            LOGGER.error("IOException exception occured for url "
                + u.toString(), e);
            return out;
        }

        String line;
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        try {
            while ((line = br.readLine()) != null) {
                out += line + "\n";
            }
        } catch (Exception e) {
            LOGGER.error("IOException exception occured for url "
                    + u.toString(), e);
            out = "";
        }

        try {
            is.close();
        } catch (IOException e) {
            LOGGER.error("IOException while closing stream for url "
                + u.toString(), e);
        }

        return out;
    }
}
