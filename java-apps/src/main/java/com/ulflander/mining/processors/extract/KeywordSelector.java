package com.ulflander.mining.processors.extract;

import com.ulflander.app.model.Document;
import com.ulflander.app.model.Token;
import com.ulflander.app.model.TokenType;
import com.ulflander.mining.processors.Processor;
import com.ulflander.mining.processors.ProcessorDepthControl;
import com.ulflander.mining.processors.Requires;

import java.util.HashMap;

/**
 * Select some keywords based on tokens frequency.
 *
 * Created by Ulflander on 4/17/14.
 */
@Requires(processors = {
        "extract.TokenFrequency"
})
public class KeywordSelector extends Processor {

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
        Float avg = (Float) doc.getProperty("meta", "avg_token_frequency");
        HashMap<Token, Integer> freq = doc.getFrequency();
        HashMap<String, Integer> result = new HashMap<String, Integer>();
        String st;
        for (Token t: freq.keySet()) {
            st = t.getClean();
            if (freq.get(t) > avg
                    && t.getType() == TokenType.KEYWORD
                    && st.length() > 1) {
                result.put(st, freq.get(t));
            }
        }

        int idx = 0;
        for (String s: result.keySet()) {
            doc.addProperty("keywords", "keyword_" + idx, s);
            idx += 1;
        }
    }
}
