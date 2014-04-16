package com.ulflander.mining.processors.extract;

import com.ulflander.application.Conf;
import com.ulflander.application.model.Token;
import com.ulflander.application.model.TokenType;
import com.ulflander.application.utils.UlfFileUtils;
import com.ulflander.mining.processors.ILocalizedProcessor;
import com.ulflander.mining.processors.Processor;
import com.ulflander.mining.processors.ProcessorDepthControl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Compare each token to a list of names and add score for token that looks
 * like a name part.
 *
 * Created by Ulflander on 4/14/14.
 */
public class TokenListGuesser extends Processor implements ILocalizedProcessor {

    /**
     * Token length minimum for score to not be decreased.
     */
    private static final int MIN_TOKEN_LENGTH = 3;

    /**
     * Default score.
     */
    private static final int DEFAULT_SCORE = 2;


    @Override
    public final String[] getLanguages() {
        return new String[]{"en", "fr", "es", "de", "it"};
    }

    /**
     * Logger.
     */
    private static final Logger LOGGER =
        LogManager.getLogger(TokenListGuesser.class);

    /**
     * List of names.
     */
    private Set<String> names = new HashSet<String>();

    @Override
    public final void init() {

        // Read names
        String ctn = UlfFileUtils.read(Conf.getDataPath() + "names.txt");
        StringTokenizer st = new StringTokenizer(ctn, "\n");
        while (st.hasMoreTokens()) {
            names.add(st.nextToken());
        }

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

        int score = DEFAULT_SCORE;

        // Decrease score for short tokens
        if (token.getRaw().length() < MIN_TOKEN_LENGTH) {
            score = DEFAULT_SCORE - 1;
        }

        if (names.contains(token.getRaw().toLowerCase())) {
            token.score(TokenType.PERSON_PART, score);
        }
    }
}
