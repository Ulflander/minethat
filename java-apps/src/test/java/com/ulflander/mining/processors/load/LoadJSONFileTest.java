package com.ulflander.mining.processors.load;

import com.ulflander.AbstractTest;
import com.ulflander.application.utils.UlfFileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;


public class LoadJSONFileTest extends AbstractTest {

    @Test
    public void Test () {

        s.addProcessor("extract.DocumentCleaner");
        s.addProcessor("extract.DocumentSplitter");
        s.addProcessor("extract.LanguageDetector");
        s.addProcessor("extract.DocumentTokenizer");
        s.addProcessor("extract.TokenCounter");
        s.addProcessor("extract.TokenCleaner");
        s.addProcessor("extract.TokenRegExpGuesser");
        s.addProcessor("extract.en.EnTokenSingularization");
        s.addProcessor("augment.BasicTextStat");
        s.addProcessor("augment.geoloc.MaxMindIPExtraction");
        s.addProcessor("load.JSONFile");
        d.setRaw("This english test moslty test that my IP 81.2.69.160 is somehow geolocated, and that plurals are " +
                "actually considered as plural. For future tests it contains also sentences.\n\nAnd paragraphs");
        s.submit(d);

        Assert.assertEquals("A JSON file should be saved", true, UlfFileUtils.exists("/tmp/test.json"));
    }

    @After
    public void removeTestFile () {
        UlfFileUtils.delete("/tmp/test.json");
    }
}
