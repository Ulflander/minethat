package com.ulflander.mining;

import com.ulflander.AbstractTest;
import org.apache.logging.log4j.LogManager;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.net.URL;

/**
 * Test file extraction using Apache Tika.
 *
 * @author Ulflander <xlaumonier@gmail.com>
 * @since 2/24/14
 */
public class FileExtractorTest extends AbstractTest {

    public final static String TEST_TEXT =
        "This document contains some English text. And that’s great.";

    private File getTestFile(String filename) {

        URL url = Thread.currentThread()
            .getContextClassLoader()
            .getResource("com/ulflander/application/model/" + filename);

        if (url == null) {
            LogManager
                .getLogger(FileExtractorTest.class)
                .error("Resource not found: "
                    + "com/ulflander/application/model/" + filename);
        }

        return new File(url.getPath());
    }

    @Test
    public void parsePDFTest() {
        FileExtractor e = new FileExtractor ();
        e.extract(getTestFile("FileExtractorTest.pdf"));

        Assert.assertEquals("Text from .pdf should be extracted",
            " EarthBox a Day Giveaway \n"
            + "Objectives \n"
            +"EarthBox wanted to engage their Facebook \n"
            +"audience with an Earth Day promotion that would \n"
                + "also increase their Facebook likes. They needed a \n"
                + "simple solution that would allow them to create a \n"
                + "sweepstakes application themselves. \n \n \n \n"



                + "Solution \n"
                + "EarthBox utilized the Votigo \n"
                + "platform to create a like-\n"
                + "gated sweepstakes. Utilizing a \n"
                + "theme and uploading a custom graphic they \n"
                + "were able to create a branded promotion. \n \n \n \n \n \n \n"
                + "Details \n"
                + "• 1 prize awarded each day for the entire Month of April  \n"
                + "• A grand prize given away on Earth Day  \n"
                + "• Daily winner announcements on Facebook \n"
                + "• Promoted through email newsletter blast  \n \n"

                + "Results (4 weeks) \n"
                + "• 6,550 entries \n \n"

                + "Facebook  \n"
            ,
            e.getContent().replace("\r", "\n"));
    }
    @Test
    public void parseDocTest() {
        FileExtractor e = new FileExtractor ();
        e.extract(getTestFile("FileExtractorTest.doc"));

        Assert.assertEquals("Text from .doc should be extracted",
            TEST_TEXT,
            e.getContent());
    }
    @Test
    public void parseDocxTest() {
        FileExtractor e = new FileExtractor ();
        e.extract(getTestFile("FileExtractorTest.docx"));

        Assert.assertEquals("Text from .docx should be extracted",
            TEST_TEXT,
            e.getContent());
    }
    @Test
    public void parseHtmlTest() {
        FileExtractor e = new FileExtractor ();
        e.extract(getTestFile("FileExtractorTest.htm"));

        Assert.assertEquals("Text from .html should be extracted",
            TEST_TEXT,
            e.getContent());
    }
    @Test
    public void parseRTFTest() {
        FileExtractor e = new FileExtractor ();
        e.extract(getTestFile("FileExtractorTest.rtf"));

        Assert.assertEquals("Text from .rtf should be extracted",
            TEST_TEXT,
            e.getContent());
    }
    @Test
    public void parseTxtTest() {
        FileExtractor e = new FileExtractor ();
        e.extract(getTestFile("FileExtractorTest.txt"));

        Assert.assertEquals("Text from .txt should be extracted",
            TEST_TEXT,
            e.getContent());
    }
    @Test
    public void parseMdTest() {
        FileExtractor e = new FileExtractor ();
        e.extract(getTestFile("FileExtractorTest.md"));

        Assert.assertEquals("Text from .md should be extracted",
            TEST_TEXT,
            e.getContent());
    }
}
