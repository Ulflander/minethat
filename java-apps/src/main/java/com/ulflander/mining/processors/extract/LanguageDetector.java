package com.ulflander.mining.processors.extract;

import com.cybozu.labs.langdetect.Detector;
import com.cybozu.labs.langdetect.DetectorFactory;
import com.ulflander.app.model.Chapter;
import com.ulflander.app.model.Document;
import com.ulflander.app.model.Language;
import com.ulflander.app.model.Paragraph;
import com.ulflander.app.model.Sentence;
import com.ulflander.mining.processors.Processor;
import com.ulflander.mining.processors.ProcessorDepthControl;
import com.ulflander.mining.processors.Requires;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

/**
 * Detects language of a document on the global document,
 * then on each sentences.
 * <p/>
 * Uses https://code.google.com/p/language-detection/
 * @author Nakatani Shuyo
 */
@Requires(processors = {
    "extract.DocumentSplitter"
})
public class LanguageDetector extends Processor {

    /**
     * Logger.
     */
    private static final Logger LOGGER =
        LogManager.getLogger(LanguageDetector.class);


    /**
     * Initialize the processor (Creates the detector factory and load the
     * language profiles).
     */
    @Override
    public final void init() {

        setDepthControl(ProcessorDepthControl.SENTENCE);

        try {
            DetectorFactory.clear();
            DetectorFactory.loadProfile(
                new String[]{"en", "fr", "es", "de", "it", "zh-cn"});
        } catch (Exception e) {
            LOGGER.error("LanguageDetector unable to init", e);
        }

        setInitialized(true);
    }

    /**
     * Provides a description of this Processor.
     *
     * @return Description of this processor
     */
    @Override
    public final String describe() {
        return "Detects language of a document on the global document, "
            + "then on each sentences. Uses "
            + "https://code.google.com/p/language-detection/";
    }

    /**
     * Run processor on a Document.
     *
     * @param doc Document to run processor on
     */
    @Override
    public final void extractDocument(final Document doc) {

        Language lang = detect(doc.getSurface());
        HashMap<Language, Integer> languages = new HashMap<Language, Integer>();

        if (lang != null) {
            doc.setLanguage(lang);

            for (Chapter chap : doc.getChapters()) {
                chap.setLanguage(lang);
                for (Paragraph paragraph : chap.getParagraphs()) {
                    paragraph.setLanguage(lang);
                    for (Sentence sentence : paragraph.getSentences()) {
                        detectSentence(sentence, languages);
                    }
                }
            }
        }

        doc.addProperty("language", "multilingual", languages.size() > 1);
        doc.addProperty("language", "main_language", lang.toString());

        doc.setLanguages(languages);
    }

    /**
     * Detect language at a sentence level.
     *
     * @param sentence Sentence to evaluate
     * @param languages Languages summary to populate
     */
    public final void detectSentence(final Sentence sentence,
                               final HashMap<Language, Integer> languages) {
        Language lang = detect(sentence.getSurface());
        sentence.setLanguage(lang);

        if (languages.containsKey(lang)) {
            languages.put(lang, languages.get(lang) + 1);
        } else {
            languages.put(lang, 1);
        }
    }

    /**
     * Detect language on a string.
     *
     * @param str String to evaluate
     * @return Language found (may be Language.UNKNOWN)
     * @see com.ulflander.app.model.Language
     */
    private Language detect(final String str) {

        if (str == null || str.equals("")) {
            LOGGER.error("No text provided for language detection...");
            return Language.UNKNOWN;
        }

        String lang;

        // Use detector to get lang
        try {
            Detector detector = DetectorFactory.create();
            detector.append(str);
            lang = detector.detect();
        } catch (Exception e) {
            LOGGER.error("Unable to detect language", e);
            return Language.UNKNOWN;
        }

        // Check value
        if (lang == null) {
            return Language.UNKNOWN;
        }

        // Language detection library may return complex languages code,
        // this fixes chinese support (Taiwan chinese not supported yet)
        if (lang.equals("zh-cn")) {
            lang = "zh";
        }

        // Check support and return Language representation
        if (Language.isSupported(lang)) {
            return Language.get(lang);
        }

        return Language.UNKNOWN;
    }
}
