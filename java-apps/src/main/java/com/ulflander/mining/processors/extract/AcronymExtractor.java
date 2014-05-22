package com.ulflander.mining.processors.extract;

import com.ulflander.app.model.Entity;
import com.ulflander.app.model.EntitySource;
import com.ulflander.app.model.EntityType;
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
     * Score applied to a token when some candidates are found
     * for acronym definition.
     */
    public static final int ACRONYM_SCORE = 4;


    /**
     * Initial confidence for acronym definition.
     */
    public static final Float ACRONYM_DEFINITION_BASE_CONFIDENCE = 0.5f;

    /**
     * Maximum length for an acronym.
     */
    private static final int MAX_TEST_LENGTH = 8;

    /**
     * Minimum length for an acronym.
     */
    private static final int MIN_TEST_LENGTH = 1;

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
            // Compose second pattern (with operators
            String p2 = "";
            int i;
            for (i = 0; i < l; i++) {
                p += s.charAt(i) + "[a-z]+";
                p2 += s.charAt(i) + "[a-z]+";

                if (i < l - 1) {
                    p += "[ \\-\\.]";
                    p2 += "( [a-z']{1,3})+ ";
                }
            }

            applyPattern(p, token);
            applyPattern(p2, token);

        }
    }

    /**
     * Apply a pattern on document surface and select some acronym definition
     * candidates.
     *
     * @param p Pattern
     * @param t Acronym token
     */
    private void applyPattern(final String p, final Token t) {
        Pattern pat = Pattern.compile(p);

        Matcher m = pat.matcher(current().getSurface());
        Set<String> candidates = new HashSet<String>();

        while (m.find()) {
            candidates.add(m.group());
        }

        int amount = candidates.size();
        if (amount == 1) {
            t.score(TokenType.ACRONYM, ACRONYM_SCORE);
        } else if (candidates.size() > 0) {
            t.score(TokenType.ACRONYM, 1);
        }

        for (String candidate: candidates) {
            t.addEntity(new Entity(
                    EntitySource.RAW,
                    candidate,
                    EntityType.ACRONYM_DEFINITION,
                    ACRONYM_DEFINITION_BASE_CONFIDENCE
                ));
        }

    }


}
