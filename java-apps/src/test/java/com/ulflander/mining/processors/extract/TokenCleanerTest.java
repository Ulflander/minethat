package com.ulflander.mining.processors.extract;

import com.ulflander.AbstractTest;
import org.junit.Assert;
import org.junit.Test;

public class TokenCleanerTest extends AbstractTest {



    @Test
    public void TokenCleanerSimpleTest () {
        s.addProcessor("extract.DocumentCleaner");
        s.addProcessor("extract.DocumentSplitter");
        s.addProcessor("extract.LanguageDetector");
        s.addProcessor("extract.DocumentTokenizer");
        s.addProcessor("extract.TokenCounter");
        s.addProcessor("extract.TokenCleaner");
        s.submit(d);

        Assert.assertEquals("With TokenCleaner the last token should be clean", "test", d.getTokenAt(0,0,0,3).getSurface());
    }



    @Test
    public void TokenCleanerComplexTest () {

        d.setSurface("Is this a -&{-&-(test)]!!!?");

        s.addProcessor("extract.DocumentCleaner");
        s.addProcessor("extract.DocumentSplitter");
        s.addProcessor("extract.LanguageDetector");
        s.addProcessor("extract.DocumentTokenizer");
        s.addProcessor("extract.TokenCounter");
        s.addProcessor("extract.TokenCleaner");
        s.submit(d);

        Assert.assertEquals("With TokenCleaner the last token should be clean", "test", d.getTokenAt(0, 0, 0, 3).getSurface());
    }
}
