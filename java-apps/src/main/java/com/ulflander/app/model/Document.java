package com.ulflander.app.model;

import com.google.gson.annotations.Expose;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

/**
 * Document is the main model in processing. It holds main language and results,
 * some metadatas, a processing status, the chapters and so on. Documents are
 * hold by jobs.
 *
 * @since 2/24/14
 * @author Ulflander <xlaumonier@gmail.com>
 * @see Job
 */
public class Document extends Text implements Storable {

    /**
     * Logger.
     */
    private static final Logger LOGGER =
        LogManager.getLogger(Document.class);

    /**
     * Minimum content length to create an excerpt.
     */
    public static final int EXCERPT_CHAR_LEN = 40;

    /**
     * Id in MongoDB.
     */
    private String id;

    /**
     * Date created.
     */
    private Date created;

    /**
     * Languages used in document, and number of sentences for each language.
     */
    private HashMap<Language, Integer> languages;

    /**
     * Document properties.
     */
    private HashMap<String, HashMap<String, Object>> properties =
            new HashMap<String, HashMap<String, Object>>();

    /**
     * List of chapters.
     */
    private ArrayList<Chapter> chapters;


    /**
     * Document processing status.
     */
    private DocumentStatus status = DocumentStatus.VOID;

    /**
     * Document processing history: list of names of processors run on the
     * document.
     */
    private ArrayList<String> history;

    /**
     * Time spent to process the document, in milliseconds.
     */
    private long processingTime;

    /**
     * Length of raw text.
     */
    private long rawLength;


    /**
     * Original raw text.
     */
    @Expose
    private String original;

    /**
     * Does doc exists in database.
     */
    private Boolean exists = false;

    /**
     * Frequency of tokens.
     */
    private HashMap<Token, Integer> frequency;


    /**
     * Create a new document with an empty content.
     */
    public Document() {
        super("");
    }

    /**
     * Create a new document with given content.
     *
     * @param raw Initial content of document
     */
    public Document(final String raw) {
        super(raw);
    }


    /**
     * Add a document property. Won't accept null value. Types accepted are:
     * - String
     * - Boolean
     * - Integer
     * - Float
     *
     * @param group Property group
     * @param name Property name
     * @param obj Property value
     */
    public final void addProperty(final String group,
                                  final String name,
                                  final Object obj) {

        if (obj == null) {
            throw new IllegalArgumentException("Document properties "
                    + "cant be null. "
                    + "Property " + group + "." + name);
        }

        if (!(obj instanceof String)
            && !(obj instanceof Integer)
            && !(obj instanceof Long)
            && !(obj instanceof Boolean)
            && !(obj instanceof Float)) {
            throw new IllegalArgumentException("Document properties "
                        + "only accepts String and numbers. "
                        + "Given: " + obj.getClass().getName() + " for "
                        + "property " + group + "." + name);
        }

        if (!properties.containsKey(group)) {
            properties.put(group, new HashMap<String, Object>());
        }

        properties.get(group).put(name, obj);
    }

    /**
     * Add document property only if not already set.
     *
     * @param group Property group
     * @param name Property name
     * @param obj Property value
     */
    public final void ensureProperty(final String group,
                                     final String name,
                                     final Object obj) {

        if (!hasProperty(group, name)) {
            addProperty(group, name, obj);
        }
    }

    /**
     * Get a property by group and name.
     *
     * @param group Property group
     * @param name Property name
     * @return Property value if found, null otherwise
     */
    public final Object getProperty(final String group, final String name) {
        if (!properties.containsKey(group)) {
            LOGGER.warn("Attempt to access unknown property group " + group
                    + ". Please double check group name, use hasProperty() or "
                    + "setup properly processor requirements.");
            return null;
        }

        return properties.get(group).get(name);
    }

    /**
     * Get a property by group and name, providing a fallback.
     *
     * @param group Property group
     * @param name Property name
     * @param fallback Fallback string
     * @return Property value if found, fallback otherwise
     */
    public final String getProperty(final String group,
                                    final String name,
                                    final String fallback) {
        if (!hasProperty(group, name)) {
            return fallback;
        }

        return (String) properties.get(group).get(name);
    }

    /**
     * Get a property by group and name, providing a fallback.
     *
     * @param group Property group
     * @param name Property name
     * @param fallback Fallback string
     * @return Property value if found, fallback otherwise
     */
    public final Long getProperty(final String group,
                                    final String name,
                                    final Long fallback) {
        if (!hasProperty(group, name)) {
            return fallback;
        }

        return (Long) properties.get(group).get(name);
    }

    /**
     * Get a property by group and name, providing a fallback.
     *
     * @param group Property group
     * @param name Property name
     * @param fallback Fallback string
     * @return Property value if found, fallback otherwise
     */
    public final Integer getProperty(final String group,
                                    final String name,
                                    final Integer fallback) {
        if (!hasProperty(group, name)) {
            return fallback;
        }

        return (Integer) properties.get(group).get(name);
    }

    /**
     * Get properties group.
     *
     * @param group Property group
     * @return Properties of group if group exists, null otherwise
     */
    public final HashMap<String, Object> getProperties(final String group) {
        if (!properties.containsKey(group)) {
            return null;
        }

        return (HashMap<String, Object>) properties.get(group).clone();
    }

    /**
     * Check if document property exists.
     *
     * @param group Property group
     * @param name Property name
     * @return True if property exists, false otherwise
     */
    public final boolean hasProperty(final String group, final String name) {
        return properties.containsKey(group)
                && properties.get(group).containsKey(name);
    }

    /**
     * Check if document property exists and has given type.
     *
     * @param group Property group
     * @param name Property name
     * @param clazz Class for type checking
     * @return True if property exists, false otherwise
     */
    public final boolean hasProperty(final String group,
                                     final String name,
                                     final Class clazz) {
        return hasProperty(group, name)
                && properties.get(group).get(name)
                            .getClass().isInstance(clazz);
    }

    /**
     * Get properties groups names.
     *
     * @return Groups names.
     */
    public final Set<String> getPropertyGroups() {
        return properties.keySet();
    }

    @Override
    public final void whileSetRaw() {
        chapters = new ArrayList<Chapter>();
        if (history == null) {
            history = new ArrayList<String>();
        }
    }

    /**
     * Get list of chapters in the document.
     *
     * @return List of chapters
     */
    public final ArrayList<Chapter> getChapters() {
        return chapters;
    }

    /**
     * Get number of chapters in the document.
     *
     * @return Number of chapters in document
     */
    public final int getChaptersSize() {
        if (chapters == null) {
            return 0;
        }
        return chapters.size();
    }

    /**
     * Append a chapter to the document.
     *
     * @param c Chapter to append
     */
    public final void appendChapter(final Chapter c) {
        if (c.getSurface().equals("")) {
            return;
        }

        chapters.add(c);
        c.setDocument(this);
    }

    /**
     * Get chapter at given index.
     *
     * @param pos Index of chapter
     * @return Chapter if exist, null otherwise
     */
    public final Chapter getChapterAt(final int pos) {
        if (chapters != null && chapters.size() > pos) {
            return chapters.get(pos);
        }
        return null;
    }

    /**
     * Get paragraph at given chapter index and paragraph index - WILL throw an
     * exception if chapter does not exist.
     *
     * @param chapterPos Index of chapter
     * @param pos Index of paragraph in chapter
     * @return Paragraph if exist, null otherwise, throws error if chapter
     * does not exist
     */
    public final Paragraph getParagraphAt(final int chapterPos,
                                          final int pos) {
        return getChapterAt(chapterPos)
            .getParagraphAt(pos);
    }

    /**
     * Get sentence at given chapter index, paragraph index and sentence index
     * - WILL throw an exception if chapter or paragraph does not exist.
     *
     * @param chapterPos Index of chapter
     * @param paragraphPos Index of paragraph in chapter
     * @param pos Index of sentence in paragraph
     * @return Sentence if exist, null otherwise, throws error if chapter
     * or paragraph does not exist
     */
    public final Sentence getSentenceAt(final int chapterPos,
                                        final int paragraphPos,
                                        final int pos) {
        return getChapterAt(chapterPos)
                .getParagraphAt(paragraphPos)
                .getSentenceAt(pos);
    }

    /**
     * Get token at given chapter index, paragraph index, sentence index and
     * token index - WILL throw an exception if chapter or paragraph or
     * sentence does not exist.
     *
     * @param chapterPos Index of chapter
     * @param paragraphPos Index of paragraph in chapter
     * @param sentencePos Index of sentence in paragraph
     * @param pos Index of token in sentence
     * @return Token if exist, null otherwise, throws error if chapter
     * or paragraph or sentence does not exist
     */
    public final Token getTokenAt(final int chapterPos,
                                  final int paragraphPos,
                                  final int sentencePos,
                                  final int pos) {
        return getChapterAt(chapterPos)
                .getParagraphAt(paragraphPos)
                .getSentenceAt(sentencePos)
                .getTokenAt(pos);
    }

    /**
     * Get an excerpt of the content (few first chars).
     *
     * @return A short excerpt of the document text
     */
    public final String getExcerpt() {

        String s = getSurface().replaceAll("\n", "").replaceAll("\r", "");

        if (s.length() > EXCERPT_CHAR_LEN) {
            return s.substring(0, EXCERPT_CHAR_LEN) + "...";
        }

        return s;
    }

    /**
     * Get processing history.
     *
     * @return List of processors run on the document
     */
    public final ArrayList<String> getHistory() {
        return history;
    }

    /**
     * Append a processor name in history.
     *
     * @param historyItem Name of processor that has run on document
     */
    public final void appendHistory(final String historyItem) {
        history.add(historyItem);
    }

    /**
     * Get languages used in document, and number of sentences
     * for each language.
     *
     * @return A map containing language as keys and number of sentence
     * using this language as value
     */
    public final HashMap<Language, Integer> getLanguages() {
        return languages;
    }

    /**
     * Set the list of languages used in document,
     * and number of sentences for each language.
     *
     * @param l A map containing language as keys and number of sentence
     * using this language as value
     */
    public final void setLanguages(final HashMap<Language, Integer> l) {
        this.languages = l;
    }

    /**
     * Get document processing status value.
     *
     * @return Document processing status
     */
    public final DocumentStatus getStatus() {
        return status;
    }

    /**
     * Set document processing status value.
     *
     * @param s Document processing status
     */
    public final void setStatus(final DocumentStatus s) {
        this.status = s;
    }

    /**
     * Get document processing time.
     *
     * @return Document processing time, if processing finished, 0 otherwise
     */
    public final long getProcessingTime() {
        return processingTime;
    }

    /**
     * Set document processing time.
     *
     * @param t Document processing timee
     */
    public final void setProcessingTime(final long t) {
        this.processingTime = t;
    }

    /**
     * Get document raw length value.
     *
     * @return Document raw length
     */
    public final long getRawLength() {
        return rawLength;
    }

    /**
     * Set document raw length value.
     *
     * @param l Document raw length
     */
    public final void setRawLength(final long l) {
        this.rawLength = l;
    }

    /**
     * Get document ID.
     *
     * @return Document ID
     */
    public final String getId() {
        return id;
    }

    /**
     * Set document ID.
     *
     * @param i Document ID
     */
    public final void setId(final String i) {
        this.id = i;
    }

    /**
     * Get created.
     *
     * @return Created
     */
    public final Date getCreated() {
        return created;
    }

    /**
     * Set created.
     *
     * @param c Created
     */
    public final void setCreated(final Date c) {
        this.created = c;
    }

    /**
     * Get original.
     *
     * @return Original
     */
    public final String getOriginal() {
        return original;
    }

    /**
     * Set original.
     *
     * @param o Original
     */
    public final void setOriginal(final String o) {
        this.original = o;
    }


    /**
     * Does document exists in database.
     *
     * @return Exists
     */
    public final Boolean getExists() {
        return exists;
    }

    /**
     * Set exists.
     *
     * @param e Exists
     */
    public final void setExists(final Boolean e) {
        this.exists = e;
    }


    /**
     * Get tokens frequencies.
     *
     * @return Tokens frequencies
     */
    public final HashMap<Token, Integer> getFrequency() {
        return frequency;
    }

    /**
     * Set tokens frequencies.
     *
     * @param tf Tokens frequencies
     */
    public final void setFrequency(final HashMap<Token, Integer> tf) {
        this.frequency = tf;
    }


}
