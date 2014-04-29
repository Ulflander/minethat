package com.ulflander.mining.processors.extract;


import com.ulflander.AbstractTest;
import com.ulflander.app.model.Language;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LanguageDetectorTest extends AbstractTest {

    @Before
    public void setupProcessors () {
        s.addProcessor("extract.DocumentCleaner");
        s.addProcessor("extract.DocumentSplitter");
        s.addProcessor("extract.LanguageDetector");
        s.addProcessor("extract.DocumentTokenizer");
        s.addProcessor("extract.TokenCounter");
        s.addProcessor("extract.TokenCleaner");
        s.addProcessor("extract.TokenRegExpGuesser");
    }

    @Test
    public void FrShortTest () {

        d.setRaw("Ce texte est manifestement en français. L'objectif est que la reconnaissance de langue soit efficace et que ces quelques mots soient considérés étant français.");
        s.submit(d);

        Assert.assertEquals("French text should be recognized as french text", Language.FR, d.getLanguage());
    }

    @Test
    public void EnShortTest () {

        d.setRaw("This text is obviously written in english. The goal is that language recognition be efficient and that those few words be considered as english speaking.");
        s.submit(d);

        Assert.assertEquals("English text should be recognized as english text", Language.EN, d.getLanguage());
    }

    @Test
    public void ItShortTest () {

        d.setRaw("Questo testo è ovviamente scritto in italiano. L'obiettivo è che il riconoscimento linguaggio sia efficiente e che quelle poche parole essere considerati lingua italiano.");
        s.submit(d);

        Assert.assertEquals("Italian text should be recognized as italian text", Language.IT, d.getLanguage());
    }

    @Test
    public void EsShortTest () {

        d.setRaw("Este texto es, obviamente, escrito en español. El objetivo es que el reconocimiento del lenguaje sea eficiente y que esas pocas palabras se considerarán como que habla español.");
        s.submit(d);

        Assert.assertEquals("Spanish text should be recognized as spanish text", Language.ES, d.getLanguage());
    }

    @Test
    public void DeShortTest () {

        d.setRaw("Dieser Text wird natürlich in Deutsch geschrieben. Das Ziel ist, dass die Spracherkennung effizient sein, und dass diese wenigen Worten wie Deutsch sprech berücksichtigt werden.");
        s.submit(d);

        Assert.assertEquals("German text should be recognized as german text", Language.DE, d.getLanguage());
    }

    @Test
    public void ZhCnShortTest () {

        d.setRaw("这段文字显然是德国人写的。我们的目标是，语言识别是有效的，而且这些几句话被认为是讲英语");
        s.submit(d);

        Assert.assertEquals("Chinese text should be recognized as chinese text", Language.ZH, d.getLanguage());
    }
}
