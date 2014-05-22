package com.ulflander.mining.processors.extract;

import com.ulflander.app.model.Document;
import com.ulflander.mining.processors.Processor;
import com.ulflander.mining.processors.ProcessorDepthControl;

/**
 * Processor that validates and add some meta tags.
 *
 * @author Ulflander <xlaumonier@gmail.com>
 * @since 2/21/14
 */
public class MetaChecker extends Processor {

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
        return "Validates meta tags";
    }

    /**
     * Run processor on a Document.
     *
     * @param doc Document to run processor on
     */
    @Override
    public final void extractDocument(final Document doc) {
        checkDateMeta(doc);
    }

    /**
     * Set a "meta.date" global tag. Will use tags "doc_published_date",
     * "doc_edited_date", "doc_created_date", "doc_aggregated_date" if they
     * exist and if no one exist then it will set date to "now" timestamp.
     *
     * @param doc Document to check date meta
     */
    private void checkDateMeta(final Document doc) {

        if (doc.hasProperty("meta", "doc_published_date", Long.class)) {
            doc.addProperty("meta", "date",
                    doc.getProperty("meta", "doc_published_date"));

        } else if (doc.hasProperty("meta", "doc_edited_date", Long.class)) {
            doc.addProperty("meta", "date",
                    doc.getProperty("meta", "doc_edited_date"));

        } else if (doc.hasProperty("meta", "doc_created_date", Long.class)) {
            doc.addProperty("meta", "date",
                    doc.getProperty("meta", "doc_created_date"));

        } else if (doc.hasProperty("meta", "doc_aggregated_date", Long.class)) {
            doc.addProperty("meta", "date",
                    doc.getProperty("meta", "doc_aggregated_date"));

        } else {
            doc.addProperty("meta", "date", System.currentTimeMillis());
        }
    }

}
