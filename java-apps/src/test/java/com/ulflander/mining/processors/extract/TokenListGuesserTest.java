package com.ulflander.mining.processors.extract;

import com.ulflander.AbstractTest;
import com.ulflander.app.model.TokenType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Ulflander on 4/14/14.
 */
public class TokenListGuesserTest extends AbstractTest {

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
        d.setRaw("My name is Maria Johns and I live in Paris, Texas.");
    }



    @Test
    public void personPartTest () {

        s.submit(d);
        trace (d);

        Assert.assertEquals("Fourth token should have a score of two for token type PERSON_PART",
                2, d.getTokenAt(0, 0, 0, 3).getScore(TokenType.PERSON_PART));

        Assert.assertEquals("Fifth token should have a score of two for token type PERSON_PART",
                2, d.getTokenAt(0,0,0,4).getScore(TokenType.PERSON_PART));
    }

}
