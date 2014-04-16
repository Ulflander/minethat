package com.ulflander.application.model;

/**
 * Heading is a special paragraph that contains only a few words.
 *
 * @author Ulflander <xlaumonier@gmail.com>
 * @since 2/28/14
 */
public class Heading extends Paragraph {

    /**
     * Heading level - default is NORMAL.
     */
    private HeadingLevel level = HeadingLevel.NORMAL;

    /**
     * Get heading level.
     *
     * @return Heading level
     */
    public final HeadingLevel getLevel() {
        return level;
    }

    /**
     * Set heading level.
     *
     * @param hl Heading level
     */
    public final void setLevel(final HeadingLevel hl) {
        this.level = hl;
    }


}
