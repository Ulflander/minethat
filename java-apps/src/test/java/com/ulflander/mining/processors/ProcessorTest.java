package com.ulflander.mining.processors;

import com.ulflander.AbstractTest;
import org.junit.Assert;
import org.junit.Test;

public class ProcessorTest extends AbstractTest {

    @Test
    public void DuplicateProcessorTest () {

        s.addProcessor("extract.DocumentSplitter");
        s.addProcessor("extract.DocumentSplitter");

        s.submit(d);

        Assert.assertEquals("Adding twice the same processor shouldn't be allowed", 1, s.getProcessorsSize( ));
    }

    @Test
    public void LanguageSpecificProcessorTest () {
        d.setSurface("Ce texte est manifestement en français. L'objectif est que la reconnaissance de langue soit efficace et que ces quelques mots soient considérés étant français.");

        s.addProcessor("extract.DocumentCleaner");
        s.addProcessor("extract.DocumentSplitter");
        s.addProcessor("extract.LanguageDetector");
        s.addProcessor("extract.DocumentTokenizer");
        s.addProcessor("extract.TokenCleaner");
        s.addProcessor("extract.TokenCorpusGuesser");
        s.addProcessor("extract.TokenRegExpGuesser");
        s.addProcessor("extract.en.EnTokenSingularization");

        s.submit(d);

        Assert.assertEquals("Processor dedicated to english language shouldnt run on french text", false, d.getHistory().contains("extract.en.EnTokenSingularization"));
    }

    @Test
    public void GoodLanguageSpecificProcessorTest () {
        d.setSurface("This text is obviously written in english. The goal is that language recognition be efficient and that those few words be considered as english speaking.");

        s.addProcessor("extract.DocumentCleaner");
        s.addProcessor("extract.DocumentSplitter");
        s.addProcessor("extract.LanguageDetector");
        s.addProcessor("extract.DocumentTokenizer");
        s.addProcessor("extract.TokenCleaner");
        s.addProcessor("extract.TokenCorpusGuesser");
        s.addProcessor("extract.TokenRegExpGuesser");
        s.addProcessor("extract.en.EnTokenSingularization");

        s.submit(d);

        Assert.assertEquals("Processor dedicated to english language should run on english text", true, d.getHistory().contains("extract.en.EnTokenSingularization"));
    }
}
