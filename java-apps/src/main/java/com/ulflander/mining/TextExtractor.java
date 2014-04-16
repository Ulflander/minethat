package com.ulflander.mining;

import com.ulflander.application.model.Document;
import com.ulflander.application.model.Job;
import com.ulflander.application.model.JobDocumentType;
import com.ulflander.application.utils.UlfNetworkUtils;
import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.extractors.CanolaExtractor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

/**
 * Class that create a document given various inputs: URL, raw string, file.
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
     * @see com.ulflander.application.model.JobDocumentType
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
     * @see com.ulflander.application.utils.UlfNetworkUtils
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
     * Extractor used is CanolaExtractor (v1.2.0). Note that v1.2.0 is used
     * but is not available on Maven.
     *
     * We then:
     * - Clean multiple spaces
     * - Transform new lines to double new lines, so paragraph detection
     * of DocumentSplitter processor can work.
     * - And finally use <fromString> method onto the resulting string.
     *
     * Possible improvement here would be to add multiple strategies for text
     * extraction, and try to compare quality of results, keeping only the
     * "best match".
     *
     * @param str String to process
     * @return Document populated with text retrieved from URL
     * @throws ExtractionException In case content is empty
     */
    public static Document fromHTMLString(final String str)
            throws ExtractionException {

        String fullText;

        try {
            fullText = CanolaExtractor.INSTANCE.getText(str);
        } catch (BoilerpipeProcessingException e) {
            throw new ExtractionException("Boilerpipe exception: "
                    + e.getMessage());
        }

        fullText = TextCleaner.cleanSpaces(fullText);
        fullText = fullText.replaceAll("\n", "\n\n");

        return fromString(fullText);
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

        String s = TextCleaner.trimLines(str);

        if (s.equals("")) {
            if (fallback != null) {
                return new Document(fallback);
            }
            throw new ExtractionException("Content is empty");
        }

        return new Document(s);
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
