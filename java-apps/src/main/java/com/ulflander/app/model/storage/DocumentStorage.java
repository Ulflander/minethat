package com.ulflander.app.model.storage;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.ulflander.app.model.Chapter;
import com.ulflander.app.model.Document;
import com.ulflander.app.model.Entity;
import com.ulflander.app.model.Paragraph;
import com.ulflander.app.model.Sentence;
import com.ulflander.app.model.Token;
import com.ulflander.mining.rdf.RDFUtil;
import com.ulflander.utils.MongoAccessor;
import com.ulflander.utils.UlfHashUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Set;

/**
 * Store and retrieve documents from MongoDB.
 *
 *
 * @author Ulflander <xlaumonier@gmail.com>
 * @since 2/28/14
 */
public final class DocumentStorage extends Storage {

    /**
     * Logger.
     */
    private static final Logger LOGGER =
        LogManager.getLogger(DocumentStorage.class);

    /**
     * Private constructor.
     */
    private DocumentStorage() {

    }

    /**
     * Save a document in MongoDB.
     *
     * @param document Document
     * @return Identifier of document
     */
    public static String insert(final Document document) {
        DBObject obj = fromDocument(document);
        MongoAccessor.getDocuments().insert(obj);

        document.setId(obj.get("_id").toString());

        return document.getId();
    }


    /**
     * Update a document in MongoDB (Only updates status, processingLength).
     *
     * @param document Document
     * @return Identifier of document
     */
    public static String update(final Document document) {
        DBObject obj = fromDocument(document);

        if (obj == null) {
            return null;
        }

        DBObject idObj = getDBObject(document.getId(),
                                        MongoAccessor.getDocuments());
        MongoAccessor.getDocuments().update(idObj, obj);

        return idObj.get("_id").toString();
    }

    /**
     * Get a document from MongoDB.
     *
     * @param id Document identifier
     * @return Document object
     */
    public static Document get(final String id) {
        Document d = (Document) get(id, Document.class,
                                    MongoAccessor.getDocuments());
        if (d != null) {
            d.setExists(true);
        }
        return d;
    }


    /**
     * Create a DBObject from a Document object.
     *
     * @param document Document to convert into DBObject
     * @return DBObject to be used along with MongoAccessor
     */
    private static DBObject fromDocument(final Document document) {


        // Document
        DBObject doc = new BasicDBObject();
        doc.put("surface", document.getSurface());

        // Meta
        Set<String> groups = document.getPropertyGroups();
        DBObject properties = new BasicDBObject();

        for (String group : groups) {
            DBObject obj = new BasicDBObject();
            HashMap<String, Object> values = document.getProperties(group);
            for (String key : values.keySet()) {
                obj.put(key, values.get(key));
            }
            properties.put(group, obj);
        }

        doc.put("properties", properties);

        DBObject entities = new BasicDBObject();
        int ent = 0;

        for (Chapter c: document.getChapters()) {
            for (Paragraph p: c.getParagraphs()) {
                for (Sentence s: p.getSentences()) {
                    for (Token t: s.getTokens()) {
                        Entity e = t.getEntity();
                        if (e != null) {
                            DBObject obj = new BasicDBObject();
                            obj.put("id", e.getId());
                            obj.put("confidence", e.getConfidence());
                            obj.put("label", e.getLabel());
                            obj.put("desc", e.getDescription());
                            obj.put("class",
                                    RDFUtil.getMainClass(e.getClasses()));

                            entities.put(UlfHashUtils.sha1(e.getId()), obj);
                            ent++;
                        }
                    }
                }
            }
        }

        if (ent > 0) {
            doc.put("entities", entities);
        }

        return doc;
    }
}
