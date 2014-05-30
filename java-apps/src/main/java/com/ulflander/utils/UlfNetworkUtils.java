package com.ulflander.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

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
        String out = "";
        String line = "";
        URLConnection urlc;
        try {
            urlc = u.openConnection();
        } catch (IOException e) {
            LOGGER.error("URL " + u.toString()
                    + " didn't accept opening connection", e);
            return "";
        }

        /*
            Google Chrome headers
         */
        urlc.setRequestProperty("Accept", "text/html,application/xhtml+xml,"
                + "application/xml;q=0.9,image/webp,*/*;q=0.8");
        urlc.setRequestProperty("Host", u.getHost());
        urlc.setRequestProperty("Accept-Language", "en-US,en;q=0.8,fr;q=0.6");
        urlc.setRequestProperty("User-Agent", "Mozilla/5.0 "
                + "(Macintosh; Intel Mac OS X 10_9_3) AppleWebKit/537.36 "
                + "(KHTML, like Gecko) Chrome/35.0.1916.114 Safari/537.36");
        urlc.setRequestProperty("Cache-Control", "no-cache");
        urlc.setRequestProperty("Pragma", "no-cache");

        BufferedReader in = null;

        try {
            in = new BufferedReader(new InputStreamReader(
                    urlc.getInputStream()));

            line = in.readLine();
            while (line != null) {
                out += line + "\n";
                line = in.readLine();
            }
        } catch (IOException e) {
            LOGGER.error("URL " + u.toString()
                    + " unable to be read", e);
            return "";
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    LOGGER.error("URL " + u.toString() + " unable to close"
                            + " buffered readed.");
                }
            }
        }

        return out;
    }


    /*
     * Get content as string from URL given headers.
     *
     * @param u URL of webpage to retrieve
     * @param h Hashmap of key/value headers
     * @return Webpage as a string
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
     */
}
