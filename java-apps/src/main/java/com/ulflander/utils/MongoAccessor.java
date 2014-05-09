package com.ulflander.utils;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.WriteConcern;
import com.ulflander.app.Conf;
import com.ulflander.app.Env;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.UnknownHostException;

/**
 * Utility class that offers Minethat collections access.
 *
 * @author Ulflander <xlaumonier@gmail.com>
 * @since 2/28/14
 */
public final class MongoAccessor {

    /**
     * Default MongoDB port.
     */
    public static final int DEFAULT_PORT = 27017;


    /**
     * Logger.
     */
    private static final Logger LOGGER =
        LogManager.getLogger(MongoAccessor.class);

    /**
     * Private constructor.
     */
    private MongoAccessor() {

    }

    /**
     * Initialize client.
     *
     * @return MongDB client
     */
    private static MongoClient initClient() {

        try {
            client = new MongoClient(Conf.getMongoHost() , DEFAULT_PORT);

            LOGGER.debug("Client inited");
        } catch (UnknownHostException e) {
            LOGGER.error("Unable to connect to MongoDB. Quitting...", e);
            System.exit(1);
            return null;
        }

        useDatabase(Conf.getEnv());

        return client;
    }

    /**
     * MongoDB database.
     */
    private static DB db = null;

    /**
     * Current environment.
     */
    private static Env env = null;

    /**
     * MongoDB client.
     */
    private static MongoClient client = initClient();

    /**
     * Setup accessor for corresponding environment.
     *
     * @param e Environment to use
     */
    public static void useDatabase(final Env e) {
        if (env != e) {
            db = client.getDB(Conf.getMongoDbPrefix()
                    + e.toString().toLowerCase());
            db.setWriteConcern(WriteConcern.ACKNOWLEDGED);
            ensureIndexes();
            env = e;

            LOGGER.debug("Database inited for env " + e);
        }
    }

    /**
     * Ensure indexes are set up.
     */
    private static void ensureIndexes() {
        DBCollection j = db.getCollection("jobs");
        j.ensureIndex(new BasicDBObject("customerId", 1));
        j.ensureIndex(new BasicDBObject("status", 1));
        j.ensureIndex(new BasicDBObject("gateway", 1));
    }


    /**
     * Get Documents collection.
     *
     * @return MongoDB Documents collection
     */
    public static DBCollection getDocuments() {
        return db.getCollection("documents");
    }

    /**
     * Get Jobs collection.
     *
     * @return MongoDB Jobs collection
     */
    public static DBCollection getJobs() {
        return db.getCollection("jobs");
    }

    /**
     * Drop database (work only for test environment).
     */
    public static void drop() {
        if (env == Env.TEST) {
            db.dropDatabase();
        }
    }
}
