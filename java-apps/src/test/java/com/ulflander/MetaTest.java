package com.ulflander;

import com.ulflander.app.model.Language;
import org.junit.Assert;
import org.junit.Test;

public class MetaTest extends AbstractTest {

    @Test
    public void LanguageTest () {

        d.setSurface("Ceci est un test.");
        d.setLanguage(Language.FR);
        s.addProcessor("extract.DocumentCleaner");
        s.addProcessor("extract.DocumentSplitter");

        s.submit(d);

        Assert.assertEquals("Setting manually language should always keep it", Language.FR, d.getLanguage());
    }
}
