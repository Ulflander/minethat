package com.ulflander.mining.processors.extract;

import com.ulflander.app.model.Chapter;
import com.ulflander.app.model.Document;
import com.ulflander.app.model.Paragraph;
import com.ulflander.app.model.Sentence;
import com.ulflander.mining.Patterns;
import com.ulflander.mining.processors.Processor;
import com.ulflander.mining.processors.ProcessorDepthControl;
import com.ulflander.mining.processors.Requires;

import java.text.BreakIterator;

/**
 * Processor that split a document into chapters, and paragraphs.
 *
 * @author Ulflander <xlaumonier@gmail.com>
 * @since 2/21/14
 */
@Requires(processors = {
    "extract.DocumentCleaner"
})
public class DocumentSplitter extends Processor {

    /**
     * Three.
     */
    private static final int THREE = 3;

    /**
     * Four.
     */
    private static final int FOUR = 4;

    /**
     * Initialize the processor.
     */
    @Override
    public final void init() {
        setDepthControl(ProcessorDepthControl.DOCUMENT);
        setInitialized(true);
    }

    /**
     * Provides a description of this Processor.
     *
     * @return Description of this processor
     */
    @Override
    public final String describe() {
        return "Split a document raw string to chapters, "
            + "paragraphs, sentences and tokens";
    }

    /**
     * Run processor on a Document.
     *
     * @param doc Document to run processor on
     */
    @Override
    public final void extractDocument(final Document doc) {
        toChapters(doc);
    }

    /**
     * Split a document to chapters.
     *
     * FOR NOW NOP SPLIT OCCURS, ANY DOCUMENT HAS ONLY ONE CHAPTER
     *
     *
     * @param doc Document to split
     */
    public final void toChapters(final Document doc) {
        if (doc.getSurface().equals("")) {
            return;
        }
        // TODO : split to chapter
        Chapter chapter = new Chapter(doc.getSurface());
        chapter.setStartIndex(0);
        chapter.setEndIndex(doc.getSurface().length() - 1);
        toParagraphs(doc, chapter);
    }

    /**
     * Split a chapter to paragraph and append chapter
     * to document if there are some paragraphs.
     * <p/>
     * Uses the following pattern to split chapter string:
     * "(?<=(\r\n|\r|\n))([ \t]*$)+"
     *
     * @param doc Document being splitted
     * @param chapter Chapter to split
     */
    public final void toParagraphs(final Document doc,
                                  final Chapter chapter) {
        if (chapter.getSurface().equals("")) {
            return;
        }

        String[] paragraphs =
            Patterns.CHAPTER_TO_PARAGRAPH.split(chapter.getSurface());
        Paragraph prev = null;
        int index = 0;

        for (String s : paragraphs) {
            if (s.equals("")) {
                continue;
            }


            Paragraph paragraph = new Paragraph(s);
            paragraph.setStartIndex(index);
            index = index + s.length();
            paragraph.setEndIndex(index);

            if (prev != null) {
                prev.setNext(paragraph);
                paragraph.setPrevious(prev);
            }
            prev = paragraph;

            toSentences(chapter, paragraph);
        }

        if (chapter.getParagraphsSize() > 0
            && !doc.getChapters().contains(chapter)) {
            doc.appendChapter(chapter);
        }

        return;
    }

    /**
     * Split a paragraph to sentences and append paragraph
     * to chapter if there are some sentences.
     *
     * Uses java.text.BreakIterator
     *
     * @param chapter Chapter being splitted
     * @param paragraph Paragraph to split
     */
    public final void toSentences(final Chapter chapter,
                                 final Paragraph paragraph) {
        if (paragraph.getSurface().equals("")) {
            return;
        }


        String raw = paragraph.getSurface();
        BreakIterator bi = BreakIterator.getSentenceInstance();

        /*
            Paragraph shouldn't have any new lines.
            We replace new lines by "."
         */
        raw = raw.replaceAll("\\n", ". ");

        bi.setText(raw);
        int index = 0;
        int curr;
        Sentence prev = null;

        while (bi.next() != BreakIterator.DONE) {
            curr = bi.current();

            /*
                Check special use case where we got a unique
                letter before the breack iterator. It's very likely
                a kind of reduction like in "John E. Adams".
             */
            String s = raw.substring(curr - FOUR, curr - THREE);
            if ((s.equals(" ") || s.equals(".")) && curr < raw.length()) {
                continue;
            }

            /*
                Create the sentence
             */
            Sentence sentence =
                new Sentence(raw.substring(index, curr));

            if (sentence.getSurface().equals("")) {
                continue;
            }

            /*
                And set connections with next/previous.
             */
            if (prev != null) {
                prev.setNext(sentence);
                sentence.setPrevious(prev);
            }

            sentence.setStartIndex(index);
            sentence.setEndIndex(curr);


            prev = sentence;
            index = curr;

            if (!paragraph.getSentences().contains(sentence)) {
                paragraph.appendSentence(sentence);
            }
        }

        if (paragraph.getSentencesSize() > 0
            && !chapter.getParagraphs().contains(paragraph)) {
            chapter.appendParagraph(paragraph);
        }
    }

}
