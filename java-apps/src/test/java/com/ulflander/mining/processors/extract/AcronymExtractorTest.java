package com.ulflander.mining.processors.extract;

import com.ulflander.AbstractTest;
import com.ulflander.app.model.TokenType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Ulflander on 4/30/14.
 */
public class AcronymExtractorTest extends AbstractTest {

    @Before
    public void initProcs(){
        s.addProcessor("extract.DocumentCleaner");
        s.addProcessor("extract.en.EnCommonAcronymsCleaner");
        s.addProcessor("extract.DocumentSplitter");
        s.addProcessor("extract.LanguageDetector");
        s.addProcessor("extract.POSTagger");
        s.addProcessor("extract.DocumentTokenizer");
        s.addProcessor("extract.TokenWeightConsolidation");
        s.addProcessor("extract.TokenCounter");
        s.addProcessor("extract.TokenCleaner");
        s.addProcessor("extract.AcronymExtractor");
    }


    @Test
    public void testExtractionSimple() {

        d.setSurface("Here is an acrynom: ATO, and its meaning: Anti Terrorist Operations.");

        s.submit(d);

        Assert.assertEquals(true, d.getTokenAt(0,0,0,5).hasScore(TokenType.ACRONYM));
    }


}
