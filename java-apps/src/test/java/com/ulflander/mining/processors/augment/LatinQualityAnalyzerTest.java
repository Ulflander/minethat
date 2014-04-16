package com.ulflander.mining.processors.augment;

public class LatinQualityAnalyzerTest {

    public void commentTest1 () {

        // BAD COMMENT:
        // No capitalization at beginning
        // Low quantity of punctuation
        // dont, didnt... vs good writing
        // oprah, rachael not capitalize
        // I not capitalized
        // double spaces

        String comment = "Helen, i saw these products on oprah and rachael rays show. " +
                "however, i didn't know how to order it and came across your  site where " +
                "you found bonus trial supplies which is great because i dont want to pay " +
                "for something i didnt know worked. i'm currently on my 2nd month on this " +
                "stuff and i have to say.. this stuff works and my results  are unbelieveable " +
                " thank you so much for putting up this article and  doing the test.";


    }

}
