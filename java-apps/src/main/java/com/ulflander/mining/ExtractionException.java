package com.ulflander.mining;

/**
 * Triggered on extraction process.
 *
 * @author Ulflander <xlaumonier@gmail.com>
 * @since 2/27/14
 */
public class ExtractionException extends Exception {

    /**
     * Create a new ExtractionException.
     *
     * @param msg Exception message
     */
    public ExtractionException(final String msg) {
        super(msg);
    }
}
