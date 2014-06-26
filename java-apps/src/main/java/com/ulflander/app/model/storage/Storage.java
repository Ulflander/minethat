package com.ulflander.app.model.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.ulflander.app.model.Storable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.types.ObjectId;

/**
 * Abtsract class that offers utility methods for concrete storage classes.
 *
 * Created by Ulflander on 4/9/14.
 */
public abstract class Storage {

    /**
     * Logger.
     */
    private static final Logger LOGGER =
        LogManager.getLogger(Storage.class);

    /**
     * Get a document as DBObject from MongoDB.
     *
     * @param id Document identifier
     * @param collection Mongo collection
     * @return Document DBObject
     */
    public static DBObject getDBObject(final String id,
                                       final DBCollection collection) {
        BasicDBObject query = new BasicDBObject("_id", new ObjectId(id));
        return collection.findOne(query);
    }

    /**
     * Get an object by field/value.
     *
     * @param key Field name
     * @param value Field value
     * @param fields List of fields
     * @param clazz Class of object
     * @param collection Mongo collection
     * @return The instance of object class if found, null otherwise
     */
    protected static final Object get(final String key,
                                      final String value,
                                      final BasicDBObject fields,
                                      final Class clazz,
                                      final DBCollection collection) {

        DBObject dbObj = null;
        try {
            BasicDBObject query = new BasicDBObject(key, value);
            dbObj = collection.findOne(query, fields);
        } catch (Exception e) {
            LOGGER.error("Unable to retrieve doc with " + key + "=" + value, e);
        }

        if (dbObj == null) {
            return null;
        }

        Object res;
        try {
            GsonBuilder gsonAdapter = GsonTypeAdapter
                    .getGsonBuilder(GsonTypeAdapter.AType.DESERIALIZER);
            Gson gson = gsonAdapter.create();

            res = gson.fromJson(dbObj.toString(), clazz);
        } catch (Exception e) {
            LOGGER.error("Unable to read JSON Mongo entry ["
                    + collection.getName() + ":" + key + "=" + value + "]", e);
            System.out.println(dbObj.toString());
            return null;
        }

        if (Storable.class.isAssignableFrom(clazz)) {
            ((Storable) res).setId(dbObj.get("_id").toString());
        }

        return res;
    }

    /**
     * Get an object by field/value.
     *
     * @param key Field name
     * @param value Field value
     * @param clazz Class of object
     * @param collection Mongo collection
     * @return The instance of object class if found, null otherwise
     */
    protected static final Object get(final String key,
                                      final String value,
                                      final Class clazz,
                                      final DBCollection collection) {

        return get(key, value, null, clazz, collection);
    }

    /**
     * Get an object by ID.
     *
     * @param id ID of object
     * @param clazz Class of object
     * @param collection Mongo collection
     * @return The instance of object class if found, null otherwise
     */
    protected static final Object get(final String id,
                                      final Class clazz,
                                      final DBCollection collection) {

        DBObject dbObj = null;
        try {
            dbObj = getDBObject(id, collection);
        } catch (Exception e) {
            LOGGER.error("Unable to retrieve model with id " + id, e);
        }

        if (dbObj == null) {
            return null;
        }

        Object res;
        try {
            GsonBuilder gsonAdapter = GsonTypeAdapter
                    .getGsonBuilder(GsonTypeAdapter.AType.DESERIALIZER);
            Gson gson = gsonAdapter.create();

            res = gson.fromJson(dbObj.toString(), clazz);
        } catch (Exception e) {
            LOGGER.error("Unable to read JSON Mongo entry ["
                    + collection.getName() + ":" + id + "]", e);
            System.out.println(dbObj.toString());
            return null;
        }

        if (Storable.class.isAssignableFrom(clazz)) {
            ((Storable) res).setId(id);
        }

        return res;
    }
}
