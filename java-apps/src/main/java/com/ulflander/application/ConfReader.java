package com.ulflander.application;

import com.ulflander.application.utils.UlfFileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.HashMap;

/**
 * Utility to read configuration in conf file on disk.
 *
 * @since 2/24/14
 * @author Ulflander <xlaumonier@gmail.com>
 */
public final class ConfReader {

    /**
     * Logger.
     */
    private static final Logger LOGGER =
        LogManager.getLogger(ConfReader.class);

    /**
     * Default conf file root.
     */
    private static String root =
            new File(System.getProperty("user.dir")).getParent();

    /**
     * Private constructor.
     */
    private ConfReader() {

    }

    /**
     * Get root.
     *
     * @return Root
     */
    public static String getRoot() {
        return root;
    }

    /**
     * Set root.
     *
     * @param r Root
     */
    public static void setRoot(final String r) {
        root = r;
    }



    /**
     * Read conf file for given environment.
     *
     * @param env Environment: either "local", "dev", "staging", "prod"
     */
    public static void read(final String env) {
        LOGGER.debug("Loading configuration from "
            + root + "/conf/conf." + env + ".sh");
        Conf.setDataPath(root + "/datasets/");
        if (root == null) {
            LOGGER.error("Unable to know configuration path. "
                + "Conf should be in '" + root + "conf/'. Exiting...");
            System.exit(1);
        }
        File confFile = new File(root + "/conf/conf." + env + ".sh");
        if (!confFile.exists()) {
            LOGGER.error("Conf file not found. Conf should be in "
                + "'" + root + "/conf/conf." + env + ".sh'. Exiting...");
            System.exit(1);
        }

        parse(UlfFileUtils.read(root + "/conf/conf." + env + ".sh"));
    }

    /**
     * Parse configuration file.
     *
     * @param conf Configuration file content
     */
    private static void parse(final String conf) {
        HashMap<String, String> map = new HashMap<String, String>();
        String[] lines = conf.split("\n");
        String line;
        for (int i = 0, l = lines.length; i < l; i++) {
            line = lines[i].trim();
            if (line.length() == 0
                    || line.substring(0, 1).equals("#")
                    || !line.contains("=")) {
                continue;
            }
            String[] keyVal = line.split("=");
            map.put(keyVal[0].trim(), keyVal[1].trim());
        }
        apply(map);
    }

    /**
     * Apply configuration.
     *
     * @param map Map of configuration variables
     */
    private static void apply(final HashMap<String, String> map) {

        if (map.containsKey("DEFAULT_CUSTOMER_KEY")) {
            Conf.setDefaultCID(map.get("DEFAULT_CUSTOMER_KEY"));
        }
        if (map.containsKey("EMAIL_SERVICE_USER")) {
            Conf.setEmailServiceUser(map.get("EMAIL_SERVICE_USER"));
        }
        if (map.containsKey("EMAIL_SERVICE_PASS")) {
            Conf.setEmailServicePass(map.get("EMAIL_SERVICE_PASS"));
        }

        if (map.containsKey("RABBIT_HOST")) {
            Conf.setRabbitHost(map.get("RABBIT_HOST"));
        }

        if (map.containsKey("MONGO_HOST")) {
            Conf.setMongoHost(map.get("MONGO_HOST"));
        }
        if (map.containsKey("MONGO_DB_PREFIX")) {
            Conf.setMongoDbPrefix(map.get("MONGO_DB_PREFIX"));
        }

        if (map.containsKey("SHAREDCOUNT_API_KEY")) {
            Conf.setSharedCountKey(map.get("SHAREDCOUNT_API_KEY"));
        }
    }
}
