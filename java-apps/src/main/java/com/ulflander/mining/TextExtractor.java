package com.ulflander.mining;

import com.ulflander.app.model.Document;
import com.ulflander.app.model.Job;
import com.ulflander.app.model.JobDocumentType;
import com.ulflander.utils.UlfHashUtils;
import com.ulflander.utils.UlfNetworkUtils;
import com.ulflander.utils.UlfStringUtils;
import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.document.TextBlock;
import de.l3s.boilerpipe.document.TextDocument;
import de.l3s.boilerpipe.document.TextDocumentStatistics;
import de.l3s.boilerpipe.estimators.SimpleEstimator;
import de.l3s.boilerpipe.extractors.ArticleExtractor;
import de.l3s.boilerpipe.extractors.CanolaExtractor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Arrays;

/**
 * Class that create a document given various inputs: URL, raw string, file.
 *
 * Lot of different strategies/libraries are used depending on the kind of
 * method called.
 *
 * @author Ulflander <xlaumonier@gmail.com>
 * @since 2/24/14
 */
public final class TextExtractor {

    /**
     * Logger.
     */
    private static final Logger LOGGER =
        LogManager.getLogger(TextExtractor.class);

    /**
     * Private constructor.
     */
    private TextExtractor() {

    }

    /**
     * Create a document from given Job, depending of type of Job.
     *
     * @see com.ulflander.app.model.JobDocumentType
     * @param job Job document
     * @return Processable document
     * @throws ExtractionException In case content is empty or an error occured
     * during retrieval of URL
     */
    public static Document fromJobDocument(final Job job)
        throws ExtractionException  {

        if (job == null) {
            LOGGER.warn("Got a null Job");
            return null;
        }

        if (job.getType() == JobDocumentType.FILE) {
            return fromFile(new File(job.getValue()));
        }

        if (job.getType() == JobDocumentType.HTML_STRING) {
            return fromHTMLString(job.getValue());
        }

        if (job.getType() == JobDocumentType.STRING) {
            return fromString(job.getValue());
        }

        if (job.getType() == JobDocumentType.URL) {
            return fromURL(job.getValue());
        }

        throw new ExtractionException("JobDocumentType "
            + job.getType().toString() + " is unknown");
    }

    /**
     * Should get meta.excerpt as text if URL retrieving didn't work.
     *
     * @param job Job to extract text from
     * @return Document populated with URL content of feed article
     * or feed excerpt
     * @throws ExtractionException If no content in feed neither at URL given
     */
    public static Document fromFeedURL(final Job job)
            throws ExtractionException {
        return fromString("");
    }

    /**
     * Get URL content, then use <fromHTMLString> method to extract text.
     *
     * @see com.ulflander.utils.UlfNetworkUtils
     * @param url URL to load
     * @return Document populated with text retrieved from URL
     * @throws ExtractionException In case content is empty or an error occured
     * during retrieval of URL
     */
    public static Document fromURL(final String url)
        throws ExtractionException {

        // First retrieve URL content
        String content = UlfNetworkUtils.getContent(url);

        if (content == null || content.equals("")) {
            LOGGER.warn("URL " + url + " didn't return any content.");
            return null;
        }

        // Then use HTML String extraction
        return fromHTMLString(content);
    }


    /**
     * Populate a new document with given string -
     * extract content using Boilerpipe.
     *
     * Extractors used are CanolaExtractor and ArticleExtractor from Boilerpipe
     * version 1.2.0. Note that v1.2.0 is used
     * but is not available on Maven, it's available as local maven library
     * and can be installed using Makefile.
     *
     * We apply both extractors, then we compare the results, first by checking
     * low quality flag from Boilerpipe SimpleEstimator, then by comparing the
     * average num words if both extractions are considered high quality by
     * estimator(less being better).
     *
     * Once text is extract, we execute the following:
     * - Clean multiple spaces
     * - Transform new lines to double new lines, so paragraph detection
     * of DocumentSplitter processor can work
     * - Use fromString method onto the resulting string
     * - Populate doc with metadata from HTML if found
     *
     * Possible improvement here would be to add multiple strategies for text
     * extraction, and try to compare quality of results, keeping only the
     * "best match".
     *
     * Doc meta extraction is done using the MetaExtractor class and Jsoup.
     *
     * @see de.l3s.boilerpipe.extractors.ArticleExtractor
     * @see de.l3s.boilerpipe.extractors.CanolaExtractor
     * @see de.l3s.boilerpipe.document.TextDocumentStatistics
     * @see de.l3s.boilerpipe.estimators.SimpleEstimator
     * @see com.ulflander.mining.MetaExtractor
     * @param str String to process
     * @return Document populated with text retrieved from URL
     * @throws ExtractionException In case content is empty
     */
    public static Document fromHTMLString(final String str)
            throws ExtractionException {

        TextDocumentStatistics statsBefore = getStatistics(str);
        String canolaText;
        String articleText;
        String chosen;
        String extractor;

        try {
            canolaText = CanolaExtractor.INSTANCE.getText(str);
        } catch (BoilerpipeProcessingException e) {
            throw new ExtractionException("Boilerpipe exception: "
                    + e.getMessage());
        }

        try {
            articleText = ArticleExtractor.INSTANCE.getText(str);
        } catch (BoilerpipeProcessingException e) {
            throw new ExtractionException("Boilerpipe exception: "
                    + e.getMessage());
        }

        TextDocumentStatistics canolaStats = getStatistics(canolaText);
        Boolean canolaLow =
            SimpleEstimator.INSTANCE.isLowQuality(statsBefore, canolaStats);

        TextDocumentStatistics articleStats = getStatistics(articleText);
        Boolean articleLow =
            SimpleEstimator.INSTANCE.isLowQuality(statsBefore, articleStats);

        if (canolaLow && articleLow) {
            if (articleText.length() < canolaText.length()) {
                chosen = articleText;
                extractor = "low_article";
            } else {
                chosen = canolaText;
                extractor = "low_canola";
            }
        } else if (canolaLow) {
            chosen = articleText;
            extractor = "article";
        } else if (articleLow) {
            chosen = canolaText;
            extractor = "canola";
        } else if (articleText.length() < canolaText.length()) {
            chosen = articleText;
            extractor = "article";
        } else {
            chosen = canolaText;
            extractor = "canola";
        }

        chosen = UlfStringUtils.cleanSpaces(chosen);
        chosen = chosen.replaceAll("\n", "\n\n");

        Document doc = fromString(chosen);
        MetaExtractor.extract(doc, str);
        doc.addProperty("meta", "boilerpipe_extractor", extractor);
        return doc;
    }

    /**
     * Get Boilerpipe TextDocumentStatistics from a string.
     *
     * @param str String to get statistics from
     * @return Document statistics as returned by Boilerpipe
     */
    private static TextDocumentStatistics getStatistics(final String str) {
        return new TextDocumentStatistics(new TextDocument(Arrays.asList(
                new TextBlock(str)
        )), true);
    }


    /**
     * Populate a new document with given string - string is cleaned before.
     *
     * All lines are trimmed.
     *
     * @param str String to process
     * @return Document populated with text retrieved from String
     * @throws ExtractionException In case content is empty
     */
    public static Document fromString(final String str)
        throws ExtractionException {
        return fromString(str, null);
    }

    /**
     * Populate a new document with given string, if empty then use fallback.
     *
     * @param str String to process
     * @param fallback Fallback string to process
     * @return Document populated with text retrieved from String or fallback
     * @throws ExtractionException If content is empty and there's no fallback
     */
    public static Document fromString(final String str,
                                      final String fallback)
            throws ExtractionException {

        if (str == null) {
            if (fallback != null) {
                return new Document(fallback);
            }
            throw new ExtractionException("Content is null");
        }

        String s = UlfStringUtils.trimLines(str);

        if (s.equals("")) {
            if (fallback != null) {
                return new Document(fallback);
            }
            throw new ExtractionException("Content is empty");
        }

        Document d = new Document(s);

        // Add aggregated date
        d.addProperty("meta", "doc_aggregated_date",
                System.currentTimeMillis());

        // Doc fingerprint
        d.addProperty("meta", "doc_fingerprint",
                UlfHashUtils.sha256(d.getSurface()));


        return d;
    }

    /**
     * Open file, extract content using Apache Pika or PDFBox,
     * return resulting document.
     *
     * @see com.ulflander.mining.FileExtractor
     * @param f File to get content from
     * @return Document populated with text from file
     * @throws ExtractionException In case content is empty or an error occured
     * during retrieval of URL
     */
    public static Document fromFile(final File f)
        throws ExtractionException {

        FileExtractor extractor = new FileExtractor();
        extractor.extract(f);
        return fromString(extractor.getContent());
    }

}
