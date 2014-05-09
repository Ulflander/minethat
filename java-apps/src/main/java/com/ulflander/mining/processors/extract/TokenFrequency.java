package com.ulflander.mining.processors.extract;

import com.ulflander.app.model.Document;
import com.ulflander.app.model.Token;
import com.ulflander.app.model.TokenType;
import com.ulflander.mining.processors.Processor;
import com.ulflander.mining.processors.ProcessorDepthControl;
import com.ulflander.mining.processors.Requires;

import java.util.HashMap;

/**
 * Creates a list of tokens based on their frequency, populates document
 * "meta.avg_token_frequency" property.
 *
 * Created by Ulflander on 4/17/14.
 */
@Requires(processors = {
        "extract.DocumentTokenizer"
})
public class TokenFrequency extends Processor {

    @Override
    public final void init() {
        this.setDepthControl(ProcessorDepthControl.TOKEN);
        this.setInitialized(true);
    }

    @Override
    public final String describe() {
        return "Count occurences of each word, keyword and verb";
    }

    /**
     * Map string/token object.
     */
    private HashMap<String, Token> tokens;

    /**
     * Map token object/score.
     */
    private HashMap<Token, Integer> frequency;

    /**
     * Total citations for all tokens.
     */
    private int total;

    @Override
    public final void extractDocument(final Document doc) {
        frequency = new HashMap<Token, Integer>();
        tokens = new HashMap<String, Token>();
        total = 0;
    }

    @Override
    public final void extractToken(final Token token) {
        TokenType tt = token.getType();
        if (tt != TokenType.KEYWORD
            && tt != TokenType.WORD) {
            return;
        }

        Token nt;
        String clean = token.getSingular();

        if (!tokens.containsKey(clean)) {
            nt = new Token();
            nt.setSurface(clean);
            nt.setType(token.getType());
            nt.setTag(token.getTag());
            tokens.put(clean, nt);
            frequency.put(nt, 1);
        } else {
            nt = tokens.get(clean);
            frequency.put(nt, frequency.get(nt) + 1);
        }

        total += 1;

        nt.score(token.getScores());
    }

    @Override
    public final void onProcessed(final Document doc) {
        doc.setFrequency(frequency);
        doc.addProperty("meta", "avg_token_frequency",
                ((float) total / frequency.size()));
    }
}
