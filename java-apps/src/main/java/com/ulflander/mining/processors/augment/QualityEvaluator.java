package com.ulflander.mining.processors.augment;

import com.ulflander.app.model.Document;
import com.ulflander.app.model.TextLength;
import com.ulflander.app.model.Token;
import com.ulflander.mining.processors.Processor;
import com.ulflander.mining.processors.ProcessorDepthControl;
import com.ulflander.mining.processors.Requires;

import java.util.Arrays;
import java.util.List;

/**
 * Evaluates quality given a lot of parameters.
 *
 * @author Ulflander <xlaumonier@gmail.com>
 * @since 2/21/14
 */
@Requires(processors = {
    "augment.BasicTextStat",
    "extract.TokenFrequency"
})
public class QualityEvaluator extends Processor {


    /**
     * Initial score.
     */
    public static final int INITIAL_SCORE = 100;

    /**
     * Max score.
     */
    public static final int MAX_SCORE = 200;

    /**
     * Score if specific props are given in meta
     * (always better to have an author, title..).
     */
    public static final int HAS_META_SCORE = 10;

    /**
     * Low average sentence/paragraph - it means that less than 3 sentences
     * per paragraph is considered low.
     */
    public static final int LOW_SENTENCE_PARAGRAPH_RATIO = 3;

    /**
     * Low average sentence/paragraph score.
     */
    public static final int LOW_SENTENCE_PARAGRAPH_RATIO_SCORE = -20;

    /**
     * More tokens are frequents (repeated), more text is considered as poor.
     */
    public static final float HIGH_TOKEN_FREQUENCY = 2.5f;

    /**
     * Score when tokens frequency average is high.
     */
    public static final int HIGH_TOKEN_FREQUENCY_SCORE = -20;

    /**
     * Score when document is long.
     */
    public static final int LONG_TEXT_BONUS_SCORE = 20;

    /**
     * Fake flag.
     */
    public static final int HAS_FAKE_FLAG = -50;

    /**
     * Godwin point score.
     */
    public static final int GODWIN_POINT_SCORE = -20;


    /**
     * Vocable for godwin point.
     */
    public static final List<String> GODWIN_POINT_VOCABLE = Arrays.asList(
            "nazi",
            "nazis",
            "hitler"
    );

    /**
     * Initialize the processor.
     */
    @Override
    public final void init() {
        setDepthControl(ProcessorDepthControl.TOKEN);
        setInitialized(true);
    }

    /**
     * Provides a description of this Processor.
     *
     * @return Description of this processor
     */
    @Override
    public final String describe() {
        return "Evaluates quality of the text.";
    }

    /**
     * Quality score.
     */
    private int score;

    @Override
    public final void extractDocument(final Document doc) {
        float f = (float) doc.getProperty("basic_stats", "ave_sent_per_par");
        float tokenAveFreq =
                (float) doc.getProperty("basic_stats", "avg_token_frequency");
        score = INITIAL_SCORE;

        if ((int) doc.getProperty("basic_stats", "text_length")
                >= TextLength.MEDIUM) {

            score += LONG_TEXT_BONUS_SCORE;
        }

        if (f < LOW_SENTENCE_PARAGRAPH_RATIO) {
            score += LOW_SENTENCE_PARAGRAPH_RATIO_SCORE;
        } else {
            score -= LOW_SENTENCE_PARAGRAPH_RATIO_SCORE;
        }

        if (tokenAveFreq > HIGH_TOKEN_FREQUENCY) {
            score += HIGH_TOKEN_FREQUENCY_SCORE;
        } else {
            score -= HIGH_TOKEN_FREQUENCY_SCORE;
        }

        if (doc.hasProperty("meta", "doc_author")) {
            score += HAS_META_SCORE;
        } else {
            score -= HAS_META_SCORE;
        }

        if (doc.hasProperty("meta", "doc_title")) {
            score += HAS_META_SCORE;
        } else {
            score -= HAS_META_SCORE;
        }

        if (doc.hasProperty("meta", "doc_source")) {
            score += HAS_META_SCORE;
        } else {
            score -= HAS_META_SCORE;
        }

        if (doc.hasProperty("flags", "fake")) {
            score += HAS_FAKE_FLAG;
        }
    }

    @Override
    public final void extractToken(final Token token) {
        if (!current().hasProperty("flag", "godwin")) {
            score += evaluateGodwinPoint(token);
        }
    }

    @Override
    public final void onProcessed(final Document doc) {
        if (score > MAX_SCORE) {
            score = MAX_SCORE;
        } else if (score < 0) {
            score = 0;
        }
        doc.addProperty("basic_stats", "quality_score", score / 2);
    }

    /**
     * Evaluates if token is part of godwin vocable.
     *
     * @param t Token to evaluate
     * @return True if token is like "Nazi" or similar, false otherwise.
     */
    private int evaluateGodwinPoint(final Token t) {
        if (GODWIN_POINT_VOCABLE.contains(t.getClean())) {
            current().addProperty("flag", "godwin", true);
            return GODWIN_POINT_SCORE;
        }
        return 0;
    }
}
