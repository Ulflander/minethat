package com.ulflander.mining.processors.extract;

import com.ulflander.app.model.Chapter;
import com.ulflander.app.model.Document;
import com.ulflander.app.model.Paragraph;
import com.ulflander.app.model.Sentence;
import com.ulflander.mining.processors.Processor;
import com.ulflander.mining.processors.ProcessorDepthControl;
import com.ulflander.mining.processors.Requires;

/**
 * Processor that amount of token for each sentence/paragraph/chapter/doc.
 *
 * This processors requires the DocumentTokenizer processor.
 *
 * Created by Ulflander on 4/16/14.
 */
@Requires(processors = {
        "extract.DocumentTokenizer"
})
public class TokenCounter extends Processor {

    @Override
    public final void init() {
        this.setDepthControl(ProcessorDepthControl.DOCUMENT);
        this.setInitialized(true);
    }

    @Override
    public final String describe() {
        return "Populates token counters with real numbers";
    }

    @Override
    public final void extractDocument(final Document doc) {
        int docCount = 0;
        int chapCount;
        int paragraphCount;
        int t;

        for (Chapter c : doc.getChapters()) {
            chapCount = 0;

            for (Paragraph p : c.getParagraphs()) {
                paragraphCount = 0;

                for (Sentence s : p.getSentences()) {

                    t = s.getTokensSize();
                    paragraphCount += t;
                    chapCount += t;
                    docCount += t;
                }

                p.setTotalToken(paragraphCount);
            }

            c.setTotalToken(chapCount);
        }

        doc.setTotalToken(docCount);
    }

}
