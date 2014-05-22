package com.ulflander.app.model;

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
     * Full weight.
     */
    protected static final Float FULL_WEIGHT = 1.0f;

    /**
     * Default weight.
     */
    protected static final Float DEFAULT_WEIGHT = 0.5f;

    /**
     * Weight (from token to chapter level) from 0 to 1.x.
     */
    private Float weight = DEFAULT_WEIGHT;

    /**
     * Weight total use.
     */
    private int weightTotal = 1;

    /**
     * Raw string of the text.
     */
    @Expose
    private String surface;

    /**
     * Clean string.
     */
    private String clean;

    /**
     * Size of clean string.
     */
    private int size;

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
     * @see com.ulflander.app.model.TextLength
     */
    private int length;

    /**
     * Total number of tokens in this text.
     */
    private int totalToken;

    /**
     * Start index (from parent).
     */
    private int startIndex = -1;

    /**
     * End index (from parent).
     */
    private int endIndex = -1;

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
        this.setSurface(r);
    }


    @Override
    public final String toString() {
        return "Text {"
            + "length=" + length
            + ", surface='" + surface + "'"
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
     * Get surface string of this text as initially provided.
     *
     * @return Raw string of the text
     */
    public final String getSurface() {
        return surface;
    }

    /**
     * Set surface string for this text.
     *
     * @param r Raw string of the text
     */
    public final void setSurface(final String r) {

        String s = r;

        if (s == null) {
            s = "";
        }

        this.surface = StringUtils.trim(s);
        this.resetProperties();
        this.whileSetRaw();
        this.clean = surface.toLowerCase();
        this.size = this.clean.length();
    }

    /**
     * Method called by surface property setter, and overridable in subclasses.
     */
    public void whileSetRaw() {

    }

    /**
     * Append a new string to the text.
     *
     * @param r Raw text to append to existing surface text
     */
    public final void append(final String r) {
        this.surface += " " + StringUtils.trim(r);
        this.resetProperties();
        this.clean = surface.toLowerCase();
        this.size = this.clean.length();
    }


    /**
     * Reset lowercase, uppercase... statuses when surface form is updated.
     */
    private void resetProperties() {
        this.lowercased = surface.matches(Patterns.ABSTRACT_TEXT_LOWERCASE);
        this.uppercased = !this.lowercased;
        if (surface.length() > 0) {
            this.capitalized =
                    StringUtils.isAllUpperCase(surface.substring(0, 1));
        }
    }

    /**
     * Get clean representation of this text (trimmed, lowercased).
     *
     * @return A clean string from this text surface form
     */
    public final String getClean() {
        return clean;
    }

    /**
     * Set clean representation. Note that normally the clean (trimmed,
     * lowercased) representation of text is generated automatically when
     * calling Text.setSurface(), however some processors like TokenCleaner
     * may override the clean representation for some specific languages.
     *
     * @param s Clean representation of this string
     */
    public final void setClean(final String s) {
        clean = s;
    }

    /**
     * Expose whether surface text is all uppercased.
     *
     * @return True if no lowercase letter is in surface string, false otherwise
     */
    public final Boolean isUppercased() {
        return uppercased;
    }

    /**
     * Expose whether surface text is capitalized (first letter is uppercased).
     *
     * @return True if first letter is upppercased, false otherwise.
     */
    public final Boolean isCapitalized() {
        return capitalized;
    }

    /**
     * Expose whether surface text is all lowercased.
     *
     * @return True if no uppercase letter is in surface string, false otherwise
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

    /**
     * Get size of clean text.
     *
     * @return Size of clean text
     */
    public final int size() {
        return size;
    }

    /**
     * Get weight.
     *
     * @return Weight
     */
    public final Float getWeight() {
        return weight / weightTotal;
    }

    /**
     * Set average weight between current weight and given weight.
     *
     * @param w Weight to calculate average with
     */
    public final void setWeight(final Float w) {
        this.weight += w;
        weightTotal += 1;
    }


    /**
     * Set weight.
     *
     * @param w Weight of token
     */
    public final void setAbsoluteWeight(final Float w) {
        this.weight = w;
    }

    /**
     * Get start index.
     *
     * @return Start index
     */
    public final int getStartIndex() {
        return startIndex;
    }

    /**
     * Set start index.
     *
     * @param i Start index
     */
    public final void setStartIndex(final int i) {
        this.startIndex = i;
    }

    /**
     * Get end index.
     *
     * @return End index
     */
    public final int getEndIndex() {
        return endIndex;
    }

    /**
     * Set end index.
     *
     * @param i End index
     */
    public final void setEndIndex(final int i) {
        this.endIndex = i;
    }

}
