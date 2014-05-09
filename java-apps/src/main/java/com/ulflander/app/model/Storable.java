package com.ulflander.app.model;

/**
 * Models that get dedicated mongo table must implement this interface.
 *
 * Created by Ulflander on 4/9/14.
 */
public interface Storable {

    /**
     * Set storage id.
     *
     * @param i Storage id
     */
    void setId(String i);

    /**
     * Get storage id.
     *
     * @return Storage id
     */
    String getId();

}
