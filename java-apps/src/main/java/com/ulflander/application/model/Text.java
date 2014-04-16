package com.ulflander.application.model;

import com.google.gson.annotations.Expose;
import com.ulflander.mining.Patterns;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Text is the base class for any text model: document, paragraph, token...
 *
 * @author Ulflander <xlaumonier@gmail.com>
 * @since 2/23/14
 */
public abstract class Text {

    /**
     * Logger.
     */
    private static final Logger LOGGER =
        LogManager.getLogger(Text.class);

    /**
     * Raw string of the text.
     */
    @Expose
    private String raw;

    /**
     * Is that text uppercased.
     */

    private Boolean uppercased = false;

    /**
     * Is that test lowercased.
     */
    private Boolean lowercased = false;

    /**
     * Is that text capitalized.
     */
    private Boolean capitalized = false;

    /**
     * Language of this text.
     */
    private Language language = Language.UNKNOWN;

    /**
     * Length of this text.
     *
     * @see com.ulflander.application.model.TextLength
     */
    private int length;

    /**
     * Total number of tokens in this text.
     */
    private int totalToken;

    /**
     * Instanciate a new text with an empty string.
     */
    public Text() {
        this("");
    }

    /**
     * Instanciate a new text with given string.
     *
     * @param r Raw string for the new text
     */
    public Text(final String r) {
        this.setRaw(r);
    }


    @Override
    public final String toString() {
        return "Text {"
            + "length=" + length
            + ", raw='" + raw + "'"
            + '}';
    }

    /**
     * Get text language value.
     *
     * @return Text language
     */
    public final Language getLanguage() {
        return language;
    }

    /**
     * Set text language value.
     *
     * @param o Text language
     */
    public final void setLanguage(final Language o) {
        this.language = o;
    }

    /**
     * Get raw string of this text as initially provided.
     *
     * @return Raw string of the text
     */
    public final String getRaw() {
        return raw;
    }

    /**
     * Set raw string for this text.
     *
     * @param r Raw string of the text
     */
    public final void setRaw(final String r) {

        String s = r;

        if (s == null) {
            s = "";
        }

        this.raw = StringUtils.trim(s);
        this.resetProperties();
        this.whileSetRaw();
    }

    /**
     * Method called by raw property setter, and overridable in subclasses.
     */
    public void whileSetRaw() {

    }

    /**
     * Append a new string to the text.
     *
     * @param r Raw text to append to existing raw text
     */
    public final void append(final String r) {
        this.raw += " " + StringUtils.trim(r);
        this.resetProperties();
    }

    /**
     * Reset lowercase, uppercase... statuses when raw string is updated.
     */
    private void resetProperties() {
        this.lowercased = raw.matches(Patterns.ABSTRACT_TEXT_LOWERCASE);
        this.uppercased = !this.lowercased;
        if (raw.length() > 0) {
            this.capitalized = StringUtils.isAllUpperCase(raw.substring(0, 1));
        }
    }

    /**
     * Get clean representation of this text (trimmed, lowercased).
     *
     * @return A clean string from this text raw string
     */
    public final String getClean() {
        return raw.trim().toLowerCase();
    }

    /**
     * Expose whether raw text is all uppercased.
     *
     * @return True if no lowercase letter is in raw string, false otherwise
     */
    public final Boolean isUppercased() {
        return uppercased;
    }

    /**
     * Expose whether raw text is capitalized (first letter is uppercased).
     *
     * @return True if first letter is upppercased, false otherwise.
     */
    public final Boolean isCapitalized() {
        return capitalized;
    }

    /**
     * Expose whether raw text is all lowercased.
     *
     * @return True if no uppercase letter is in raw string, false otherwise
     */
    public final Boolean isLowercased() {
        return lowercased;
    }

    /**
     * Get text length.
     *
     * @return Text length
     */
    public final int getTextLength() {
        return length;
    }

    /**
     * Set text length.
     *
     * @param l Length of text
     */
    private void setTextLength(final int l) {
        if (l < TextLength.TINY && l > TextLength.LONG) {
            LOGGER.error("Trying to set invalid text length: " + l);
            return;
        }
        this.length = l;
    }

    /**
     * Get total number of tokens in text.
     *
     * @return Number of tokens in text
     */
    public final int getTotalToken() {
        return totalToken;
    }

    /**
     * Set total number of tokens in text
     * (doing it automatically redefines text length).
     *
     * @param tt Total number of tokens in text
     */
    public final void setTotalToken(final int tt) {
        this.setTextLength(TextLength.getLength(tt));
        this.totalToken = tt;
    }

}
