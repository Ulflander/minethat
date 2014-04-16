package com.ulflander.application.model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * A chapter contains paragraphs and is contained into a document.
 *
 * @since 2/24/14
 * @author Ulflander <xlaumonier@gmail.com>
 */
public class Chapter extends Text {

    /**
     * Previous chapter in document.
     */
    private Chapter previous;

    /**
     * Next chapter in document.
     */
    private Chapter next;

    /**
     * Paragraphs contained in the chapter.
     */
    @Expose
    private ArrayList<Paragraph> paragraphs = new ArrayList<Paragraph>();

    /**
     * Create a new chapter with an empty content.
     */
    public Chapter() {
        super("");
    }

    /**
     * Create a new chapter with given content.
     *
     * @param raw Initial content of chapter
     */
    public Chapter(final String raw) {
        super(raw);
    }

    /**
     * Get previous chapter in document value.
     *
     * @return Previous chapter in document
     */
    public final Chapter getPrevious() {
        return previous;
    }

    /**
     * Set previous chapter in document value.
     *
     * @param c Previous chapter in document
     */
    public final void setPrevious(final Chapter c) {
        this.previous = c;
    }

    /**
     * Get next chapter in document value.
     *
     * @return Next chapter in document
     */
    public final Chapter getNext() {
        return next;
    }

    /**
     * Set next chapter in document value.
     *
     * @param c Next chapter in document
     */
    public final void setNext(final Chapter c) {
        this.next = c;
    }

    /**
     * Get list of paragraphs contained in the chapter.
     *
     * @return List of paragraphs of the chapter
     */
    public final ArrayList<Paragraph> getParagraphs() {
        return paragraphs;
    }

    /**
     * Get the number of paragraphs in the chapter.
     *
     * @return Number of paragraphs contained in this chapter
     */
    public final int getParagraphsSize() {
        if (paragraphs == null) {
            return 0;
        }
        return paragraphs.size();
    }

    /**
     * Append a paragraph to the chapter.
     *
     * @param p Paragraph to append to the chapter
     */
    public final void appendParagraph(final Paragraph p) {
        if (p.getRaw().equals("")) {
            return;
        }

        paragraphs.add(p);
    }

    /**
     * Get paragraph at given index.
     *
     * @param pos Index of paragraph to get
     * @return Paragraph if exist, null otherwise
     */
    public final Paragraph getParagraphAt(final int pos) {
        if (paragraphs != null && paragraphs.size() > pos) {
            return paragraphs.get(pos);
        }
        return null;
    }

    /**
     * Get sentence at given paragraph index and sentence index - WILL throw an
     * exception if paragraph does not exist.
     *
     * @param paragraphPos Index of paragraph
     * @param pos Index of sentence in paragraph
     * @return Sentence if exist, null otherwise, throws error if paragraph
     * does not exist
     */
    public final Sentence getSentenceAt(final int paragraphPos, final int pos) {
        return getParagraphAt(paragraphPos).getSentenceAt(pos);
    }


    /**
     * Get token at given paragraph index and sentence index and token index -
     * WILL throw an exception if paragraph or sentence does not exist.
     *
     * @param paragraphPos Index of paragraph
     * @param sentencePos Index of sentence in paragraph
     * @param pos Index of token in sentence
     * @return Token if exist, null otherwise, throws error if paragraph or
     * sentence does not exist
     */
    public final Token getTokenAt(final int paragraphPos,
                                  final int sentencePos,
                                  final int pos) {
        return getParagraphAt(paragraphPos)
                .getSentenceAt(sentencePos)
                .getTokenAt(pos);
    }

}
