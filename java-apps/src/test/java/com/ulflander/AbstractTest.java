package com.ulflander;

import com.ulflander.app.model.Document;
import com.ulflander.app.model.Paragraph;
import com.ulflander.app.model.Sentence;
import com.ulflander.app.model.Token;
import com.ulflander.app.model.TokenType;
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
            System.out.println(p.getSurface());
            for (Sentence s: p.getSentences()) {

                for (Token t: s.getTokens()) {
                    System.out.println("Token " + t.getSurface() + " / " + t.getClean()
                            + " (" + t.getTag() + "/" + t.getType() + "/"+t.getWeight()+") " + t.getSingular());

                    for (TokenType tt : t.getScores().keySet()) {
                        System.out.println("        " + tt + ": " + t.getScore(tt));
                    }

                    System.out.println();
                }

                System.out.println("POS: " + s.getRawPartOfSpeech());
                System.out.println("---");
            }
            System.out.println(" ******** ");
        }
        System.out.println();
        System.out.println();

        HashMap<Token, Integer> freq = d.getFrequency();

        if (freq != null) {
            for (Token t: freq.keySet()) {
                System.out.println(freq.get(t) + " " + t.getSurface() + " (" + t.getTag() + "/" +
                        t.getType() + ")");

                for (TokenType tt : t.getScores().keySet()) {
                    System.out.println("        " + tt + ": " + t.getScore(tt));
                }
            }
        }

        System.out.println("\n-------------\n");
    }

}
