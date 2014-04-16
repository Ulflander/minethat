package com.ulflander.mining.processors.extract;

import com.ulflander.mining.Patterns;
import com.ulflander.application.model.Chapter;
import com.ulflander.application.model.Document;
import com.ulflander.application.model.Paragraph;
import com.ulflander.application.model.Sentence;
import com.ulflander.application.model.Token;
import com.ulflander.mining.processors.Processor;
import com.ulflander.mining.processors.ProcessorDepthControl;
import com.ulflander.mining.processors.Requires;

import java.text.BreakIterator;
import java.util.Scanner;

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
        int total = toChapters(doc);

        // Once we 'll get number of words overall to define length of document
        doc.setTotalToken(total);
    }

    /**
     * Split a document to chapters.
     *
     * FOR NOW NOP SPLIT OCCURS, ANY DOCUMENT HAS ONLY ONE CHAPTER
     *
     *
     * @param doc Document to split
     * @return Number of tokens found in document
     */
    public final int toChapters(final Document doc) {
        if (doc.getRaw().equals("")) {
            return 0;
        }
        // TODO : split to chapter
        Chapter chapter = new Chapter(doc.getRaw());
        int total = toParagraphs(doc, chapter);
        chapter.setTotalToken(total);
        return total;
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
     * @return Number of tokens found in chapter
     */
    public final int toParagraphs(final Document doc,
                                  final Chapter chapter) {
        if (chapter.getRaw().equals("")) {
            return 0;
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

            paragraph.setTotalToken(toSentences(chapter, paragraph));
            total += paragraph.getTotalToken();
        }

        if (chapter.getParagraphsSize() > 0
            && !doc.getChapters().contains(chapter)) {
            doc.appendChapter(chapter);
        }

        return total;
    }

    /**
     * Split a paragraph to sentences and append paragraph
     * to chapter if there are some sentences.
     * <p/>
     * Uses java.text.BreakIterator
     *
     * @param chapter Chapter being splitted
     * @param paragraph Paragraph to split
     * @return Number of tokens found in paragraph
     */
    public final int toSentences(final Chapter chapter,
                                 final Paragraph paragraph) {
        if (paragraph.getRaw().equals("")) {
            return 0;
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
            sentence.setTotalToken(toTokens(paragraph, sentence));
            total += sentence.getTotalToken();
        }

        if (paragraph.getSentencesSize() > 0
            && !chapter.getParagraphs().contains(paragraph)) {
            chapter.appendParagraph(paragraph);
        }

        return total;
    }

    /**
     * Split a sentence to tokens and append sentence to
     * paragraph if there are some tokens.
     * <p/>
     * Uses java.util.Scanner tokenization
     *
     * @param paragraph Paragraph being splitted
     * @param sentence Sentence to split
     * @return Number of tokens found in sentence
     */
    public final int toTokens(final Paragraph paragraph,
                              final Sentence sentence) {
        if (sentence.getRaw().equals("")) {
            return 0;
        }

        Scanner tokenize = new Scanner(sentence.getRaw());
        Token prev = null;
        int total = 0;

        while (tokenize.hasNext()) {
            Token t = new Token(tokenize.next());
            sentence.appendToken(t);

            if (prev != null) {
                prev.setNext(t);
                t.setPrevious(prev);
            }

            prev = t;
            total += 1;
        }

        if (sentence.getTokensSize() > 0
            && !paragraph.getSentences().contains(sentence)) {
            paragraph.appendSentence(sentence);
        }

        return total;
    }
}
