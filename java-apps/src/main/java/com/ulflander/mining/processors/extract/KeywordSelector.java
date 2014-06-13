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
import java.util.Iterator;
import java.util.List;

/**
 * Select some keywords based on tokens frequency and weight.
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

    /**
     * Maximum amount of keywords to set as doc meta.
     */
    public static final int MAX_KEYWORDS = 100;


    @Override
    public final void init() {
        this.setDepthControl(ProcessorDepthControl.DOCUMENT);
        this.setInitialized(true);
    }

    @Override
    public final String describe() {
        return "Select some keywords based on tokens frequency and weight";
    }

    @Override
    public final void extractDocument(final Document doc) {
        Float avg =
                (Float) doc.getProperty("basic_stats", "avg_token_frequency");
        HashMap<Token, Integer> freq = doc.getFrequency();
        final HashMap<Token, Integer> result = new HashMap<Token, Integer>();

        Float avgWeight =
                (Float) doc.getProperty("basic_stats", "avg_token_weight");

        Token st;
        for (Token t: freq.keySet()) {
            st = t;
            int score = freq.get(t);

            if ((score > avg / 2 || t.getAggregated())
                    && t.getWeight() > avgWeight
                    && !t.hasScore(TokenType.PERSON_PART)
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
                return result.get(s2).compareTo(result.get(s1));
            }
        });


        int idx = 0;
        ArrayList<String> finalList = new ArrayList<String>();
        Iterator<Token> tokens = keywords.iterator();
        while (tokens.hasNext() && idx < MAX_KEYWORDS) {
            Token t = tokens.next();
            finalList.add(t.getSurface());
            idx += 1;
        }

        doc.addProperty("keywords", "main", finalList);
    }
}
