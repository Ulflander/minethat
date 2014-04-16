package com.ulflander.mining.processors.extract;

import com.ulflander.AbstractTest;
import org.junit.Before;
import org.junit.Test;

public class POSTaggerTest extends AbstractTest {

    @Before
    public void setupProcessors () {
        s.addProcessor("extract.DocumentCleaner");
        s.addProcessor("extract.DocumentSplitter");
        s.addProcessor("extract.LanguageDetector");
        s.addProcessor("extract.POSTagger");
    }

    @Test
    public void basicTest () {
        d.setRaw("Pierre Vinken, 61 years old, will join the board as a nonexecutive director Nov. 29.");
        s.submit(d);
    }

    @Test
    public void multilingualTest () {
        d.setRaw("Pierre Vinken, 61 years old, will join the board as a nonexecutive director Nov. 29. Pierre Vinken, 61 ans, rejoindra le comité de direction en tant que directeur non executif le 29 novembre.");
        s.submit(d);
    }
}
