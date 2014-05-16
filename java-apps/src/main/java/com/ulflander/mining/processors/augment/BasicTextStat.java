package com.ulflander.mining.processors.augment;

import com.ulflander.app.model.Chapter;
import com.ulflander.app.model.Document;
import com.ulflander.app.model.Paragraph;
import com.ulflander.mining.processors.Processor;
import com.ulflander.mining.processors.ProcessorDepthControl;
import com.ulflander.mining.processors.Requires;

/**
 * Processor that returns a staistic analysis about the document.
 *
 * @author Ulflander <xlaumonier@gmail.com>
 * @since 2/21/14
 */
@Requires(processors = {
    "extract.DocumentSplitter",
    "extract.LanguageDetector"
})
public class BasicTextStat extends Processor {


    /**
     * Number of tokens one can read in one second in average.
     */
    public static final Integer TOKENS_READ_PER_SECOND = 4;


    /**
     * Number of seconds in a minute.
     */
    private static final Integer SEC_PER_MIN = 60;

    /**
     * One hundred.
     */
    private static final Integer ONE_HUNDRED = 100;

    /**
     * Floated one hundred.
     */
    private static final Float FL_ONE_HUNDRED = 100f;

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
        return "Gives some basic statistics about the text.";
    }

    @Override
    public final void extractDocument(final Document doc) {
        int totalParagraph = 0;
        int totalSentence = 0;

        for (Chapter c : doc.getChapters()) {
            for (Paragraph p : c.getParagraphs()) {
                totalParagraph++;
                totalSentence += p.getSentencesSize();
            }
        }

        doc.addProperty("basic_stats", "text_length", doc.getTextLength());
        doc.addProperty("basic_stats", "total_tokens", doc.getTotalToken());
        doc.addProperty("basic_stats", "total_paragraphs", totalParagraph);
        doc.addProperty("basic_stats", "total_sentences", totalSentence);
        doc.addProperty("basic_stats", "ave_sent_per_par",
                1f * totalSentence / totalParagraph);
        doc.addProperty("basic_stats", "ave_tok_per_sent",
                doc.getTotalToken() / totalSentence);

        doc.addProperty("basic_stats", "read_time_est",
                Math.round(
                    (doc.getTotalToken() / TOKENS_READ_PER_SECOND / SEC_PER_MIN)
                * ONE_HUNDRED) / FL_ONE_HUNDRED);
    }
}
