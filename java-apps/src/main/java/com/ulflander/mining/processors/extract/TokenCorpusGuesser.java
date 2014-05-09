package com.ulflander.mining.processors.extract;

import com.ulflander.app.model.Token;
import com.ulflander.app.model.TokenType;
import com.ulflander.mining.processors.Requires;
import com.ulflander.mining.services.Corpora;
import com.ulflander.mining.services.CorporaResponse;
import com.ulflander.mining.processors.ILocalizedProcessor;
import com.ulflander.mining.processors.Processor;
import com.ulflander.mining.processors.ProcessorDepthControl;
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
public class TokenCorpusGuesser extends Processor
        implements ILocalizedProcessor {

    /**
     * Token length minimum for score to not be decreased.
     */
    private static final int MIN_TOKEN_LENGTH = 5;

    /**
     * Default score.
     */
    private static final int DEFAULT_SCORE = 3;

    /**
     * High score.
     */
    private static final int HIGH_SCORE = 10;

    /**
     * List of corpus files for this guesser.
     */
    private static String[] corpora = new String[]{
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
    private static String[] corporaAssoc = new String[]{
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
        LogManager.getLogger(TokenCorpusGuesser.class);

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


    @Override
    public final void extractToken(final Token token) {

        if (token.getType() == TokenType.COMMA
            || token.getType() == TokenType.VERB
            || token.getType() == TokenType.OPERATOR
            || token.getType() == TokenType.NUMERIC) {
            return;
        }

        String s = token.getClean();
        int l = s.length();
        int score = DEFAULT_SCORE;

        // Decrease score for short tokens
        if (l < MIN_TOKEN_LENGTH) {
            score = score / 2;
        }

        CorporaResponse res = Corpora.query(s, corpora);
        if (res == null) {
            LOGGER.warn("Corpora returned a null response");
            return;
        }

        score(res.getCorpora(), token, score);
        Token next = token.getNext();

        if (next == null) {
            return;
        }

        s += " " + next.getClean();

        if (next.getType() == TokenType.KEYWORD) {
            res = Corpora.query(s, corporaAssoc);
            score(res.getCorpora(), token, score * 2);
            score(res.getCorpora(), next, score * 2);
        }

        Token third = next.getNext();

        if (third == null) {
            return;
        }

        s += " " + third.getClean();

        if (third.getType() == TokenType.KEYWORD) {

            res = Corpora.query(s, corporaAssoc);
            score(res.getCorpora(), token, score * 2);
            score(res.getCorpora(), next, score * 2);
            score(res.getCorpora(), third, score * 2);
        }
    }


    /**
     * Given corpus names returned by Corpora service,
     * score token for various token types.
     *
     * @param c Corpus files
     * @param t Token
     * @param score Score to add to token
     */
    private void score(final ArrayList<String> c,
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
            t.score(TokenType.LOCATION_PART, score);
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
