package com.ulflander.mining.processors.extract;

import com.ulflander.app.model.Chapter;
import com.ulflander.app.model.Document;
import com.ulflander.app.model.Heading;
import com.ulflander.app.model.HeadingLevel;
import com.ulflander.app.model.KeywordList;
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
     * Mininmum size for a sentence to be considered as is.
     */
    public static final int MIN_SENTENCE_SIZE = 8;

    /**
     * Minimum length of an uppercased word at the end of a sentence
     * (including terminator).
     */
    public static final int LAST_WORD_UPPERCASED_MIN_LENGTH = 5;



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

        Paragraph prev = null;
        int index = 0;

        // If doc has some meta like title and description, we add these
        if (doc.hasProperty("meta", "doc_title")) {
            String surface = (String) doc.getProperty("meta", "doc_title");
            Heading h = new Heading();
            h.setLevel(HeadingLevel.IMPORTANT);
            h.setSurface(surface);

            chapter.appendParagraph(h);
            prev = h;
            index += surface.length();
            toSentences(chapter, h);
        }

        if (doc.hasProperty("meta", "doc_description")) {
            String surface =
                    (String) doc.getProperty("meta", "doc_description");
            Paragraph p = new Paragraph();
            p.setSurface(surface);

            if (prev != null) {
                prev.setNext(p);
                p.setPrevious(prev);
            }
            prev = p;
            chapter.appendParagraph(p);
            index += surface.length();
            toSentences(chapter, p);
        }

        if (doc.hasProperty("meta", "doc_keywords")) {
            String surface =
                    (String) doc.getProperty("meta", "doc_keywords");
            KeywordList p = new KeywordList();
            p.setSurface(surface);

            if (prev != null) {
                prev.setNext(p);
                p.setPrevious(prev);
            }
            prev = p;
            chapter.appendParagraph(p);
            index += surface.length();
            toSentences(chapter, p);
        }



        String[] paragraphs =
                Patterns.CHAPTER_TO_PARAGRAPH.split(chapter.getSurface());

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
            if (curr > FOUR) {
                String s = raw.substring(curr - FOUR, curr - THREE);
                if ((s.equals(" ") || s.equals(".")) && curr < raw.length()) {
                    continue;
                }
            }


            String surface = raw.substring(index, curr);

            /*
                If sentence is less than four chars, it's
                likely not a sentence.
             */
            if (surface.length() < MIN_SENTENCE_SIZE) {
                continue;
            }

            /*
                If last word is less than 4 chars and starts with an uppercase,
                it's likely a descriptor.
             */
            String[] surfaceSplitted = surface.split(" ");
            String last = surfaceSplitted[surfaceSplitted.length - 1];

            if (last.length() < LAST_WORD_UPPERCASED_MIN_LENGTH
                && last.matches("\\p{javaUpperCase}\\p{javaLowerCase}+\\.")) {
                continue;
            }

            /*
                Create the sentence
             */
            Sentence sentence =
                new Sentence(surface);

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
