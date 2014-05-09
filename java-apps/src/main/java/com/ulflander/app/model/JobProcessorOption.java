package com.ulflander.app.model;

import java.io.Serializable;

/**
 * Options for a processor.
 *
 * @since 2/24/14
 * @author Ulflander <xlaumonier@gmail.com>
 */
public class JobProcessorOption implements Serializable {

    /**
     * Option key.
     */
    private String key;

    /**
     * Option value.
     */
    private String value;

    /**
     * Get option key.
     *
     * @return Option key
     */
    public final String getKey() {
        return key;
    }

    /**
     * Set option key.
     *
     * @param k Option key
     */
    public final void setKey(final String k) {
        this.key = k;
    }

    /**
     * Get value.
     *
     * @return Value
     */
    public final String getValue() {
        return value;
    }

    /**
     * Set value.
     *
     * @param v Value
     */
    public final void setValue(final String v) {
        this.value = v;
    }


}
