package com.ulflander.mining.processors.augment;


import com.ulflander.AbstractTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BasicTextStatTest extends AbstractTest {

    @Before
    public void setupProcessors () {
        s.addProcessor("extract.DocumentCleaner");
        s.addProcessor("extract.DocumentSplitter");
        s.addProcessor("extract.TokenCleaner");
        s.addProcessor("extract.TokenRegExpGuesser");
        s.addProcessor("extract.LanguageDetector");
        s.addProcessor("augment.BasicTextStat");
    }

    @Test
    public void BasicTest () {

        d.setRaw("Ce texte est manifestement en français. L'objectif est que la reconnaissance de langue soit efficace et que ces quelques mots soient considérés étant français.");
        s.submit(d);

        Assert.assertEquals("Result should be available", true, d.getProperties("basic_stats").size() > 0);
    }

    @Test
    public void toStringTest () {

        d.setRaw("Ce texte est manifestement en français. L'objectif est que la reconnaissance de langue soit efficace et que ces quelques mots soient considérés étant français.");
        s.submit(d);

        Assert.assertNotSame("Result should be available", "",
                d.getProperties("basic_stats").get("text_length"));
    }

}
