package com.ulflander.mining.processors.augment;

import com.ulflander.app.model.Document;
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
     * Max score.
     */
    public static final int MAX_SCORE = 100;

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
     * Fake flag.
     */
    public static final int HAS_FAKE_FLAG = -50;

    /**
     * Godwin point score.
     */
    public static final int GODWIN_POINT_SCORE = -20;

    /**
     * One hundred.
     */
    public static final int ONE_HUNDRED = 100;

    /**
     * Max frequency.
     */
    public static final int MAX_FREQUENCY = 4;



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

    /**
     * Number of entities.
     */
    private int entities = 0;

    @Override
    public final void extractDocument(final Document doc) {
        float f = (float) doc.getProperty("basic_stats", "ave_sent_per_par");

        float tokenAveFreq =
                (float) doc.getProperty("basic_stats", "avg_token_frequency");
        float entityAveFreq =
                (float) doc.getProperty("basic_stats", "avg_token_frequency");

        score = (int) ((entityAveFreq * ONE_HUNDRED / MAX_FREQUENCY)
                        + (tokenAveFreq * ONE_HUNDRED / MAX_FREQUENCY)) / 2;

        if (f < LOW_SENTENCE_PARAGRAPH_RATIO) {
            score += LOW_SENTENCE_PARAGRAPH_RATIO_SCORE;
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

        if (token.hasEntity()) {
            entities += 1;
        }
    }

    @Override
    public final void onProcessed(final Document doc) {


        if (score > MAX_SCORE) {
            score = MAX_SCORE;
        } else if (score < 0) {
            score = 0;
        }
        doc.addProperty("basic_stats", "quality_score", score);
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
