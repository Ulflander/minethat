package com.ulflander.mining;

import com.ulflander.AbstractTest;
import com.ulflander.app.model.Document;
import com.ulflander.app.model.Job;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.util.HashMap;

/**
 * @author Ulflander <xlaumonier@gmail.com>
 * @since 2/26/14
 */
public class TextExtractorTest extends AbstractTest {

    @Test
    public void fromFileTest() throws ExtractionException {

        URL url = Thread.currentThread()
            .getContextClassLoader()
            .getResource("com/ulflander/app/model/FileExtractorTest.txt");


        Document d = TextExtractor.fromFile(new File(url.getPath()));

        Assert.assertEquals("Document raw should be populated with file content",
           FileExtractorTest.TEST_TEXT, d.getSurface());
    }

    @Test
    public void fromURLTest() throws ExtractionException {
        Document d = TextExtractor.fromURL("http://example.com/");

        Assert.assertEquals("Document raw should be populated with URL content",
            "Example Domain\n\n" +
            "This domain is established to be used for illustrative examples in documents. You may use this domain in examples without prior coordination or asking for permission.", d.getSurface());

    }

    @Test
    public void fromStringTest() throws ExtractionException {
        Document d = TextExtractor.fromString(FileExtractorTest.TEST_TEXT);

        Assert.assertEquals("Document raw should be populated with file content",
            FileExtractorTest.TEST_TEXT, d.getSurface());
    }

    @Test (expected = ExtractionException.class)
    public void fromEmptyStringTest() throws ExtractionException {
        TextExtractor.fromString("");
    }

    @Test (expected = ExtractionException.class)
    public void fromNullStringTest() throws ExtractionException {
        TextExtractor.fromString(null);
    }

    @Test
    public void feedDescFallbackHTMLTest() throws ExtractionException {

        HashMap<String, Object> meta = new HashMap<String, Object>();
        meta.put("doc_description", "Some description with <img src /> in it");

        Job j = new Job();
        j.setMeta(meta);

        Document d = TextExtractor.feedDescFallback(j);

        Assert.assertEquals("Description should be clean", "Some description with   in it", d.getSurface());
    }
}
