package com.ulflander.mining.processors.preset;

import com.ulflander.app.model.JobProcessor;

import java.util.ArrayList;

/**
 * A preset that only split document and returns some basic statistics about
 * the document.
 *
 * @see com.ulflander.mining.processors.augment.BasicTextStat
 *
 * @author Ulflander <xlaumonier@gmail.com>
 * @since 2/23/14
 */
public final class BasicPreset implements IPreset {

    /**
     * Returns processors needed for extracting some statistics on text.
     *
     * @return An array of model processors
     */
    @Override
    public ArrayList<JobProcessor> getProcessors() {
        ArrayList<JobProcessor> procs = new ArrayList<JobProcessor>();

        /**
         * First and before anything, bit of cleanup: mainly some newlines and
         * space characters replacement so separators are coherent.
         */
        procs.add(new JobProcessor("extract.DocumentCleaner"));

        /**
         * Mainly useful for news articles: replace some common journos
         * acronyms so we don't deal with too much of them.
         */
        procs.add(new JobProcessor("extract.en.EnCommonAcronymsCleaner"));

        /**
         * Split document into chapters/paragraphs/sentences.
         */
        procs.add(new JobProcessor("extract.DocumentSplitter"));


        /**
         * Detect language at sentence and document level.
         */
        procs.add(new JobProcessor("extract.LanguageDetector"));
        procs.add(new JobProcessor("extract.POSTagger"));
        procs.add(new JobProcessor("extract.DocumentTokenizer"));
        procs.add(new JobProcessor("extract.TokenWeightConsolidation"));
        procs.add(new JobProcessor("extract.TokenCounter"));
        procs.add(new JobProcessor("extract.TokenCleaner"));
        procs.add(new JobProcessor("extract.en.EnTokenSingularization"));
        procs.add(new JobProcessor("extract.TokenCorpusGuesser"));
        procs.add(new JobProcessor("extract.TokenRegExpGuesser"));
        procs.add(new JobProcessor("extract.en.ENTokenPOSConsolidation"));
        procs.add(new JobProcessor("extract.TokenCorpusConsolidation"));
        procs.add(new JobProcessor("extract.TokenSiblingsConsolidation"));
        procs.add(new JobProcessor("extract.TokenInferConsolidation"));
        procs.add(new JobProcessor("extract.en.EnOperatorBasedConsolidation"));
        procs.add(new JobProcessor("extract.TokenFrequency"));
        procs.add(new JobProcessor("extract.TokenAggregator"));

        procs.add(new JobProcessor("augment.BasicTextStat"));
        procs.add(new JobProcessor("augment.social.SocialStats"));
        procs.add(new JobProcessor("augment.geoloc.MaxMindIPExtraction"));


        return procs;
    }
}
