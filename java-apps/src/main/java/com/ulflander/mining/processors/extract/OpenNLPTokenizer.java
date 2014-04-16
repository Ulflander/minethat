package com.ulflander.mining.processors.extract;

import com.ulflander.application.Conf;
import com.ulflander.application.model.Sentence;
import com.ulflander.mining.processors.Processor;
import com.ulflander.mining.processors.ProcessorDepthControl;
import com.ulflander.mining.processors.Requires;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;

/**
 * Processor that tokenize a sentence (Using Apache OpenNLP).
 *
 * @author Ulflander <xlaumonier@gmail.com>
 * @since 2/21/14
 */
@Requires(processors = {
    "extract.DocumentSplitter"
})
public class OpenNLPTokenizer extends Processor {

    /**
     * Logger.
     */
    private static final Logger LOGGER =
        LogManager.getLogger(OpenNLPTokenizer.class);

    /**
     * Tokenizer reference.
     */
    private Tokenizer tokenizer = null;

    /**
     * Initialize the processor (Load tokenizer data).
     */
    @Override
    public final void init() {
        setDepthControl(ProcessorDepthControl.SENTENCE);

        File modelIn;
        try {
            // Loading tokenizer model
            modelIn = new File(Conf.getDataPath() + "open-nlp/en-token.bin");
            final TokenizerModel tokenModel = new TokenizerModel(modelIn);

            tokenizer = new TokenizerME(tokenModel);

            setInitialized(true);

        } catch (final IOException ioe) {
            LOGGER.error("OpenNLPTokenizer couldnt "
                + "init english model", ioe);
        }
    }


    /**
     * Provides a description of this Processor.
     *
     * @return Description of this processor
     */
    @Override
    public final String describe() {
        return "Tokenize sentences using OpenNLP";
    }

    /**
     * Run processor on a Sentence.
     *
     * @param sentence Sentence to run processor on
     */
    @Override
    public final void extractSentence(final Sentence sentence) {
        String raw = sentence.getRaw();
        String[] tokens = tokenizer.tokenize(raw);

        for (int i = 0, l = tokens.length; i < l; i++) {
            LOGGER.debug(tokens[i]);
        }
    }


}
