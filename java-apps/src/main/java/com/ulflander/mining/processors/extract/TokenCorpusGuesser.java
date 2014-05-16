package com.ulflander.mining.processors.extract;

import com.ulflander.app.model.Token;
import com.ulflander.app.model.TokenType;
import com.ulflander.mining.processors.Requires;
import com.ulflander.mining.services.Corpora;
import com.ulflander.mining.services.CorporaResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Compare each token to a list of names and add score for token that looks
 * like a name part.
 *
 * Created by Ulflander on 4/14/14.
 */
@Requires(processors = {
        "extract.DocumentTokenizer"
})
public class TokenCorpusGuesser extends AbstractCorpusGuesser {


    /**
     * Logger.
     */
    private static final Logger LOGGER =
        LogManager.getLogger(TokenCorpusGuesser.class);


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

        CorporaResponse res = Corpora.query(s, CORPORA);
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
            res = Corpora.query(s, CORPORA_ASSOC);
            score(res.getCorpora(), token, score * 2);
            score(res.getCorpora(), next, score * 2);
        }

        Token third = next.getNext();

        if (third == null) {
            return;
        }

        s += " " + third.getClean();

        if (third.getType() == TokenType.KEYWORD) {

            res = Corpora.query(s, CORPORA_ASSOC);
            score(res.getCorpora(), token, score * 2);
            score(res.getCorpora(), next, score * 2);
            score(res.getCorpora(), third, score * 2);
        }
    }


}
