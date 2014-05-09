package com.ulflander.mining.processors.extract;

import com.ulflander.app.model.Token;
import com.ulflander.app.model.TokenType;
import com.ulflander.mining.processors.Requires;
import com.ulflander.mining.services.Corpora;
import com.ulflander.mining.services.CorporaResponse;
import com.ulflander.mining.processors.ILocalizedProcessor;
import com.ulflander.mining.processors.Processor;
import com.ulflander.mining.processors.ProcessorDepthControl;

import java.util.ArrayList;

/**
 * Consolidate sibbling tokens types based on some corpuses.
 *
 * For example: Vladimir is both a name and a place.
 * In "President Vladimir Putin" sentence, "Vladimir" will be tagged both as
 * place and name. Running corpus consolidation will increase score of name type
 * as "President" is part of name corpus, but not of place corpus.
 *
 *
 *
 * Created by Ulflander on 4/14/14.
 */
@Requires(processors = {
        "extract.DocumentTokenizer"
})
public class TokenCorpusConsolidation extends Processor
        implements ILocalizedProcessor {

    /**
     * Token length minimum for score to not be decreased.
     */
    private static final int MIN_TOKEN_LENGTH = 3;

    /**
     * Default score.
     */
    private static final int DEFAULT_SCORE = 4;

    /**
     * Default radius.
     */
    private static final int DEFAULT_RADIUS = 4;


    @Override
    public final String[] getLanguages() {
        return new String[]{"en"};
    }

    /**
     * List of corpus files for this guesser.
     */
    private static String[] corpora = new String[]{
            "en/places-cxt",
            "en/companies-cxt",
            "en/names-cxt",
            "en/dates-cxt",
            "en/money-cxt"
    };

    @Override
    public final void init() {
        this.setInitialized(true);
        this.setDepthControl(ProcessorDepthControl.TOKEN);
    }

    @Override
    public final String describe() {
        return "Compares tokens to corpuses, and consolidate corresponding"
                + " sibbling tokens type.";
    }


    @Override
    public final void extractToken(final Token token) {


        if (token.getType() == TokenType.COMMA
                || token.getType() == TokenType.NUMERIC) {
            return;
        }

        String s = token.getSurface().toLowerCase();
        int l = s.length();

        // Decrease score for short tokens
        if (l < MIN_TOKEN_LENGTH) {
            return;
        }

        CorporaResponse res = Corpora.query(s, corpora);
        if (res == null || res.length() == 0) {
            return;
        }

        ArrayList<String> c = res.getCorpora();


        /**
         * Companies
         */
        if (c.contains("en/companies-cxt")) {
            token.consolidate(TokenType.ORGANIZATION,
                    DEFAULT_SCORE, DEFAULT_RADIUS);
            token.discriminate(TokenType.ORGANIZATION);
            token.score(TokenType.ORGANIZATION_DESCRIPTOR, DEFAULT_SCORE);
        }

        /**
         * If token indicates a person name context.
         */
        if (c.contains("en/names-cxt")) {

            // We consolidate around person parts
            token.consolidate(TokenType.PERSON_PART,
                    DEFAULT_SCORE, DEFAULT_RADIUS);

            // We discriminate person part for this one token
            token.discriminate(TokenType.PERSON_PART);
            // and apply it person descriptor
            token.score(TokenType.PERSON_DESCRIPTOR, DEFAULT_SCORE);

            // And we infer that location parts around may not be
            // location parts
            token.consolidate(TokenType.LOCATION_PART, -DEFAULT_SCORE,
                    DEFAULT_RADIUS/2);
        }

        if (c.contains("en/places-cxt")) {
            token.consolidate(TokenType.LOCATION_PART,
                    DEFAULT_SCORE, DEFAULT_RADIUS);
            token.discriminate(TokenType.LOCATION_PART);
            token.score(TokenType.LOCATION_DESCRIPTOR, DEFAULT_SCORE);
        }


        if (c.contains("en/dates-cxt")) {
            token.score(TokenType.DATE_PART, 1);
        }

        if (c.contains("en/money-cxt")) {
            token.score(TokenType.MONEY_AMOUNT, 1);
            token.consolidate(TokenType.MONEY_AMOUNT,
                    DEFAULT_SCORE, DEFAULT_RADIUS);
        }


    }
}
