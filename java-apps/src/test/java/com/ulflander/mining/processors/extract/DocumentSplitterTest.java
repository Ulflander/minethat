package com.ulflander.mining.processors.extract;

import com.ulflander.AbstractTest;
import com.ulflander.app.model.TextLength;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DocumentSplitterTest extends AbstractTest {

    @Before
    public void setupProcessors () {
        s.addProcessor("extract.DocumentCleaner");
        s.addProcessor("extract.DocumentSplitter");
        s.addProcessor("extract.LanguageDetector");
        s.addProcessor("extract.DocumentTokenizer");
        s.addProcessor("extract.TokenCounter");
    }

    @Test
    public void SpecialCaseNamesWithUniqueCapitalLetterTest () {
        d.setSurface("A sentence containing Edward E. Johns name should not count for two sentences.");
        s.submit(d);
        Assert.assertEquals("First paragraph should return one sentence", 1, d.getParagraphAt(0,0).getSentencesSize());

    }


    @Test
    public void DocumentTinyLengthTest () {
        d.setSurface("Tiny text");
        s.submit(d);
        Assert.assertEquals("Tiny text getTextLength should be tiny", TextLength.TINY, d.getTextLength());
    }

    @Test
    public void DocumentShortLengthTest () {
        d.setSurface("We can consider that this sentence is a rather long sentence out of any other consideration.");
        s.submit(d);
        Assert.assertEquals("Short text getTextLength should be short", TextLength.SHORT, d.getTextLength());
    }

    @Test
    public void DocumentMediumLengthTest () {
        String text = "";
        int i = 5;
        while (i > 0) {
            text += "We can consider that this text is a rather medium text out of any other consideration but the fact we repeat it 10x.";
            i--;
        }

        d.setSurface(text);
        s.submit(d);
        Assert.assertEquals("Medium text getTextLength should be medium", TextLength.MEDIUM, d.getTextLength());
    }


    @Test
    public void DocumentLongLengthTest () {
        String text = "";
        int i = 200;
        while (i > 0) {
            text += "We can consider that this text is a rather long text out of any other consideration but the fact we repeat it 100x.";
            i--;
        }

        d.setSurface(text);
        s.submit(d);
        Assert.assertEquals("Long text getTextLength should be long", TextLength.LONG, d.getTextLength());
    }

    @Test
    public void DocumentSplitterBasicTest () {
        s.submit(d);

        Assert.assertEquals("Simple sentence of 4 words should return only one chapter", 1, d.getChaptersSize());
        Assert.assertEquals("Simple sentence of 4 words should return only one paragraph from one chapter", 1, d.getChapterAt(0).getParagraphsSize());
        Assert.assertEquals("Simple sentence of 4 words should return only one sentence from one paragraph", 1, d.getParagraphAt(0, 0).getSentencesSize());
        Assert.assertEquals("Simple sentence of 4 words should return four com.ulflander.app.tokens from one sentence", 4, d.getSentenceAt(0, 0, 0).getTokensSize());
    }


    @Test
    public void DocumentSplitterParagraphTest () {

        d.setSurface("This paragraph contains two paragraphs.\n\nHere is the second one.");
        s.submit(d);

        Assert.assertEquals("Two paragraph should return only one chapter", 1, d.getChaptersSize());
        Assert.assertEquals("Two paragraph should return two paragraph", 2, d.getChapterAt(0).getParagraphsSize());
        Assert.assertEquals("First paragraph should return only one sentence", 1, d.getParagraphAt(0, 0).getSentencesSize());
        Assert.assertEquals("Second paragraph should return only one sentence", 1, d.getParagraphAt(0, 1).getSentencesSize());
    }


    @Test
    public void DocumentSplitter2SentencesTest () {

        d.setSurface("This paragraph contains two sentences. Here is the second one.");
        s.submit(d);

        Assert.assertEquals("Two sentence paragraph should return only one chapter", 1, d.getChaptersSize());
        Assert.assertEquals("Two sentence paragraph should return only one paragraph", 1, d.getChapterAt(0).getParagraphsSize());
        Assert.assertEquals("Two sentence paragraph should return two sentences", 2, d.getParagraphAt(0,0).getSentencesSize());
    }

    @Test
    public void DocumentSplitter3SentencesTest () {

        d.setSurface("This paragraph contains two sentences. SECOND ONE IS EMPHASIZED!!!!! Here is the third one.");
        s.submit(d);

        Assert.assertEquals("Two sentence paragraph should return only one chapter", 1, d.getChaptersSize());
        Assert.assertEquals("Two sentence paragraph should return only one paragraph", 1, d.getChapterAt(0).getParagraphsSize());
        Assert.assertEquals("Two sentence paragraph should return three sentences", 3, d.getParagraphAt(0,0).getSentencesSize());
    }



    @Test
    public void SetRawAndProcessingMulitpleTimesTest () {

        d.setSurface("This paragraph contains two paragraphs.\n\nHere is the second one.");
        s.submit(d);

        Assert.assertEquals("Two paragraph should return only one chapter", 1, d.getChaptersSize());
        Assert.assertEquals("Two paragraph should return two paragraph", 2, d.getChapterAt(0).getParagraphsSize());
        Assert.assertEquals("First paragraph should return only one sentence", 1, d.getParagraphAt(0,0).getSentencesSize());
        Assert.assertEquals("Second paragraph should return only one sentence", 1, d.getParagraphAt(0,1).getSentencesSize());

        d.setSurface("This paragraph contains two sentences. Here is the second one.");
        s.submit(d);

        Assert.assertEquals("Two sentence paragraph should return only one chapter", 1, d.getChaptersSize());
        Assert.assertEquals("Two sentence paragraph should return only one paragraph", 1, d.getChapterAt(0).getParagraphsSize());
        Assert.assertEquals("Two sentence paragraph should return two sentences", 2, d.getParagraphAt(0,0).getSentencesSize());
    }

    @Test
    public void ZhCnSentenceTest () {

        d.setSurface("这段文字显然是德国人写的。我们的目标是，语言识别是有效的，而且这些几句话被认为是讲英语。");
        s.submit(d);

        Assert.assertEquals("Two sentence paragraph should return only one chapter", 1, d.getChaptersSize());
        Assert.assertEquals("Two sentence paragraph should return only one paragraph", 1, d.getChapterAt(0).getParagraphsSize());
        Assert.assertEquals("Two sentence paragraph should return two sentences", 2, d.getParagraphAt(0, 0).getSentencesSize());
    }
}
