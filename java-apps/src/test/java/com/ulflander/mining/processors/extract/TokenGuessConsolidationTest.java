package com.ulflander.mining.processors.extract;

import com.ulflander.AbstractTest;
import com.ulflander.application.model.TokenType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TokenGuessConsolidationTest extends AbstractTest {

    @Before
    public void setupProcessors () {
        s.addProcessor("extract.DocumentCleaner");
        s.addProcessor("extract.DocumentSplitter");
        s.addProcessor("extract.LanguageDetector");
        s.addProcessor("extract.TokenCleaner");
        s.addProcessor("extract.TokenListGuesser");
        s.addProcessor("extract.TokenGuessConsolidation");
        d.setRaw("My name is Maria Johns and I live in Paris, Texas.");
    }



    @Test
    public void personConsolidationTest () {

        s.submit(d);

        Assert.assertEquals("Fourth token should have a score of two for token type PERSON_PART",
                3, d.getTokenAt(0, 0, 0, 3).getScore(TokenType.PERSON_PART));

        Assert.assertEquals("Fifth token should have a score of two for token type PERSON_PART",
                3, d.getTokenAt(0,0,0,4).getScore(TokenType.PERSON_PART));
    }

}

