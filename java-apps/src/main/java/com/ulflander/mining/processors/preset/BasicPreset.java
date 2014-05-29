package com.ulflander.mining.processors.preset;

import com.ulflander.app.model.JobProcessor;
import com.ulflander.mining.processors.augment.BasicTextStat;
import com.ulflander.mining.processors.augment.QualityEvaluator;
import com.ulflander.mining.processors.augment.geoloc.MaxMindIPExtraction;
import com.ulflander.mining.processors.augment.social.SocialStats;
import com.ulflander.mining.processors.extract.AggregatedCorpusGuesser;
import com.ulflander.mining.processors.extract.DocumentCleaner;
import com.ulflander.mining.processors.extract.DocumentSplitter;
import com.ulflander.mining.processors.extract.DocumentTokenizer;
import com.ulflander.mining.processors.extract.EntityBasedAggregator;
import com.ulflander.mining.processors.extract.EntityCleaner;
import com.ulflander.mining.processors.extract.EntityConsolidation;
import com.ulflander.mining.processors.extract.EntityLookup;
import com.ulflander.mining.processors.extract.EntitySelector;
import com.ulflander.mining.processors.extract.KeywordSelector;
import com.ulflander.mining.processors.extract.LanguageDetector;
import com.ulflander.mining.processors.extract.MetaChecker;
import com.ulflander.mining.processors.extract.POSTagger;
import com.ulflander.mining.processors.extract.TokenAggregator;
import com.ulflander.mining.processors.extract.TokenCleaner;
import com.ulflander.mining.processors.extract.TokenCorpusConsolidation;
import com.ulflander.mining.processors.extract.TokenCorpusGuesser;
import com.ulflander.mining.processors.extract.TokenCounter;
import com.ulflander.mining.processors.extract.TokenFrequency;
import com.ulflander.mining.processors.extract.TokenInferConsolidation;
import com.ulflander.mining.processors.extract.TokenRegExpGuesser;
import com.ulflander.mining.processors.extract.TokenSiblingsConsolidation;
import com.ulflander.mining.processors.extract.TokenWeightConsolidation;
import com.ulflander.mining.processors.extract.UnknownPersonToEntity;
import com.ulflander.mining.processors.extract.en.EnCommonAcronymsCleaner;
import com.ulflander.mining.processors.extract.en.EnOperatorBasedConsolidation;
import com.ulflander.mining.processors.extract.en.EnTokenPOSConsolidation;
import com.ulflander.mining.processors.extract.en.EnTokenSingularization;

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
         * FIRST GLOBAL PROCESS
         */

        // Validates meta tags.
        procs.add(new JobProcessor(MetaChecker.class));

        //Mainly useful for news articles: replace some common journos
        // acronyms so we don't deal with too much of them.
        procs.add(new JobProcessor(EnCommonAcronymsCleaner.class));

        // Bit of cleanup: mainly some newlines and
        // space characters replacement so separators are coherent.
        procs.add(new JobProcessor(DocumentCleaner.class));

        // Split document into chapters/paragraphs/sentences.
        procs.add(new JobProcessor(DocumentSplitter.class));

        // Detect language at sentence and document level.
        procs.add(new JobProcessor(LanguageDetector.class));

        // Part Of Speech tagging.
        procs.add(new JobProcessor(POSTagger.class));


        /**
         * SECOND STEP: TOKENIZATION & TOKEN ANALYSIS
         */

        // Tokenization (and run Stanford NER as well)
        procs.add(new JobProcessor(DocumentTokenizer.class));

        // Set weight on tokens.
        procs.add(new JobProcessor(TokenWeightConsolidation.class));

        // Count simple tokens.
        procs.add(new JobProcessor(TokenCounter.class));

        // Clean tokens
        procs.add(new JobProcessor(TokenCleaner.class));

        // Singularize tokens
        procs.add(new JobProcessor(EnTokenSingularization.class));

        // Search token in corpuses
        procs.add(new JobProcessor(TokenCorpusGuesser.class));

        // Apply regexp recognition on token
        procs.add(new JobProcessor(TokenRegExpGuesser.class));

        // Consolidation of TokenType based on Port-Of-Speech data
        procs.add(new JobProcessor(EnTokenPOSConsolidation.class));

        // Consolidate token type given some specific corpuses
        procs.add(new JobProcessor(TokenCorpusConsolidation.class));

        // Console sibling similar token types
        procs.add(new JobProcessor(TokenSiblingsConsolidation.class));

        // Infer token types on tokens based on surrounding token types
        procs.add(new JobProcessor(TokenInferConsolidation.class));

        // Consolidate token type based on English operators (in, from...)
        procs.add(new JobProcessor(EnOperatorBasedConsolidation.class));

        // Aggregate token together by using similar token types
        procs.add(new JobProcessor(TokenAggregator.class));

        // Run corpus guessing on aggregated tokens
        procs.add(new JobProcessor(AggregatedCorpusGuesser.class));


        /**
         * THIRD STEP: ENTITY RECOGNITION
         */

        // Entity lookup.
        procs.add(new JobProcessor(EntityLookup.class));

        // Token aggregation based on entity
        procs.add(new JobProcessor(EntityBasedAggregator.class));

        // Entity consolidation.
        procs.add(new JobProcessor(EntityConsolidation.class));

        // Link person parts "... Jack is...." with
        // potential persons "Jack Bauer"
        procs.add(new JobProcessor(UnknownPersonToEntity.class));

        // Cleanup entities under threshold or with insufficient data
        procs.add(new JobProcessor(EntityCleaner.class));

        // Select chosen entities
        procs.add(new JobProcessor(EntitySelector.class));

        /**
         * FOURTH STEP: CREATE METADATA
         */

        // How muchg frequent are tokens?
        procs.add(new JobProcessor(TokenFrequency.class));

        // What are the text keywords?
        procs.add(new JobProcessor(KeywordSelector.class));

        // What are the text basic statistics (text lenght, amount of tokens...)
        procs.add(new JobProcessor(BasicTextStat.class));

        // How many times this web page has been shared?
        procs.add(new JobProcessor(SocialStats.class));

        // If some IPs found, try to geolocate them
        procs.add(new JobProcessor(MaxMindIPExtraction.class));

        // Evaluates quality of document and extraction based on various
        // variables
        procs.add(new JobProcessor(QualityEvaluator.class));


        return procs;
    }
}
