package com.ulflander.mining.processors.extract;

import com.ulflander.AbstractTest;
import org.junit.Before;
import org.junit.Test;

public class MaxMindFreeIPGeolocTest extends AbstractTest {

    @Before
    public void setupProcessors () {
        s.addProcessor("extract.DocumentCleaner");
        s.addProcessor("extract.DocumentSplitter");
        s.addProcessor("extract.LanguageDetector");
        s.addProcessor("extract.DocumentTokenizer");
        s.addProcessor("extract.TokenCounter");
        s.addProcessor("extract.TokenCleaner");
        s.addProcessor("extract.TokenRegExpGuesser");
        s.addProcessor("augment.geoloc.MaxMindIPExtraction");
    }

    @Test
    public void LocalIPTest () {

        d.setRaw("My IP is 192.168.0.0");
        s.submit(d);

        //Assert.assertEquals("Local IP shouldn't return a location", 0, d.getTokenAt(0,0,0,3).getResults().size());
    }

    @Test
    public void RealIPResponseTest () {

        d.setRaw("My IP is 81.2.69.160");
        s.submit(d);

        //Assert.assertEquals("Real IP should return a location", 1, d.getTokenAt(0,0,0,3).getResults().size());
    }

    @Test
    public void RealIPCityTest () {

        d.setRaw("My IP is 81.2.69.160");
        s.submit(d);

        //MaxMindIPExtractionResult res = (MaxMindIPExtractionResult) d.getTokenAt(0, 0, 0, 3).getResults().get(0);
        //Assert.assertEquals("Real IP should return London as city", "Arnold", res.getCity());
    }
}
