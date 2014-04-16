package com.ulflander.application.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

/**
 * @author Ulflander <xlaumonier@gmail.com>
 * @since 2/28/14
 */
public final class UlfHashUtils {

    /**
     * Logger.
     */
    private static final Logger LOGGER =
        LogManager.getLogger(UlfHashUtils.class);

    /**
     * Private constructor.
     */
    private UlfHashUtils() {

    }

    /**
     * Hash a string using SHA-1 algorythm.
     *
     * @param string String to hash
     * @return Hash
     */
    public static String sha1(final String string) {
        try {
            return sha1(string.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("Check up your string: " + string, e);
            return null;
        }
    }

    /**
     * Hash a string using SHA-256 algorythm.
     *
     * @param string String to hash
     * @return Hash
     */
    public static String sha256(final String string) {
        try {
            return sha256(string.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("Check up your string: " + string, e);
            return null;
        }
    }

    /**
     * Hash a byte array using SHA-1 algorythm.
     *
     * @param bytes Byte array to hash
     * @return Hash
     */
    public static String sha1(final byte[] bytes) {
        return digest(bytes, "SHA-1");
    }


    /**
     * Hash a byte array using SHA1 algorythm.
     *
     * @param bytes Byte array to hash
     * @return Hash
     */
    public static String sha256(final byte[] bytes) {
        return digest(bytes, "SHA-256");
    }


    /**
     * Hash a byte array using given algorythm.
     *
     * @param bytes Byte array to hash
     * @param alg Algorythm name to use.
     * @see java.security.MessageDigest
     * @return Hash
     */
    public static String digest(final byte[] bytes, final String alg) {

        try {
            MessageDigest crypt = MessageDigest.getInstance(alg);
            crypt.reset();
            crypt.update(bytes);
            return byteToHex(crypt.digest());
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("Algorythm " + alg + " does not exist", e);
            return null;
        }
    }

    /**
     * Convert a byte array to hex string.
     *
     * @param hash Byte array
     * @return Hash
     */
    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

}
