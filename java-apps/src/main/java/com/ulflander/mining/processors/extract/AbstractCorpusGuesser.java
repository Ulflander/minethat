package com.ulflander.mining.processors.extract;

import com.ulflander.app.model.Token;
import com.ulflander.app.model.TokenType;
import com.ulflander.mining.processors.ILocalizedProcessor;
import com.ulflander.mining.processors.Processor;
import com.ulflander.mining.processors.ProcessorDepthControl;
import com.ulflander.mining.processors.Requires;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

/**
 * Compare each token to a list of names and add score for token that looks
 * like a name part.
 *
 * Created by Ulflander on 4/14/14.
 */
@Requires(processors = {
        "extract.DocumentTokenizer"
})
public class AbstractCorpusGuesser extends Processor
        implements ILocalizedProcessor {

    /**
     * Token length minimum for score to not be decreased.
     */
    protected static final int MIN_TOKEN_LENGTH = 2;

    /**
     * Default score.
     */
    protected static final int DEFAULT_SCORE = 3;

    /**
     * High score.
     */
    protected static final int HIGH_SCORE = 10;

    /**
     * List of corpus files for this guesser.
     */
    protected static final String[] CORPORA = new String[]{
            "places",
            "companies",
            "tech-companies",
            "en/countries",
            "en/names",
            "en/nats",
            "en/dates"
    };

    /**
     * List of corpus files for this guesser.
     */
    protected static final String[] CORPORA_ASSOC = new String[]{
            "places",
            "companies",
            "tech-companies",
            "en/nats",
            "en/names"
    };

    @Override
    public final String[] getLanguages() {
        return new String[]{"en", "fr", "es", "de", "it"};
    }

    /**
     * Logger.
     */
    private static final Logger LOGGER =
        LogManager.getLogger(AbstractCorpusGuesser.class);

    @Override
    public final void init() {
        this.setInitialized(true);
        this.setDepthControl(ProcessorDepthControl.TOKEN);
    }

    @Override
    public final String describe() {
        return "Compares tokens to some lists of names, locations, in order "
                + "to reinforce their type";
    }


    /**
     * Given corpus names returned by Corpora service,
     * score token for various token types.
     *
     * @param c Corpus files
     * @param t Token
     * @param score Score to add to token
     */
    protected final void score(final ArrayList<String> c,
                       final Token t,
                       final int score) {

        if (c == null) {
            return;
        }

        if (c.contains("companies")) {
            t.score(TokenType.ORGANIZATION, score);
        } else if (c.contains("tech-companies")) {
            t.score(TokenType.ORGANIZATION, score);
        }

        if (c.contains("en/names")) {
            t.score(TokenType.PERSON_PART, score);
        }

        if (c.contains("en/countries")) {
            t.score(TokenType.LOCATION, score);
        }

        if (c.contains("en/nats")) {
            t.score(TokenType.NATIONALITY, score);
        }

        if (c.contains("places")) {
            t.score(TokenType.LOCATION_PART, score);
        }

        /**
         * This corpus is very short and only
         * contains months and week days, that's why an high score.
         */
        if (c.contains("en/dates")) {
            t.score(TokenType.DATE_PART, HIGH_SCORE);
            t.score(TokenType.PERSON_PART, -score);
            t.score(TokenType.LOCATION_PART, -score);
            t.score(TokenType.ORGANIZATION, -score);
        }
    }
}
