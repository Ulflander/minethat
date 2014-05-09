package com.ulflander.mining.processors.extract;

import com.ulflander.AbstractTest;
import com.ulflander.app.model.TokenType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class TokenRegExpGuesserTest extends AbstractTest {

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
        d.setSurface("My IP is 192.168.0.0");
    }

    @Test
    public void IPSplitTest () {

        s.submit(d);

        Assert.assertEquals("Simple sentence of 4 words with IP should return only one chapter", 1, d.getChaptersSize());
        Assert.assertEquals("Simple sentence of 4 words with IP should return only one paragraph from one chapter", 1, d.getChapterAt(0).getParagraphsSize());
        Assert.assertEquals("Simple sentence of 4 words with IP should return only one sentence from one paragraph", 1, d.getParagraphAt(0, 0).getSentencesSize());
        Assert.assertEquals("Simple sentence of 4 words with IP should return four tokens from one sentence", 4, d.getSentenceAt(0, 0, 0).getTokensSize());
        Assert.assertEquals("Simple sentence of 4 words with IP should return IP as fourth token", "192.168.0.0", d.getTokenAt(0, 0, 0, 3).getSurface());
    }

    @Test
    public void IPAtEndOfSentenceSplitTest () {

        d.setSurface("My IP is 192.168.0.0.");
        s.submit(d);

        Assert.assertEquals("Simple sentence of 4 words with IP should return only one chapter", 1, d.getChaptersSize());
        Assert.assertEquals("Simple sentence of 4 words with IP should return only one paragraph from one chapter", 1, d.getChapterAt(0).getParagraphsSize());
        Assert.assertEquals("Simple sentence of 4 words with IP should return only one sentence from one paragraph", 1, d.getParagraphAt(0, 0).getSentencesSize());
        Assert.assertEquals("Simple sentence of 4 words with IP should return four tokens from one sentence", 4, d.getSentenceAt(0, 0, 0).getTokensSize());
        Assert.assertEquals("Simple sentence of 4 words with IP should return IP as fourth token", "192.168.0.0", d.getTokenAt(0, 0, 0, 3).getSurface());
    }

    @Test
    public void IPTokenTypeTest () {
        s.submit(d);
        Assert.assertEquals("Simple sentence of 4 words with IP should return IP as fourth token kind",
                5, d.getTokenAt(0, 0, 0, 3).getScore(TokenType.IPV4));
    }

    @Test
    public void Bad1IPTokenTypeTest () {
        d.setSurface("My IP is 192.196.0");

        s.submit(d);
        Assert.assertEquals("Bad IP shouldn't return IP as fourth token kind",
                false, d.getTokenAt(0, 0, 0, 3).hasScore(TokenType.IPV4));
    }

    @Test
    public void Bad2IPTokenTypeTest () {
        d.setSurface("My IP is 192.168.0.a");

        s.submit(d);
        Assert.assertEquals("Bad IP shouldn't return IP as fourth token kind",
                false, d.getTokenAt(0, 0, 0, 3).hasScore(TokenType.IPV4));
    }

    @Test
    public void Bad3IPTokenTypeTest () {
        d.setSurface("My IP is 2222.22.22.999");

        s.submit(d);
        Assert.assertEquals("Bad IP shouldn't return IP as fourth token kind",
                false, d.getTokenAt(0, 0, 0, 3).hasScore(TokenType.IPV4));
    }


    @Test
    public void EmailTokenTypeTest () {
        d.setSurface("My IP is xlaumonier@fmail.com");
        s.submit(d);
        Assert.assertEquals("Simple sentence of 4 words with email" +
                " should return email as fourth token kind",
                5, d.getTokenAt(0, 0, 0, 3).getScore(TokenType.EMAIL));
    }


    @Test
    public void HashTagTokenTypeTest () {
        d.setSurface("My hash is #hash");
        s.submit(d);
        Assert.assertEquals("Simple sentence of 4 words with hashtag" +
                " should return hashtag as fourth token kind",
                5, d.getTokenAt(0, 0, 0, 3).getScore(TokenType.HASHTAG));
    }


    @Test
    public void HashTagComplexTokenTypeTest () {
        d.setSurface("My hash is #Hash_08_tag");
        s.submit(d);
        Assert.assertEquals("Simple sentence of 4 words with hashtag" +
                " should return hashtag as fourth token kind",
                5, d.getTokenAt(0, 0, 0, 3).getScore(TokenType.HASHTAG));
    }


    @Test
    public void twitterUsernameTokenTypeTest () {
        d.setSurface("My hash is @xav");
        s.submit(d);
        Assert.assertEquals("Simple sentence of 4 words with twitter username" +
                " should return twitter username as fourth token kind",
                5, d.getTokenAt(0, 0, 0, 3).getScore(TokenType.TWITTER_USERNAME));
    }
}
