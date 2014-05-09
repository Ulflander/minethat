package com.ulflander.app.model;

import java.util.ArrayList;

/**
 * Model of sentence. A sentence is contained into paragraphs, and contains
 * some tokens.
 *
 * @since 2/24/14
 * @author Ulflander <xlaumonier@gmail.com>
 */
public class Sentence extends Text {

    /**
     * Previous sentence in paragraph.
     */
    private Sentence previous;

    /**
     * Next sentence in paragraph.
     */
    private Sentence next;

    /**
     * POS Tagging.
     */
    private String rawPartOfSpeech;

    /**
     * List of tokens contained in the sentence.
     */
    private ArrayList<Token> tokens = new ArrayList<Token>();

    /**
     * Create a new sentence with an empty content.
     */
    public Sentence() {
        super("");
    }

    /**
     * Create a new sentence with given content.
     *
     * @param raw Initial content for sentence
     */
    public Sentence(final String raw) {
        super(raw);
    }

    /**
     * Get previous sentence in paragraph.
     *
     * @return Previous sentence if exist, null otherwise
     */
    public final Sentence getPrevious() {
        return previous;
    }

    /**
     * Set previous sentence in paragraph.
     *
     * @param p Previous sentence in paragraph
     */
    public final void setPrevious(final Sentence p) {
        this.previous = p;
    }

    /**
     * Get next sentence in paragraph.
     *
     * @return Next sentence if exist, null otherwise
     */
    public final Sentence getNext() {
        return next;
    }


    /**
     * Next previous sentence in paragraph.
     *
     * @param n Next sentence in paragraph
     */
    public final void setNext(final Sentence n) {
        this.next = n;
    }

    /**
     * Get the list of tokens contained into the sentence.
     *
     * @return Tokens contained in sentence.
     */
    public final ArrayList<Token> getTokens() {
        return tokens;
    }

    /**
     * Append a token to the sentence.
     *
     * @param token Token to append to sentence
     */
    public final void appendToken(final Token token) {
        tokens.add(token);
    }

    /**
     * Append a string to the sentence - will be converted to token.
     *
     * @param str String to append to sentence.
     */
    public final void appendToken(final String str) {
        appendToken(new Token(str));
    }

    /**
     * Get token at given index.
     *
     * @param pos Index of token
     * @return Token if exist, null otherwise
     */
    public final Token getTokenAt(final int pos) {
        if (tokens != null && tokens.size() > pos) {
            return tokens.get(pos);
        }
        return null;
    }

    /**
     * Generates a clean sentence string from clean tokens.
     *
     * @return Clean sentence
     */
    public final String generateClean() {
        String result = "";
        for (Token t: tokens) {
            result += " " + t.getClean();
        }
        return result.substring(1);
    }

    /**
     * Get number of tokens in sentence.
     *
     * @return Number of tokens in sentence
     */
    public final int getTokensSize() {
        if (tokens == null) {
            return 0;
        }
        return tokens.size();
    }

    /**
     * Get raw POS tagging result.
     *
     * @return Raw POS tagging result
     */
    public final String getRawPartOfSpeech() {
        return rawPartOfSpeech;
    }

    /**
     * Set raw POS tagging result.
     *
     * @param pos Raw POS tagging result
     */
    public final void setRawPartOfSpeech(final String pos) {
        this.rawPartOfSpeech = pos;
    }


}
