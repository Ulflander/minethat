package com.ulflander.app.model;

import java.util.HashMap;

/**
 * An entity.
 *
 * @author Ulflander <xlaumonier@gmail.com>
 * @since 3/1/14
 */
public class Entity {

    /**
     * RDF URL.
     */
    private String url;

    /**
     * Get entity RDF URL.
     *
     * @return Entity RDF URL
     */
    public final String getUrl() {
        return url;
    }

    /**
     * Set entity RDF URL.
     *
     * @param u Entity RDF URL
     */
    public final void setUrl(final String u) {
        this.url = u;
    }

    /**
     * Entity attributes.
     */
    private HashMap<String, String> attributes = new HashMap<String, String>();

    /**
     * Get an attribute.
     *
     * @param k Key
     * @return Value
     */
    public final String get(final String k) {
        return attributes.get(k);
    }

    /**
     * Set an attribute.
     *
     * @param k Key
     * @param v Value
     */
    public final void set(final String k, final String v) {
        attributes.put(k, v);
    }
}
