package com.ulflander.app.model;

/**
 * Status of a document.
 *
 * Created by Ulflander on 5/30/14.
 */
public enum DocumentStatus {

    /**
     * Document has no status.
     */
    VOID,

    /**
     * Document has been extracted.
     */
    EXTRACTED,

    /**
     * Document has been mined.
     */
    MINED,

    /**
     * Failed document mining.
     */
    FAILED;

}
