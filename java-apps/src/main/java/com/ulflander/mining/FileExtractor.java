package com.ulflander.mining;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * File extractor uses Apache Tika to extract text content and metadata
 * from a file, except for PDF where it uses Tika for metadata and PDFbox
 * for text extraction as results were far better.
 *
 * @author Ulflander <xlaumonier@gmail.com>
 * @since 2/24/14
 */
public final class FileExtractor {


    /**
     * Logger.
     */
    private static final Logger LOGGER =
        LogManager.getLogger(FileExtractor.class);

    /**
     * Reusable parser.
     */
    private static Parser parser;

    /**
     * Get reusable parser.
     *
     * @see org.apache.tika.parser.AutoDetectParser
     * @return AutoDetectParser.
     */
    private static Parser getParser() {
        if (parser == null) {
            parser = new AutoDetectParser();
        }
        return parser;
    }

    /**
     * Extracted content of file.
     */
    private String content;

    /**
     * Extracted meta of file.
     */
    private Metadata meta;

    /**
     * Get file content value.
     *
     * @return File content, if file has been extracted
     */
    public String getContent() {
        return content;
    }

    /**
     * Get file metadata.
     *
     * @return File metadata, if file has been extracted
     */
    public Metadata getMeta() {
        return meta;
    }

    /**
     * Extract text from an input stream and attempt to close stream at the end.
     *
     * @param is Input stream
     */
    public void extract(final InputStream is) {
        ContentHandler handler;

        try {
            handler = new BodyContentHandler();
            meta = new Metadata();
            getParser().parse(is, handler, meta, new ParseContext());
            content = handler.toString().trim();
        } catch (IOException e) {
            LOGGER.error("IO error on file text extraction", e);

        } catch (TikaException e) {
            LOGGER.error("Tika throwed an error on file text extraction", e);

        } catch (SAXException e) {
            LOGGER.error("SAX throwed an error on file text extraction", e);

        } finally {

            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    LOGGER.error("IO error when closing "
                        + "BufferedInputStream on file text extraction", e);
                }
            }
        }
    }

    /**
     * Extract text from a file.
     *
     * @param f File to extract text from
     */
    public void extract(final File f) {

        InputStream is = null;

        try {
            is = new BufferedInputStream(new FileInputStream(f));
        } catch (FileNotFoundException e) {
            LOGGER.error("File not found on file text extraction", e);
            return;
        }

        extract(is);

        String n = f.getName();
        if (n.contains(".pdf")) {
            extractPDF(f);
        }
    }

    /**
     * Extract text from PDF using PDFBox.
     *
     * @param f PDF file
     */
    private void extractPDF(final File f) {
        PDDocument pdf;
        PDFParser p;
        PDFTextStripper stripper;
        COSDocument cosDoc = null;

        try {
            p = new PDFParser(new FileInputStream(f));
            p.parse();
            cosDoc = p.getDocument();
        } catch (FileNotFoundException e) {
            LOGGER.error("File not found on PDF file text extraction", e);
        } catch (IOException e) {
            LOGGER.error("IO error on PDF file text extraction", e);
        }

        pdf = new PDDocument(cosDoc);

        try {
            stripper = new PDFTextStripper();
            stripper.setStartPage(0);
            stripper.setEndPage(pdf.getNumberOfPages());
            content = stripper.getText(pdf);
        } catch (IOException e) {
            LOGGER.error("Unable to strip text using PDFBox", e);
        } finally {
            try {
                if (cosDoc != null) {
                    cosDoc.close();
                }
                pdf.close();
            } catch (Exception e) {
                LOGGER.error("IO error on end of PDF file text extraction", e);
            }
        }
    }

}
