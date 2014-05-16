package com.ulflander.mining.processors.extract;

import com.ulflander.AbstractTest;
import com.ulflander.app.model.TokenType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TokenSiblingsConsolidationTest extends AbstractTest {

    @Before
    public void setupProcessors () {
        s.addProcessor("extract.DocumentCleaner");
        s.addProcessor("extract.en.EnCommonAcronymsCleaner");
        s.addProcessor("extract.DocumentSplitter");
        s.addProcessor("extract.LanguageDetector");
        s.addProcessor("extract.POSTagger");
        s.addProcessor("extract.DocumentTokenizer");
        s.addProcessor("extract.TokenCounter");
        s.addProcessor("extract.TokenCleaner");
        s.addProcessor("extract.TokenCorpusGuesser");
        s.addProcessor("extract.TokenRegExpGuesser");
        s.addProcessor("extract.TokenSiblingsConsolidation");
        d.setSurface("My name is Maria Johns and I live in Paris, Texas.");
    }



    @Test
    public void personConsolidationTest () {

        s.submit(d);

        Assert.assertTrue("Fourth token should have a score of two for token type PERSON_PART",
                d.getTokenAt(0, 0, 0, 3).getScore(TokenType.PERSON_PART) >= 2);

        Assert.assertTrue("Fifth token should have a score of two for token type PERSON_PART",
                d.getTokenAt(0,0,0,4).getScore(TokenType.PERSON_PART) >= 2);
    }

}

