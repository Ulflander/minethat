package com.ulflander.app.model;

/**
 * Describes a Job target: mining or training.
 *
 * Created by Ulflander on 4/15/14.
 */
public enum JobTarget {


    /**
     * Training target.
     */
    MINE("mine"),

    /**
     * Training target.
     */
    TRAIN("train");

    /**
     * Tag.
     */
    private final String target;

    /**
     * Initialize job target.
     *
     * @param t Target.
     */
    private JobTarget(final String t) {
        this.target = t.toUpperCase();
    }

    /**
     * Returns the encoding for this job target.
     *
     * @return A string representing the job target.
     */
    public String toString() {
        return getTarget();
    }

    /**
     * Get job target.
     *
     * @return Target as string.
     */
    protected String getTarget() {
        return this.target;
    }

    /**
     * Get JobDocumentType value given its string representation.
     *
     * @param value Supposed target
     * @return Real target
     */
    public static JobTarget get(final String value) {
        if (value == null) {
            return JobTarget.MINE;
        }

        String val = value.toUpperCase();

        for (JobTarget v : values()) {
            if (val.equals(v.getTarget())) {
                return v;
            }
        }

        throw new IllegalArgumentException("Unknown job target: '"
                + value + "'.");
    }

}
