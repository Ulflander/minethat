package com.ulflander.mining.processors.extract;

import com.ulflander.AbstractTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Ulflander on 5/28/14.
 */
public class UnknownPersonToEntityTest extends AbstractTest {

    @Before
    public void setupProcessors () {
        s.addProcessor("extract.en.EnCommonAcronymsCleaner");
        s.addProcessor("extract.DocumentCleaner");
        s.addProcessor("extract.DocumentSplitter");
        s.addProcessor("extract.LanguageDetector");
        s.addProcessor("extract.POSTagger");
        s.addProcessor("extract.DocumentTokenizer");
        s.addProcessor("extract.TokenWeightConsolidation");
        s.addProcessor("extract.TokenCounter");
        s.addProcessor("extract.TokenCleaner");
        s.addProcessor("extract.TokenCorpusGuesser");
        s.addProcessor("extract.TokenRegExpGuesser");
        s.addProcessor("extract.AcronymExtractor");
        s.addProcessor("extract.en.EnTokenPOSConsolidation");
        s.addProcessor("extract.en.EnTokenSingularization");
        s.addProcessor("extract.TokenCorpusConsolidation");
        s.addProcessor("extract.TokenSiblingsConsolidation");
        s.addProcessor("extract.TokenInferConsolidation");
        s.addProcessor("extract.en.EnOperatorBasedConsolidation");
        s.addProcessor("extract.TokenAggregator");
        s.addProcessor("extract.AggregatedCorpusGuesser");
        s.addProcessor("extract.EntityLookup");
        s.addProcessor("extract.EntityBasedAggregator");
        s.addProcessor("extract.EntityConsolidation");
        s.addProcessor("extract.UnknownPersonToEntity");
        s.addProcessor("extract.EntityCleaner");
        s.addProcessor("extract.EntitySelector");
        s.addProcessor("extract.TokenFrequency");
        s.addProcessor("extract.KeywordSelector");
        s.addProcessor("augment.BasicTextStat");
        s.addProcessor("augment.QualityEvaluator");
    }

    @Test
    public void test1() {
        d.setSurface("Jack Bauer is really a good man. Jack will save us!");
        s.submit(d);

        trace (d);
        Assert.assertNotEquals("Jack Bauer should be an entity", null,
                d.getTokenAt(0,0,0,0));

        Assert.assertEquals("Jack should be known as Bauer",
                d.getTokenAt(0, 0, 0, 0).getEntity().getId(),
                d.getTokenAt(0, 0, 1, 0).getEntity().getId());
    }
}
