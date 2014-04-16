package com.ulflander.application;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Manage configuration.
 *
 * @since 2/24/14
 * @author Ulflander <xlaumonier@gmail.com>
 */
public final class Conf {

    /**
     * Logger.
     */
    private static final Logger LOGGER =
        LogManager.getLogger(Conf.class);

    /**
     * Name of queue used by Extractor service.
     */
    public static final String EXTRACTOR_QUEUE_NAME = "extract";

    /**
     * Name of queue used by Executor service.
     */
    public static final String EXECUTOR_QUEUE_NAME = "exec";


    /**
     * Private constructor.
     */
    private Conf() {

    }

    /**
     * Current environment.
     */
    private static Env env = Env.TEST;

    /**
     * RabbitMQ host.
     */
    private static String rabbitHost = "";

    /**
     * Sharedcount.com API Key.
     */
    private static String sharedCountKey = "";

    /**
     * MongoDB host.
     */
    private static String mongoHost = "";

    /**
     * MongoDB database.
     */
    private static String mongoDbPrefix = "";

    /**
     * Datasets path in filesystem.
     */
    private static String dataPath = "";

    /**
     * Default customer ID.
     */
    private static String defaultCID = "";

    /**
     * Email service IMAP user.
     */
    private static String emailServiceUser;

    /**
     * Email service IMAP password.
     */
    private static String emailServicePass;

    /**
     * Configuration initialized (loaded at least once).
     */
    private static Boolean initialized = false;

    /**
     * Is configuration initialized (has been loaded at least once).
     *
     * @return True if configuration has been loaded once, false otherwise.
     */
    public static Boolean isInitialized() {
        return initialized;
    }

    /**
     * Read configuration on disk for given environment. Conf file will be
     * read even if Configuration has already been initialized.
     *
     * @param e Environment ("local", "dev", "staging", "prod")
     */
    public static void read(final String e) {
        LOGGER.trace("Reading configuration for environment " + e);
        setEnv(Env.get(e));
        ConfReader.read(e);
        initialized = true;
    }

    /**
     * Read configuration on disk for local environment, only if configuration
     * has not been initialized.
     */
    public static void read() {
        if (!initialized) {
            read("local");
        }
    }

    /**
     * Get env.
     *
     * @return Env
     */
    public static Env getEnv() {
        return env;
    }

    /**
     * Set env.
     *
     * @param e Env
     */
    public static void setEnv(final Env e) {
        env = e;
    }



    /**
     * Get path of datasets in filesystem.
     *
     * @return Path of datasets in filesystem
     */
    public static String getDataPath() {
        return dataPath;
    }

    /**
     * Set path of datasets in filesystem.
     *
     * @param dPath Path of datasets in filesystem
     */
    public static void setDataPath(final String dPath) {
        Conf.dataPath = dPath;
    }

    /**
     * Set default customer ID.
     *
     * @return Default customer ID
     */
    public static String getDefaultCID() {
        return defaultCID;
    }

    /**
     * Get default customer ID.
     *
     * @param dCID Default customer ID
     */
    public static void setDefaultCID(final String dCID) {
        Conf.defaultCID = dCID;
    }

    /**
     * Get email service IMAP user.
     *
     * @return Email service IMAP user
     */
    public static String getEmailServiceUser() {
        return emailServiceUser;
    }

    /**
     * Set email service IMAP user.
     *
     * @param esu Email service IMAP user
     */
    public static void setEmailServiceUser(final String esu) {
        Conf.emailServiceUser = esu;
    }

    /**
     * Get Email service IMAP password.
     *
     * @return Email service password
     */
    public static String getEmailServicePass() {
        return emailServicePass;
    }

    /**
     * Set Email service IMAP password.
     *
     * @param esp Email service password
     */
    public static void setEmailServicePass(final String esp) {
        Conf.emailServicePass = esp;
    }

    /**
     * Get rabbit host.
     *
     * @return Rabbit host
     */
    public static String getRabbitHost() {
        return rabbitHost;
    }

    /**
     * Set rabbit host.
     *
     * @param r Rabbit host
     */
    public static void setRabbitHost(final String r) {
        Conf.rabbitHost = r;
    }

    /**
     * Get mongo host.
     *
     * @return Mongo host
     */
    public static String getMongoHost() {
        return mongoHost;
    }

    /**
     * Set mongo host.
     *
     * @param h Mongo host
     */
    public static void setMongoHost(final String h) {
        mongoHost = h;
    }

    /**
     * Get mongo db.
     *
     * @return Mongo db
     */
    public static String getMongoDbPrefix() {
        return mongoDbPrefix;
    }

    /**
     * Set mongo db.
     *
     * @param db Mongo db
     */
    public static void setMongoDbPrefix(final String db) {
        mongoDbPrefix = db;
    }


    /**
     * Get sharedcount.com API key.
     *
     * @return Sharedcount.com API key
     */
    public static String getSharedCountKey() {
        return sharedCountKey;
    }

    /**
     * Set sharedcount.com API key.
     *
     * @param k Sharedcount.com API key
     */
    public static void setSharedCountKey(final String k) {
        sharedCountKey = k;
    }


}
