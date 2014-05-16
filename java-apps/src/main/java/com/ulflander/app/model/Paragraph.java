package com.ulflander.app.model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * A paragraph contains sentences and is contained into chapters.
 *
 * @since 2/24/14
 * @author Ulflander <xlaumonier@gmail.com>
 */
public class Paragraph extends Text {

    /**
     * Chapter.
     */
    private Chapter chapter;

    /**
     * Previous paragraph.
     */
    private Paragraph previous;

    /**
     * Next paragraph.
     */
    private Paragraph next;

    /**
     * Sentences in this paragraph.
     */
    @Expose
    private ArrayList<Sentence> sentences =
        new ArrayList<Sentence>();

    /**
     * Create a new paragraph with an empty content.
     */
    public Paragraph() {
        super("");
    }

    /**
     * Create a paragraph with given content.
     *
     * @param raw Initial content of paragraph
     */
    public Paragraph(final String raw) {
        super(raw);
    }

    /**
     * Get previous paragraph in chapter.
     *
     * @return Previous paragraph in chapter if exist, null otherwise
     */
    public final Paragraph getPrevious() {
        return previous;
    }

    /**
     * Set previous paragraph in chapter.
     *
     * @param p Previous paragraph in chapter
     */
    public final void setPrevious(final Paragraph p) {
        this.previous = p;
    }


    /**
     * Get next paragraph in chapter.
     *
     * @return Next paragraph in chapter if exist, null otherwise
     */
    public final Paragraph getNext() {
        return next;
    }

    /**
     * Set next paragraph in chapter.
     *
     * @param n Next paragraph in chapter
     */
    public final void setNext(final Paragraph n) {
        this.next = n;
    }

    /**
     * Get list of sentences of this paragraph.
     *
     * @return Sentences contained in the paragraph
     */
    public final ArrayList<Sentence> getSentences() {
        return sentences;
    }

    /**
     * Append a sentence at this end of the paragraph.
     *
     * @param s Sentence to append to paragraph
     */
    public final void appendSentence(final Sentence s) {
        if (s == null || s.getSurface().equals("")) {
            return;
        }

        sentences.add(s);
        s.setParagraph(this);
    }

    /**
     * Get number of sentences in paragraph.
     *
     * @return Number of sentences contained in the paragraph
     */
    public final int getSentencesSize() {
        if (sentences == null) {
            return 0;
        }
        return sentences.size();
    }

    /**
     * Get sentence at given index.
     *
     * @param pos Index of sentence in paragraph
     * @return Sentence if exist at index, null otherwise
     */
    public final Sentence getSentenceAt(final int pos) {
        if (sentences != null && sentences.size() > pos) {
            return sentences.get(pos);
        }
        return null;
    }

    /**
     * Get token at given sentence index and token index - WILL throw an
     * exception if sentence does not exist.
     *
     * @param sentencePos Index of sentence in paragraph
     * @param pos Index of token in sentence
     * @return Token if exist, null otherwise, throws error if sentence
     * does not exist
     */
    public final Token getTokenAt(final int sentencePos, final int pos) {
        return getSentenceAt(sentencePos).getTokenAt(pos);
    }

    /**
     * Get chapter.
     *
     * @return Chapter
     */
    public final Chapter getChapter() {
        return chapter;
    }

    /**
     * Set chapter.
     *
     * @param c Chapter
     */
    public final void setChapter(final Chapter c) {
        this.chapter = c;
    }


}
