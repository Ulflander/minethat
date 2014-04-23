package com.ulflander.mining.processors.extract;

import com.ulflander.application.model.Chapter;
import com.ulflander.application.model.Document;
import com.ulflander.application.model.Paragraph;
import com.ulflander.application.model.Sentence;
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
        if (doc.getRaw().equals("")) {
            return;
        }
        // TODO : split to chapter
        Chapter chapter = new Chapter(doc.getRaw());
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
        if (chapter.getRaw().equals("")) {
            return;
        }

        String[] paragraphs =
            Patterns.CHAPTER_TO_PARAGRAPH.split(chapter.getRaw());
        Paragraph prev = null;
        int total = 0;

        for (String s : paragraphs) {
            if (s.equals("")) {
                continue;
            }

            Paragraph paragraph = new Paragraph(s);

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
     * <p/>
     * Uses java.text.BreakIterator
     *
     * @param chapter Chapter being splitted
     * @param paragraph Paragraph to split
     */
    public final void toSentences(final Chapter chapter,
                                 final Paragraph paragraph) {
        if (paragraph.getRaw().equals("")) {
            return;
        }

        String raw = paragraph.getRaw();
        BreakIterator bi = BreakIterator.getSentenceInstance();
        bi.setText(raw);
        int index = 0;
        int total = 0;
        Sentence prev = null;

        while (bi.next() != BreakIterator.DONE) {
            Sentence sentence =
                new Sentence(raw.substring(index, bi.current()));

            if (sentence.getRaw().equals("")) {
                continue;
            }

            if (prev != null) {
                prev.setNext(sentence);
                sentence.setPrevious(prev);
            }

            prev = sentence;
            index = bi.current();

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
