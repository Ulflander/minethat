package com.ulflander.mining.processors.extract;

import com.ulflander.AbstractTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DocumentSplitterChainingTest extends AbstractTest {

    @Before
    public void initProcessors () {
        s.addProcessor("extract.DocumentCleaner");
        s.addProcessor("extract.DocumentSplitter");
        s.addProcessor("extract.LanguageDetector");
        s.addProcessor("extract.DocumentTokenizer");
        s.addProcessor("extract.TokenCounter");

    }

    @Test
    public void NextTest () {

        s.submit(d);

        Assert.assertEquals("Second token should be next of first one", d.getTokenAt(0,0,0,1), d.getTokenAt(0,0,0,0).getNext());
    }

    @Test
    public void PreviousTest () {

        s.submit(d);

        Assert.assertEquals("First token should be previous of second one", d.getTokenAt(0,0,0,0), d.getTokenAt(0,0,0,1).getPrevious());
    }

    @Test
    public void LastNextTest () {

        s.submit(d);

        Assert.assertEquals("Last token should have no next one", null, d.getTokenAt(0,0,0,3).getNext());
    }

    @Test
    public void FirstPreviousTest () {

        s.submit(d);

        Assert.assertEquals("First token should have no previous one", null, d.getTokenAt(0,0,0,0).getPrevious());
    }

    @Test
    public void NextPreviousTest () {

        s.submit(d);

        Assert.assertEquals("First token should be previous of second one", d.getTokenAt(0,0,0,0), d.getTokenAt(0,0,0,1).getPrevious());
        Assert.assertEquals("Third token should be next of second one", d.getTokenAt(0,0,0,2), d.getTokenAt(0,0,0,1).getNext());
    }
}
