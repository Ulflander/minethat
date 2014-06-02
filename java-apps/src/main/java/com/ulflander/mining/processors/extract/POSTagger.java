package com.ulflander.mining.processors.extract;

import com.ulflander.app.Conf;
import com.ulflander.app.model.Language;
import com.ulflander.app.model.Sentence;
import com.ulflander.mining.processors.ILocalizedProcessor;
import com.ulflander.mining.processors.Processor;
import com.ulflander.mining.processors.ProcessorDepthControl;
import com.ulflander.mining.processors.Requires;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Processor that detects part of speech in text (Using Stanford POSTagger).
 *
 * @author Ulflander <xlaumonier@gmail.com>
 * @since 2/21/14
 */
@Requires(processors = {
    "extract.DocumentSplitter",
    "extract.LanguageDetector"
})
public class POSTagger extends Processor implements ILocalizedProcessor {

    /**
     * Logger.
     */
    private static final Logger LOGGER =
        LogManager.getLogger(POSTagger.class);

    /**
     * English POSTagger.
     */
    private static MaxentTagger enTagger;

    /**
     * Arabian POSTagger.
     */
    private static MaxentTagger arTagger;

    /**
     * Chinese POSTagger.
     */
    private static MaxentTagger zhTagger;

    /**
     * French POSTagger.
     */
    private static MaxentTagger frTagger;

    /**
     * German POSTagger.
     */
    private static MaxentTagger deTagger;

    /**
     * Initialize the processor.
     */
    @Override
    public final void init() {
        setDepthControl(ProcessorDepthControl.SENTENCE);
        setInitialized(true);
    }

    /**
     * Get languages supported by this processor.
     *
     * @return Array of language codes
     */
    @Override
    public final String[] getLanguages() {
        return new String[]{"en", "ar", "zh", "fr", "de"};
    }

    /**
     * Provides a description of this Processor.
     *
     * @return Description of this processor
     */
    @Override
    public final String describe() {
        return "POSTagger is a Part-Of-Speech "
            + "extractor based on Stanford POSTagger.";
    }

    /**
     * Run processor on a Sentence.
     *
     * @param sentence Sentence to run processor on
     */
    @Override
    public final void extractSentence(final Sentence sentence) {
        MaxentTagger tagger = getTagger(current().getLanguage());
        if (tagger == null) {
            LOGGER.error("POSTagger doesnt supports "
                + sentence.getLanguage());
            return;
        }
        String tagged = tagger.tagString(sentence.getSurface());
        sentence.setRawPartOfSpeech(tagged);
    }

    /**
     * Get POSTagger for given language.
     *
     * @param lang Language of needed POSTagger
     * @return POSTagger reference or null if not found for language
     */
    public static final MaxentTagger getTagger(final Language lang) {
        if (lang == Language.EN) {
            return getEnTagger();
        }
        if (lang == Language.AR) {
            return getArTagger();
        }
        if (lang == Language.ZH) {
            return getZhTagger();
        }
        if (lang == Language.FR) {
            return getFrTagger();
        }
        if (lang == Language.DE) {
            return getDeTagger();
        }

        return null;
    }

    /**
     * Get Part Of Speech tagger for english language.
     *
     * @return POSTagger for english language
     */
    public static final MaxentTagger getEnTagger() {
        if (enTagger == null) {
            enTagger = new MaxentTagger(Conf.getDataPath()
                + "postagger-models/wsj-0-18-left3words-nodistsim.tagger");
        }
        return enTagger;
    }

    /**
     * Get Part Of Speech tagger for arabian language.
     *
     * @return POSTagger for arabian language
     */
    public static final MaxentTagger getArTagger() {
        if (arTagger == null) {
            arTagger = new MaxentTagger(Conf.getDataPath()
                + "postagger-models/arabic.tagger");
        }
        return arTagger;
    }

    /**
     * Get Part Of Speech tagger for chinese language.
     *
     * @return POSTagger for chinese language
     */
    public static final MaxentTagger getZhTagger() {
        if (zhTagger == null) {
            zhTagger = new MaxentTagger(Conf.getDataPath()
                + "postagger-models/chinese-distsim.tagger");
        }
        return zhTagger;
    }

    /**
     * Get Part Of Speech tagger for french language.
     *
     * @return POSTagger for french language
     */
    public static final MaxentTagger getFrTagger() {
        if (frTagger == null) {
            frTagger = new MaxentTagger(Conf.getDataPath()
                + "postagger-models/french.tagger");
        }
        return frTagger;
    }

    /**
     * Get Part Of Speech tagger for german language.
     *
     * @return POSTagger for german language
     */
    public static final MaxentTagger getDeTagger() {
        if (deTagger == null) {
            deTagger = new MaxentTagger(Conf.getDataPath()
                + "postagger-models/german-fast.tagger");
        }
        return deTagger;
    }
}
