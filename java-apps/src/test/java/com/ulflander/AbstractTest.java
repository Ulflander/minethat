package com.ulflander;

import com.ulflander.app.model.Document;
import com.ulflander.app.model.Paragraph;
import com.ulflander.app.model.Sentence;
import com.ulflander.app.model.Token;
import com.ulflander.app.model.TokenType;
import org.junit.Assert;
import org.junit.Before;

import java.util.HashMap;

public class AbstractTest {

    protected MinethatTestService s;
    protected Document d;

    @Before
    public final void initialize () {
        s = new MinethatTestService();
        s.run();

        d = new Document("This is a test.");
    }

    public void trace(Document d) {

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
                        d.getSentenceAt(0, 0, 0).getRawPartOfSpeech() != null);
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
