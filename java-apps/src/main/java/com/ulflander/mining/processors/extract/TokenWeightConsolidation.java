package com.ulflander.mining.processors.extract;

import com.ulflander.app.model.Document;
import com.ulflander.app.model.Token;
import com.ulflander.app.model.TokenType;
import com.ulflander.mining.processors.Processor;
import com.ulflander.mining.processors.ProcessorDepthControl;
import com.ulflander.mining.processors.Requires;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Consolidate weight of tokens based on in-document frequency (any language).
 *
 * Created by Ulflander on 4/29/14.
 */
@Requires(processors = {
        "extract.DocumentTokenizer"
})
public class TokenWeightConsolidation extends Processor {

    @Override
    public void init() {
        this.setDepthControl(ProcessorDepthControl.TOKEN);
        this.setInitialized(true);
    }

    @Override
    public String describe() {
        return "";
    }

    /**
     * Map token object/score.
     */
    private HashMap<String, List<Token>> tokens;

    @Override
    public final void extractDocument(final Document doc) {
        tokens = new HashMap<String, List<Token>>();
    }

    @Override
    public final void extractToken(final Token token) {
        TokenType tt = token.getType();
        if (tt != TokenType.KEYWORD
                && tt != TokenType.WORD) {
            return;
        }

        String clean = token.getClean();

        if (!tokens.containsKey(clean)) {
            tokens.put(clean, new ArrayList<Token>());
            tokens.get(clean).add(token);
        } else {
            tokens.get(clean).add(token);
        }
    }

    @Override
    public final void onProcessed(final Document doc) {
        for (String key: tokens.keySet()) {
            if (tokens.get(key).size() > 1) {
                for (Token t: tokens.get(key)) {
                    t.setWeight(0.9f);
                }
            }
        }
    }
}
