package com.ulflander.mining.processors;


import com.ulflander.app.model.Chapter;
import com.ulflander.app.model.Document;
import com.ulflander.app.model.Paragraph;
import com.ulflander.app.model.Sentence;
import com.ulflander.app.model.Token;

/**
 * Abstract processor.
 *
 * @author Ulflander <xlaumonier@gmail.com>
 * @since 2/21/14
 */
@Requires(processors = {

})
public abstract class Processor {


    /**
     * Depth control value (default is TOKEN).
     *
     * @see com.ulflander.mining.processors.ProcessorDepthControl
     */
    private ProcessorDepthControl depthControl = ProcessorDepthControl.TOKEN;

    /**
     * Get processor depth control value.
     *
     * @return Depth control value
     */
    public final ProcessorDepthControl getDepthControl() {
        return depthControl;
    }

    /**
     * Set processor depth control.
     *
     * Depth control let you decide if a processor run over any token object
     * of the document, or at another level like sentence or document. Default
     * is set to token.
     *
     * @param dc Max depth
     */
    public final void setDepthControl(final ProcessorDepthControl dc) {
        this.depthControl = dc;
    }

    /**
     * Initialize the processor.
     */
    public abstract void init();

    /**
     * Processor initialization flag.
     */
    private Boolean initialized = false;

    /**
     * Get initialization status.
     *
     * @return True if processor is initialized, false otherwise
     */
    public final Boolean getInitialized() {
        return initialized;
    }

    /**
     * Set initialization status.
     *
     * @param i True if processor is initialized, false otherwise
     */
    public final void setInitialized(final Boolean i) {
        this.initialized = i;
    }

    /**
     * Current document.
     */
    private Document currentDocument;

    /**
     * Get current document.
     *
     * @return Current document for subclasses on processing
     */
    protected final Document current() {
        return currentDocument;
    }

    /**
     * Provides a description of a Processor.
     *
     * @return Description of a processor
     */
    public abstract String describe();

    /**
     * Extract document.
     *
     * @param doc Document to run processor on
     */
    public final void extract(final Document doc) {

        currentDocument = doc;

        extractDocument(doc);

        if (getDepthControl() != ProcessorDepthControl.DOCUMENT) {
            for (Chapter chapter : doc.getChapters()) {
                extract(chapter);
            }
        }

        onProcessed(doc);

        currentDocument = null;
    }

    /**
     * Run concrete processor on document.
     *
     * @param doc Document to run processor on
     */
    public void extractDocument(final Document doc) {

    }

    /**
     * Hook when document has been entirely processed.
     *
     * @param doc Document to run processor on
     */
    public void onProcessed(final Document doc) {

    }

    /**
     * Run processor on a Chapter.
     *
     * @param chapter Chapter to run processor on
     */
    public final void extract(final Chapter chapter) {
        extractChapter(chapter);

        if (getDepthControl() == ProcessorDepthControl.CHAPTER) {
            return;
        }

        for (Paragraph paragraph : chapter.getParagraphs()) {
            extract(paragraph);
        }
    }

    /**
     * Run concrete processor on chapter.
     *
     * @param chapter Chapter to run processor on
     */
    public void extractChapter(final Chapter chapter) {

    }

    /**
     * Run processor on a Paragraph.
     *
     * @param paragraph Paragraph to run processor on
     */
    public final void extract(final Paragraph paragraph) {
        extractParagraph(paragraph);

        if (getDepthControl() == ProcessorDepthControl.PARAGRAPH) {
            return;
        }

        for (Sentence sentence : paragraph.getSentences()) {
            extract(sentence);
        }
    }

    /**
     * Run concrete processor on paragraph.
     *
     * @param paragraph Chapter to run processor on
     */
    public void extractParagraph(final Paragraph paragraph) {

    }

    /**
     * Run processor on a Sentence.
     *
     * @param sentence Sentence to run processor on
     */
    public final void extract(final Sentence sentence) {
        extractSentence(sentence);

        if (getDepthControl() == ProcessorDepthControl.SENTENCE) {
            return;
        }

        for (Token token : sentence.getTokens()) {
            extractToken(token);
        }
    }

    /**
     * Run concrete processor on sentence.
     *
     * @param sentence Sentence to run processor on
     */
    public void extractSentence(final Sentence sentence) {

    }


    /**
     * Run concrete processor on a Token.
     *
     * @param token Token to run processor on
     */
    public void extractToken(final Token token) {

    }

}
