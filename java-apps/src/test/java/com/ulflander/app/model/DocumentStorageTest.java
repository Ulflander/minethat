package com.ulflander.app.model;

import com.ulflander.AbstractTest;
import com.ulflander.app.Conf;
import com.ulflander.app.Env;
import com.ulflander.app.model.storage.DocumentStorage;
import com.ulflander.utils.MongoAccessor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Ulflander <xlaumonier@gmail.com>
 * @since 2/28/14
 */
public class DocumentStorageTest extends AbstractTest {

    private String testContent = "Some test content";

    @Before
    public void readConf() {
        Conf.read();
        MongoAccessor.useDatabase(Env.TEST);
        s.addProcessor("extract.DocumentCleaner");
        s.addProcessor("extract.DocumentSplitter");
        s.addProcessor("extract.TokenCleaner");
        s.addProcessor("extract.TokenRegExpGuesser");
        s.addProcessor("extract.LanguageDetector");
    }

    @Test
    public void testInsertGetNotNull() {

        Document doc = new Document();
        doc.setSurface(testContent);
        String id = DocumentStorage.insert(doc);

        Document returned = DocumentStorage.get(id);

        Assert.assertNotNull("Document shouldn't be null", returned);
    }


    @Test
    public void testInsertGetGoodValue() {

        Document doc = new Document();
        doc.setSurface(testContent);

        s.submit(doc);
        String id = DocumentStorage.insert(doc);

        Assert.assertEquals("Job retrieved should have good id",
                testContent, doc.getSurface());
    }



}
