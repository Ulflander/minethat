package com.ulflander.mining.rdf;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 *
 * Created by Ulflander on 5/22/14.
 */
public final class RDFUtil {

    /**
     * Private constructor.
     */
    private RDFUtil() {

    }

    /**
     * Select a master class from a list of RDF classes.
     *
     * @param classes Set of classes
     * @return Main class if found, null otherwise
     */
    public static String getMainClass(final List<String> classes) {

        if (classes == null || classes.isEmpty()) {
            return null;
        }

        if (classes.contains("http://schema.org/Person")) {
            return "http://schema.org/Person";
        }

        if (classes.contains("http://schema.org/Organization")) {
            return "http://schema.org/Organization";
        }

        if (classes.contains("http://schema.org/Place")) {
            return "http://schema.org/Place";
        }



        return StringUtils.join(classes, ",");
    }

}
