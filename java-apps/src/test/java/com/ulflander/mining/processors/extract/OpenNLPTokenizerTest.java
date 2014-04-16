package com.ulflander.mining.processors.extract;

import com.ulflander.AbstractTest;
import org.junit.Before;
import org.junit.Test;

public class OpenNLPTokenizerTest extends AbstractTest {

    @Before
    public void setupProcessors () {
        s.addProcessor("extract.DocumentCleaner");
        s.addProcessor("extract.DocumentSplitter");
        s.addProcessor("extract.OpenNLPTokenizer");
    }

    @Test
    public void basicTest () {
        d.setRaw("Pierre Vinken, 61 years old, will join the board as a nonexecutive director Nov. 29.\n" +
                "Mr. Vinken is chairman of Elsevier N.V., the Dutch publishing group.\n" +
                "Rudolph Agnew, 55 years old and former chairman of Consolidated Gold Fields PLC, was named a director of this British industrial conglomerate.\n" +
                "Those contraction-less sentences don't have boundary/odd cases...this one does.");
        s.submit(d);


        d.setRaw("这段文字显然是德国人写的。我们的目标是，语言识别是有效的，而且这些几句话被认为是讲英语");
        s.submit(d);

    }

}
