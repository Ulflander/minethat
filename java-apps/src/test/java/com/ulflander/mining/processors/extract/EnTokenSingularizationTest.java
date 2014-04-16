package com.ulflander.mining.processors.extract;

import com.ulflander.AbstractTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class EnTokenSingularizationTest extends AbstractTest {


    @Before
    public void setupProcessors () {
        s.addProcessor("extract.DocumentCleaner");
        s.addProcessor("extract.DocumentSplitter");
        s.addProcessor("extract.LanguageDetector");
        s.addProcessor("extract.TokenCleaner");
        s.addProcessor("extract.TokenRegExpGuesser");
        s.addProcessor("extract.en.EnTokenSingularization");
    }


    @Test
    public void TokenSingularizationSimpleTest () {


        d.setRaw("words movies horses uses theses houses wives");
        s.submit(d);

        Assert.assertEquals("First token should have singular 'word'", "word", d.getTokenAt(0,0,0,0).getSingular());
        Assert.assertEquals("Second token should have singular 'movie'", "movie", d.getTokenAt(0,0,0,1).getSingular());
        Assert.assertEquals("Third token should have singular 'horse'", "horse", d.getTokenAt(0,0,0,2).getSingular());
        Assert.assertEquals("Fourth token should have singular 'use'", "use", d.getTokenAt(0,0,0,3).getSingular());
        Assert.assertEquals("Fifth token should have singular 'thesis'", "thesis", d.getTokenAt(0,0,0,4).getSingular());
        Assert.assertEquals("Sixth token should have singular 'house'", "house", d.getTokenAt(0,0,0,5).getSingular());
        Assert.assertEquals("Seventh token should have singular 'wife'", "wife", d.getTokenAt(0,0,0,6).getSingular());
    }

    @Test
    public void TokenSingularizationIsPluralTest () {


        d.setRaw("Word words is plural");
        s.submit(d);

        Assert.assertEquals("Second token should be considered as plural", true, d.getTokenAt(0,0,0,1).isPlural());
    }

    @Test
    public void TokenSingularizationNotPluralTest () {


        d.setRaw("Word words is plural");
        s.submit(d);

        Assert.assertEquals("First token shouldn't be considered as plural", false, d.getTokenAt(0,0,0,0).isPlural());
    }

    @Test
    public void TokenSingularizationPeopleTest () {


        d.setRaw("People is plural of person");
        s.submit(d);

        Assert.assertEquals("First token 'People' should have singular 'person'", "person", d.getTokenAt(0,0,0,0).getSingular());
    }
}
