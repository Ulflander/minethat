package com.ulflander.mining.processors.extract;

import com.ulflander.app.model.Language;
import com.ulflander.app.model.Sentence;
import com.ulflander.app.model.Token;
import com.ulflander.app.model.TokenType;
import com.ulflander.mining.nlp.BasicTokenizer;
import com.ulflander.mining.nlp.EnPOSTagTokenizer;
import com.ulflander.mining.nlp.Tokenizer;
import com.ulflander.mining.processors.Processor;
import com.ulflander.mining.processors.ProcessorDepthControl;
import com.ulflander.mining.processors.Requires;

/**
 * Select and applies a Tokenization strategy.
 *
 * An early tokenization is performed by DocumentSplitter processor, but
 * in the case we had a POS tagger working, we'll rework this tokenization
 * using POS tagger result.
 *
 * Once tokenized, some basic rules are applied to token weight (related to
 * capitalization, token length...).
 *
 * Created by Ulflander on 4/16/14.
 */
@Requires(processors = {
        "extract.LanguageDetector"
})
public class DocumentTokenizer extends Processor {



    /**
     * Token size max for low weighted tokens.
     */
    private static final int LOW_WEIGHT_TOKEN_SIZE = 5;

    /**
     * Token size min for heavy weighted tokens.
     */
    private static final int HEAVY_WEIGHT_TOKEN_SIZE = 10;

    @Override
    public final void init() {
        this.setDepthControl(ProcessorDepthControl.SENTENCE);
        this.setInitialized(true);
    }

    @Override
    public final String describe() {
        return "Tokenize each sentence, using different strategies depending "
                + "on language.";
    }

    @Override
    public final void extractSentence(final Sentence sentence) {
        Tokenizer t = getTokenizer(sentence);
        t.tokenize(sentence);

        for (Token tk: sentence.getTokens()) {
            if (tk.getSurface().matches("^[0-9\\.]+$")) {
                tk.setType(TokenType.NUMERIC);
            }

            int l = tk.size();
            if (l < LOW_WEIGHT_TOKEN_SIZE) {
                tk.setWeight(0.3f);
            } else if (l > HEAVY_WEIGHT_TOKEN_SIZE) {
                tk.setWeight(0.8f);
            }

            if (tk.isCapitalized()) {
                tk.setWeight(0.8f);
            } else if (tk.isUppercased()) {
                tk.setWeight(0.6f);
            }

            if (tk.getType() == TokenType.KEYWORD) {
                tk.setWeight(0.9f);
            }
        }
    }

    /**
     * Select tokenization strategy depending on
     * language and POS tagging status.
     *
     * @param sentence Sentence to tokenize
     * @return Tokenizer suitable for sentence
     */
    private Tokenizer getTokenizer(final Sentence sentence) {
        String raw = sentence.getRawPartOfSpeech();

        // English Stanford POSTagger tokenization
        if (sentence.getLanguage() == Language.EN
                && raw != null && !raw.equals("")) {

            return new EnPOSTagTokenizer();
        }

        return new BasicTokenizer();
    }
}
