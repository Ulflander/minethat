package com.ulflander.mining.processors.extract;

import com.ulflander.app.model.Token;
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
public class AggregatedCorpusGuesser extends AbstractCorpusGuesser {


    /**
     * Logger.
     */
    private static final Logger LOGGER =
        LogManager.getLogger(AggregatedCorpusGuesser.class);


    @Override
    public final void extractToken(final Token token) {

        if (!token.getAggregated()) {
            return;
        }

        CorporaResponse res = Corpora.query(token.getClean(), CORPORA);
        if (res == null) {
            LOGGER.warn("Corpora returned a null response");
            return;
        }

        score(res.getCorpora(), token, DEFAULT_SCORE * 2);
    }


}
