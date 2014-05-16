package com.ulflander.mining;

import com.ulflander.AbstractTest;
import com.ulflander.app.model.Document;
import com.ulflander.utils.UlfFileUtils;
import org.apache.logging.log4j.LogManager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.URL;

/**
 * Created by Ulflander on 4/24/14.
 */
public class MetaExtractorTest extends AbstractTest {

    private String getTestFile(String filename) {

        URL url = Thread.currentThread()
                .getContextClassLoader()
                .getResource("com/ulflander/mining/" + filename);

        if (url == null) {
            LogManager
                    .getLogger(FileExtractorTest.class)
                    .error("Resource not found: "
                            + "com/ulflander/mining/" + filename);
        }

        return UlfFileUtils.read(new File(url.getPath()));
    }

    @Before
    public void setupProcessors () {
        s.addProcessor("extract.DocumentCleaner");
        s.addProcessor("extract.en.EnCommonAcronymsCleaner");
        s.addProcessor("extract.DocumentSplitter");
        s.addProcessor("extract.LanguageDetector");
        s.addProcessor("extract.POSTagger");
        s.addProcessor("extract.DocumentTokenizer");
        s.addProcessor("extract.TokenWeightConsolidation");
        s.addProcessor("extract.TokenCounter");
        s.addProcessor("extract.TokenCleaner");
        s.addProcessor("extract.AcronymExtractor");
        s.addProcessor("extract.TokenCorpusGuesser");
        s.addProcessor("extract.TokenRegExpGuesser");
        s.addProcessor("extract.en.EnTokenPOSConsolidation");
        s.addProcessor("extract.en.EnTokenSingularization");
        s.addProcessor("extract.TokenCorpusConsolidation");
        s.addProcessor("extract.TokenSiblingsConsolidation");
        s.addProcessor("extract.TokenInferConsolidation");
        s.addProcessor("extract.en.EnOperatorBasedConsolidation");
        s.addProcessor("extract.TokenAggregator");
        s.addProcessor("extract.AggregatedCorpusGuesser");
        s.addProcessor("extract.TokenFrequency");
        s.addProcessor("extract.KeywordSelector");
        s.addProcessor("augment.BasicTextStat");
        s.addProcessor("augment.QualityEvaluator");
    }


    @Test
    public void fullTest() throws ExtractionException {
        String html = getTestFile("test-washpost.html");
        Document d = TextExtractor.fromHTMLString(html);
        s.submit(d);
        trace(d);
    }


    @Test
    public void testNytimes() {
        String html = getTestFile("meta-nytimes.html");
        Document d = new Document();
        MetaExtractor.extract(d, html);

        Assert.assertEquals("This Story Stinks", d.getProperty("meta", "doc_title"));
        Assert.assertEquals("http://www.nytimes.com/2013/03/03/opinion/sunday/this-story-stinks.html", d.getProperty("meta", "doc_url"));
        Assert.assertEquals("article", d.getProperty("meta", "doc_type"));
        Assert.assertEquals("Online reader comments have a bigger effect on our understanding than many of us realize.", d.getProperty("meta", "doc_description"));
        Assert.assertEquals("http://graphics8.nytimes.com/images/2013/03/03/sunday-review/03GRAYMATTER/03GRAYMATTER-superJumbo.jpg", d.getProperty("meta", "doc_image"));
        Assert.assertEquals("@nytimes", d.getProperty("meta", "doc_twitter_username"));
        Assert.assertEquals("Customs  Etiquette and Manners,Social Networking (Internet),Computers and the Internet,Psychology and Psychologists,Journal of Computer-Mediated Communication  The", d.getProperty("meta", "doc_keywords"));
        Assert.assertEquals("The New York Times", d.getProperty("meta", "doc_source_name"));
        Assert.assertEquals("Customs, Etiquette and Manners;Social Networking (Internet);Computers and the Internet;Psychology and Psychologists", d.getProperty("meta", "doc_classes"));
        Assert.assertEquals("Dominique Brossard, Dietram A. Scheufele", d.getProperty("meta", "doc_author"));
    }

    @Test
    public void testCnn() {
        String html = getTestFile("meta-cnn.html");
        Document d = new Document();
        MetaExtractor.extract(d, html);

        Assert.assertEquals("South Korean lawmaker: Sunken ferry was expanded last year", d.getProperty("meta", "doc_title"));
        Assert.assertEquals("Will Ripley, K.J. Kwon, Jethro Mullen, CNN", d.getProperty("meta", "doc_author"));
        Assert.assertEquals("http://www.cnn.com/2014/04/24/world/asia/south-korea-ship-sinking/index.html", d.getProperty("meta", "doc_url"));
        Assert.assertEquals("article", d.getProperty("meta", "doc_type"));
        Assert.assertEquals("A South Korean lawmaker says renovations last year expanded the top floor of the sunken ferry to make room for 117 more passengers.", d.getProperty("meta", "doc_description"));
        Assert.assertEquals("http://i2.cdn.turner.com/cnn/dam/assets/140423200803-ngtv-ferry-cargo-south-korea-story-top.jpg", d.getProperty("meta", "doc_image"));
        Assert.assertEquals("@CNNI", d.getProperty("meta", "doc_twitter_username"));
        Assert.assertEquals("South Korea ferry,South Korea ferry sinking,South Korea shipwreck,Sewol,South Korean ferry capsizes", d.getProperty("meta", "doc_keywords"));
        Assert.assertEquals("Jindo, South Korea", d.getProperty("meta", "doc_main_location"));
    }

    @Test
    public void testLeMonde() {
        String html = getTestFile("meta-lemonde.html");
        Document d = new Document();
        MetaExtractor.extract(d, html);

        Assert.assertEquals("Le Monde.fr", d.getProperty("meta", "doc_source_name"));
    }
}
