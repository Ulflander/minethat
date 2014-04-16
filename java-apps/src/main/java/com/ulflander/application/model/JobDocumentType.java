package com.ulflander.application.model;

/**
 * Job document type - defines what value of JobDocument is: a file, an URL,
 * a raw string...
 *
 * @author Ulflander <xlaumonier@gmail.com>
 * @since 2/27/14
 */
public enum JobDocumentType {

    /**
     * JobDocument value is not known (will trigger an exception in extraction).
     */
    UNKNOWN("unknown"),

    /**
     * JobDocument value is an URL.
     */
    URL("url"),

    /**
     * JobDocument value is a path to a file.
     */
    FILE("file"),

    /**
     * JobDocument value is an ID of a Document in database.
     */
    DOCUMENT("doc"),

    /**
     * JobDocument value is a raw text.
     */
    STRING("string"),

    /**
     * JobDocument value is an HTML string.
     */
    HTML_STRING("html_string");


    /**
     * Tag.
     */
    private final String type;

    /**
     * Initialize job document type.
     *
     * @param t Tag.
     */
    private JobDocumentType(final String t) {
        this.type = t;
    }

    /**
     * Returns the encoding for this JobDocumentType.
     *
     * @return A string representing the kind of job target
     */
    public String toString() {
        return getType();
    }

    /**
     * Get type.
     *
     * @return Tag as string.
     */
    protected String getType() {
        return this.type;
    }

    /**
     * Get JobDocumentType value given its string representation.
     *
     * @param value String representation of a part-of-speech (".", "MD"...)
     * @return PennPOSTag representation
     */
    public static JobDocumentType get(final String value) {
        if (value == null) {
            return JobDocumentType.UNKNOWN;
        }

        String val = value.toUpperCase();

        for (JobDocumentType v : values()) {
            if (val.equals(v.getType())) {
                return v;
            }
        }

        throw new IllegalArgumentException("Unknown document type: '"
            + value + "'.");
    }

}
