package com.ulflander.application.model.storage;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.bson.types.ObjectId;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Gson adapter for Mongo.
 * craigsmusings.com/2011/04/09/deserializing-mongodb-ids-and-dates-with-gson/
 *
 * Created by Ulflander on 4/10/14.
 */
public final class GsonTypeAdapter {


    /**
     * Private constructor.
     */
    private GsonTypeAdapter() {

    }

    /**
     * Adapter type.
     */
    public static enum AType {
        /**
         * Deserializer adapter type.
         */
        DESERIALIZER,
        /**
         * Serializer adapter type.
         */
        SERIALIZER
    }

    /**
     * GsonTypeAdapter.getGsonBuilder.
     *
     * @param g - Deserialize from JSON or Serialize to JSON
     * @return GsonBuilder object with type adapters for MongoDB registered
     */
    public static GsonBuilder getGsonBuilder(final AType g) {
        GsonBuilder gb = new GsonBuilder();
        switch (g) {
            case DESERIALIZER:
                gb.registerTypeAdapter(ObjectId.class,
                        new GsonTypeAdapter.ObjectIdDeserializer());
                gb.registerTypeAdapter(Date.class,
                        new GsonTypeAdapter.DateDeserializer());
                break;
            case SERIALIZER:
                gb.registerTypeAdapter(ObjectId.class,
                        new GsonTypeAdapter.ObjectIdSerializer());
                gb.registerTypeAdapter(Date.class,
                        new GsonTypeAdapter.DateSerializer());
                break;
            default:
                return null;
        }
        return gb;
    }

    /**
     * ObjectIdDeserializer.deserialize.
     * @return Bson.Types.ObjectId
     */
    public static class ObjectIdDeserializer
            implements JsonDeserializer<ObjectId> {

        @Override
        public final ObjectId deserialize(final JsonElement json,
                                    final Type typeOfT,
                                    final JsonDeserializationContext context) {
            try {
                return new ObjectId(json.getAsJsonObject()
                        .get("$oid").getAsString());
            } catch (Exception e) {
                return null;
            }
        }
    }

    /**
     * ObjectIdSerializer.serialize.
     * @return $oid JsonObject from BSON ObjectId
     */
    public static class ObjectIdSerializer
            implements JsonSerializer<ObjectId> {
        @Override
        public final JsonElement serialize(final ObjectId id,
                                     final Type typeOfT,
                                     final JsonSerializationContext context) {
            JsonObject jo = new JsonObject();
            jo.addProperty("$oid", id.toStringMongod());
            return jo;
        }
    }

    /**
     * DateDeserializer.deserialize.
     * @return Java.util.Date
     */
    public static class DateDeserializer
            implements JsonDeserializer<Date> {
        @Override
        public final Date deserialize(final JsonElement json,
                                final Type typeOfT,
                                final JsonDeserializationContext context) {
            Date d = null;
            SimpleDateFormat f2 =
                    new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            try {
                d = f2.parse(json.getAsJsonObject().get("$date").getAsString());
            } catch (ParseException e) {
                d = null;
            }
            return d;
        }
    }

    /**
     * DateSerializer.serialize.
     * @return date JsonElement
     */
    public static class DateSerializer implements JsonSerializer<Date> {
        @Override
        public final JsonElement serialize(final Date date,
                                     final Type typeOfT,
                                     final JsonSerializationContext context) {
            Date d = (Date) date;
            SimpleDateFormat format =
                    new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            JsonObject jo = new JsonObject();
            jo.addProperty("$date", format.format(d));
            return jo;
        }
    }
}
