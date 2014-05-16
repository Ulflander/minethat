package com.ulflander.mining.processors.extract;

import com.ulflander.app.model.Document;
import com.ulflander.mining.processors.Processor;
import com.ulflander.mining.processors.ProcessorDepthControl;

/**
 * Processor that clean a document, intended to run as first processor.
 *
 * @author Ulflander <xlaumonier@gmail.com>
 * @since 2/21/14
 */
public class DocumentCleaner extends Processor {

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
        return "Clean and harmonize some chars on raw text before any treatment"
            + " (line breaks chars, spaces chars, ...)";
    }

    /**
     * Run processor on a Document.
     *
     * @param doc Document to run processor on
     */
    @Override
    public final void extractDocument(final Document doc) {

        // First trim
        String raw = doc.getSurface();

            // \r\n
        raw = raw.replaceAll("\\r\\n", "\n")
            // \r
            .replaceAll("\\r", "\n")
            // Vertical tab space char
            .replaceAll("[\\x20]", " ")
            // Vertical |
            .replaceAll(" \\| ", ". ")
            // Untokenizable
            .replaceAll("\\uFFFD", " ");


        doc.setSurface(raw);
    }

}
