package com.ulflander.mining.processors.extract;

import com.ulflander.app.model.Token;
import com.ulflander.app.model.TokenType;
import com.ulflander.mining.Patterns;
import com.ulflander.mining.processors.Processor;
import com.ulflander.mining.processors.ProcessorDepthControl;
import com.ulflander.mining.processors.Requires;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Try to find some unknown acronyms in the text.
 *
 * Created by Ulflander on 4/27/14.
 */
@Requires(processors = {
        "extract.DocumentTokenizer"
})
public class AcronymExtractor extends Processor {

    /**
     * Maximum length for an acronym.
     */
    private final static int MAX_TEST_LENGTH = 8;

    /**
     * Minimum length for an acronym.
     */
    private final static int MIN_TEST_LENGTH = 1;

    @Override
    public final void init() {
        this.setDepthControl(ProcessorDepthControl.TOKEN);
        this.setInitialized(true);
    }

    @Override
    public final String describe() {
        return "Try to detect acronyms";
    }

    @Override
    public final void extractToken(final Token token) {
        String s = token.getSurface();
        int l = s.length();

        if (token.getType() != TokenType.KEYWORD
                && token.getType() != TokenType.WORD) {
            return;
        }

        if (l < MAX_TEST_LENGTH
            && l > MIN_TEST_LENGTH
            && Patterns.IS_TEXT_UPPERCASE.matcher(s).matches()) {

            // Compose pattern
            String p = "";
            int i;
            for (i = 0; i < l; i++) {
                p += s.charAt(i) + "[a-z]+";

                if (i < l - 1) {
                    p += "[ \\-\\.]";
                }
            }

            Pattern pat = Pattern.compile(p);

            Matcher m = pat.matcher(current().getSurface());
            Set<String> candidates = new HashSet<String>();

            while (m.find()) {
                candidates.add(m.group());
            }

            int amount = candidates.size();
            if (amount == 1) {
                token.score(TokenType.ACRONYM, 4);
            } else if (candidates.size() > 0) {
                token.score(TokenType.ACRONYM, 1);
            }
        }
    }


}
