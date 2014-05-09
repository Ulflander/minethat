package com.ulflander.mining.processors.extract;

import com.ulflander.AbstractTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TextCapUpperTest extends AbstractTest {


    @Before
    public void setupProcessors () {
        s.addProcessor("extract.DocumentCleaner");
        s.addProcessor("extract.DocumentSplitter");
        s.addProcessor("extract.LanguageDetector");
        s.addProcessor("extract.DocumentTokenizer");
        s.addProcessor("extract.TokenCounter");
        s.addProcessor("extract.TokenCleaner");
    }


    @Test
    public void DocumentCapUpperTest () {
        s.submit(d);

        Assert.assertEquals("Default test sentence should be capitalized", true, d.isCapitalized());
        Assert.assertEquals("Default test sentence shouldn't be uppercased", false, d.isUppercased());
    }

    @Test
    public void ChapterCapUpperTest () {
        s.submit(d);

        Assert.assertEquals("Default test sentence first chapter should be capitalized", true, d.getChapterAt(0).isCapitalized());
        Assert.assertEquals("Default test sentence first chapter shouldn't be uppercased", false, d.getChapterAt(0).isUppercased());
    }

    @Test
    public void ParagrapCapUpperTest () {
        s.submit(d);

        Assert.assertEquals("Default test sentence first paragraph should be capitalized", true, d.getParagraphAt(0, 0).isCapitalized());
        Assert.assertEquals("Default test sentence first paragraph shouldn't be uppercased", false, d.getParagraphAt(0, 0).isUppercased());
    }

    @Test
    public void SentenceCapUpperTest () {
        s.submit(d);

        Assert.assertEquals("Default test sentence first sentence should be capitalized", true, d.getSentenceAt(0, 0, 0).isCapitalized());
        Assert.assertEquals("Default test sentence first sentence shouldn't be uppercased", false, d.getSentenceAt(0, 0, 0).isUppercased());
    }

    @Test
    public void TokenCapUpperTest () {
        s.submit(d);

        Assert.assertEquals("Default test sentence first token should be capitalized", true, d.getTokenAt(0, 0, 0, 0).isCapitalized());
        Assert.assertEquals("Default test sentence first token shouldn't be uppercased", false, d.getTokenAt(0, 0, 0, 0).isUppercased());
    }

    @Test
    public void SecondTokenCapUpperTest () {
        s.submit(d);

        Assert.assertEquals("Default test sentence second token should be capitalized", false, d.getTokenAt(0, 0, 0, 1).isCapitalized());
        Assert.assertEquals("Default test sentence second token shouldn't be uppercased", false, d.getTokenAt(0, 0, 0, 1).isUppercased());
    }


    @Test
    public void UppercasedDocumentCapUpperTest () {

        d.setSurface("THIS IS AN UPPERCASED SENTENCE.");
        s.submit(d);

        Assert.assertEquals("Uppercased test sentence should be capitalized", true, d.isCapitalized());
        Assert.assertEquals("Uppercased test sentence should be uppercased", true, d.isUppercased());
    }

    @Test
    public void UppercasedChapterCapUpperTest () {

        d.setSurface("THIS IS AN UPPERCASED SENTENCE.");
        s.submit(d);

        Assert.assertEquals("Uppercased test sentence first chapter should be capitalized", true, d.getChapterAt(0).isCapitalized());
        Assert.assertEquals("Uppercased test sentence first chapter should be uppercased", true, d.getChapterAt(0).isUppercased());
    }

    @Test
    public void UppercasedParagrapCapUpperTest () {

        d.setSurface("THIS IS AN UPPERCASED SENTENCE.");
        s.submit(d);

        Assert.assertEquals("Uppercased test sentence first paragraph should be capitalized", true, d.getParagraphAt(0, 0).isCapitalized());
        Assert.assertEquals("Uppercased test sentence first paragraph should be uppercased", true, d.getParagraphAt(0, 0).isUppercased());
    }

    @Test
    public void UppercasedSentenceCapUpperTest () {

        d.setSurface("THIS IS AN UPPERCASED SENTENCE.");
        s.submit(d);

        Assert.assertEquals("Uppercased test sentence first sentence should be capitalized", true, d.getSentenceAt(0, 0, 0).isCapitalized());
        Assert.assertEquals("Uppercased test sentence first sentence should be uppercased", true, d.getSentenceAt(0, 0, 0).isUppercased());
    }

    @Test
    public void UppercasedTokenCapUpperTest () {

        d.setSurface("THIS IS AN UPPERCASED SENTENCE.");
        s.submit(d);

        Assert.assertEquals("Uppercased test sentence first token should be capitalized", true, d.getTokenAt(0, 0, 0, 0).isCapitalized());
        Assert.assertEquals("Uppercased test sentence first token should be uppercased", true, d.getTokenAt(0, 0, 0, 0).isUppercased());
    }

    @Test
    public void UppercasedSecondTokenCapUpperTest () {

        d.setSurface("THIS IS AN UPPERCASED SENTENCE.");
        s.submit(d);

        Assert.assertEquals("Uppercased test sentence second token should be capitalized", true, d.getTokenAt(0, 0, 0, 1).isCapitalized());
        Assert.assertEquals("Uppercased test sentence second token should be uppercased", true, d.getTokenAt(0, 0, 0, 1).isUppercased());
    }


}
