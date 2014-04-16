package com.ulflander.mining.processors.extract;

import com.ulflander.AbstractTest;
import org.junit.Assert;
import org.junit.Test;

public class TokenCleanerTest extends AbstractTest {



    @Test
    public void TokenCleanerSimpleTest () {
        s.addProcessor("extract.DocumentCleaner");
        s.addProcessor("extract.DocumentSplitter");
        s.addProcessor("extract.TokenCleaner");
        s.submit(d);

        Assert.assertEquals("With TokenCleaner the last token should be clean", "test", d.getTokenAt(0,0,0,3).getRaw( ));
    }



    @Test
    public void TokenCleanerComplexTest () {

        d.setRaw("Is this a -&{-&-(test)]!!!?");

        s.addProcessor("extract.DocumentCleaner");
        s.addProcessor("extract.DocumentSplitter");
        s.addProcessor("extract.TokenCleaner");
        s.submit(d);

        Assert.assertEquals("With TokenCleaner the last token should be clean", "test", d.getTokenAt(0, 0, 0, 3).getRaw( ));
    }
}
