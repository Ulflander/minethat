package com.ulflander.mining.processors.extract;

import com.ulflander.AbstractTest;
import com.ulflander.application.model.Document;
import com.ulflander.application.model.Paragraph;
import com.ulflander.application.model.Sentence;
import com.ulflander.application.model.Token;
import com.ulflander.application.model.TokenType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

public class POSTaggerTest extends AbstractTest {

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
        s.addProcessor("extract.en.EnTokenPOSConsolidation");
        s.addProcessor("extract.TokenSiblingsConsolidation");
        s.addProcessor("extract.TokenCorpusConsolidation");
        s.addProcessor("extract.TokenInferConsolidation");
        s.addProcessor("extract.TokenFrequency");
        s.addProcessor("extract.KeywordSelector");
        s.addProcessor("augment.BasicTextStat");
    }

    @Test
    public void basicTest () {
        d.setRaw("Pierre E. Gentil, 61 years old, will join the board of Vinci as a "
                + "non-executive director in the USA office on Nov. 29, his Twitter id is @pierre_vinken and "
                + "he was born in Paris, France in 1954. Paris Hilton will give a speech for a salary of $10 millions to the board of Vinci for that purpose. "
                + "The city has one of the largest GDPs in the world, €607 billion (US$845 billion) as of 2011. MOSCOW — President Vladimir V Putin of Russia emphasized on Thursday that the upper chamber of the Russian Parliament had authorized him to use military force if necessary in eastern Ukraine, and also stressed Russia’s historical claim to the territory, repeatedly referring to it as “new Russia” and saying that only “God knows” why it became part of Ukraine. By ANDREW E. KRAMER\n" +
                "The firefight in the city of Mariupol, the most lethal so far in eastern Ukraine, underscored the worsening security situation a day after government forces were humiliated by militants. " +
                "There are several Ukrainian units on the peninsula, including three anti-aircraft regiments, the 204th Tactical Aviation Brigade and Ukraine’s navy headquarters. These forces are no match for Russian troops." +
                " According to Reuters, Ukraine’s military is on “full combat alert.”");

        s.submit(d);
        trace (d);
    }

    @Test
    public void entitiesTest () {
        Document d = new Document();
        d.setRaw("U.S. rebukes Iran’s U.N. envoy pick over 1979 embassy attack\n\nStoned mom avoids jail after driving 12 miles with baby on roof\n\nMore than 100 ‘inappropriate’ encounters between NYC school staffers, students since 2009: report\n\nJoe Biden to Boston bombing survivors: ‘America will never, ever stand down’\n\nFBI failed to throughly vet Boston bombing suspect after Russian lead, report finds");
        s.submit(d);
        trace(d);
    }

    private void trace(Document d) {

        System.out.println("-------------\n");

        for (String group: d.getPropertyGroups()) {
            for (String s: d.getProperties(group).keySet()) {
                System.out.println(group+"."+s+"="+d.getProperty(group, s));
            }
        }

        System.out.println();
        System.out.println();
        for (Paragraph p : d.getChapterAt(0).getParagraphs()) {
            for (Sentence s: p.getSentences()) {

                for (Token t: s.getTokens()) {
                    System.out.println("Token " + t.getRaw() + " / " + t.getClean()
                            + "(" + t.getTag() + "/" + t.getType() + ")");

                    for (TokenType tt : t.getScores().keySet()) {
                        System.out.println("        " + tt + ": " + t.getScore(tt));
                    }

                    System.out.println();
                }

                Assert.assertTrue("Sentence should have a raw part of speech",
                        d.getSentenceAt(0,0,0).getRawPartOfSpeech() != null);
                System.out.println("---");
            }
        }
        System.out.println();
        System.out.println();

        HashMap<Token, Integer> freq = d.getFrequency();

        if (freq != null) {
            for (Token t: freq.keySet()) {
                System.out.println(freq.get(t) + " " + t.getRaw() + " (" + t.getTag() + "/" +
                        t.getType() + ")");

                for (TokenType tt : t.getScores().keySet()) {
                    System.out.println("        " + tt + ": " + t.getScore(tt));
                }
            }
        }

        System.out.println("\n-------------\n");
    }



}
