package com.ulflander.app.model;

/**
 * State of processing document.
 *
 * @since 2/24/14
 * @author Ulflander <xlaumonier@gmail.com>
 */
public enum DocumentStatus {

    /**
     * Document has been inited (populated).
     */
    VOID,

    /**
     * Document has been inited (populated).
     */
    INITED,

    /**
     * Document has passed loading processors (successful final status).
     */
    SUCCESSFULL,

    /**
     * A failure occured during processing.
     */
    FAILED

}
