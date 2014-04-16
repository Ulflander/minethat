package com.ulflander.mining.processors;

/**
 * Controls depth of extraction.
 *
 * @author Ulflander <xlaumonier@gmail.com>
 * @since 2/21/14
 */
public enum ProcessorDepthControl {

    /**
     * Extract document only at document level.
     */
    DOCUMENT,

    /**
     * Extract document until chapter level.
     */
    CHAPTER,

    /**
     * Extract document until paragraph level.
     */
    PARAGRAPH,

    /**
     * Extract document until sentence level.
     */
    SENTENCE,

    /**
     * Extract document until token level.
     */
    TOKEN



}
