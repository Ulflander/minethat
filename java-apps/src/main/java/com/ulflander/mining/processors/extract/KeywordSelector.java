package com.ulflander.mining.processors.extract;

import com.ulflander.app.model.Document;
import com.ulflander.app.model.Token;
import com.ulflander.app.model.TokenType;
import com.ulflander.mining.processors.Processor;
import com.ulflander.mining.processors.ProcessorDepthControl;
import com.ulflander.mining.processors.Requires;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Select some keywords based on tokens frequency.
 *
 * Created by Ulflander on 4/17/14.
 */
@Requires(processors = {
        "extract.TokenFrequency"
})
public class KeywordSelector extends Processor {

    /**
     * Minimum weight of a token to be considered as a keyword.
     */
    public static final float MIN_WEIGHT = 0.6f;


    @Override
    public final void init() {
        this.setDepthControl(ProcessorDepthControl.DOCUMENT);
        this.setInitialized(true);
    }

    @Override
    public final String describe() {
        return "Select some document keywords based on tokens frequency";
    }

    @Override
    public final void extractDocument(final Document doc) {
        Float avg =
                (Float) doc.getProperty("basic_stats", "avg_token_frequency");
        HashMap<Token, Integer> freq = doc.getFrequency();
        final HashMap<Token, Integer> result = new HashMap<Token, Integer>();

        Token st;
        for (Token t: freq.keySet()) {
            st = t;
            int score = freq.get(t);

            if ((score > avg || t.getAggregated())
                    && t.getType() == TokenType.KEYWORD
                    && t.getWeight() > MIN_WEIGHT
                    && st.getClean().length() > 1) {


                result.put(st, score);
            }
        }

        List<Token> keywords = new ArrayList<Token>(result.keySet());
        Collections.sort(keywords, new Comparator<Token>() {
            @Override
            public int compare(final Token s1, final Token s2) {
                return result.get(s1).compareTo(result.get(s2));
            }
        });

        int idx = 0;
        for (Token t: keywords) {
            doc.addProperty("keywords", "keyword_" + idx, t.getSurface());
            idx += 1;
        }
    }
}
