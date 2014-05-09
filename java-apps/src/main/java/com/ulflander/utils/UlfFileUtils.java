package com.ulflander.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.File;

/**
 * Utility functions for files.
 *
 * @author Ulflander <xlaumonier@gmail.com>
 * @since 2/21/14
 */
public final class UlfFileUtils {

    /**
     * Logger.
     */
    private static final Logger LOGGER =
        LogManager.getLogger(UlfFileUtils.class);

    /**
     * Buffer size.
     */
    private static final int SIZE_1024 = 1024;

    /**
     * Private constructor.
     */
    private UlfFileUtils() {
        // Not called
    }

    /**
     * Read a file in filesystem given its path.
     *
     * @param filePath Path to the file
     * @return File content as a string
     */
    public static String read(final String filePath) {
        return read(new File(filePath));
    }

    /**
     * Read a file in filesystem given a File object.
     *
     * @param file File object
     * @return File content as a string
     */
    public static String read(final File file) {

        if (file == null) {
            throw new NullPointerException("UlfFileUtils.read can't handle "
                    + "null File references.");
        }

        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        String str = "";

        try {
            br = new BufferedReader(
                new InputStreamReader(
                    new FileInputStream(file), "UTF8"));
            char[] buf = new char[SIZE_1024];
            int numRead;

            while ((numRead = br.read(buf)) != -1) {
                String readData = String.valueOf(buf, 0, numRead);
                sb.append(readData);
                buf = new char[SIZE_1024];
            }

            str = sb.toString();
        } catch (IOException e) {
            LOGGER.error("Reading file "
                + file.toString() + " raised an error: ", e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    LOGGER.error("Closing BufferedReader for "
                        + "file " + file.toString() + " raised an error: ", e);
                }
            }
        }

        return str;
    }

    /**
     * Write a file in filesystem.
     *
     * @param filePath Path to the file
     * @param content  Content of the file
     */
    public static void write(final String filePath, final String content) {
        BufferedWriter out = null;
        try {
            // Create file
            out = new BufferedWriter(
                new OutputStreamWriter(
                    new FileOutputStream(filePath), "UTF-8"));
            out.write(content);

            //Close the output stream
        } catch (Exception e) {
            LOGGER.error("Reading file "
                + filePath + " raised an error: ", e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    LOGGER.error("Closing BufferedWriter for "
                        + "file " + filePath + " raised an error: ", e);
                }
            }
        }
    }


    /**
     * Check if a file exists.
     *
     * @param filePath Path to the file
     * @return True if file exists, false otherwise
     */
    public static boolean exists(final String filePath) {
        return new File(filePath).exists();
    }

    /**
     * Delete a file given its path.
     *
     * @param filePath Path to the file
     * @return True if file has been deleted, false otherwise
     */
    public static Boolean delete(final String filePath) {
        File f = new File(filePath);
        if (!f.exists()) {
            return false;
        }

        return delete(f);

    }

    /**
     * Delete a file given its resource.
     *
     * @param f File resource
     * @return True if file has been deleted, false otherwise
     */
    public static Boolean delete(final File f) {
        return f.delete();
    }

}
