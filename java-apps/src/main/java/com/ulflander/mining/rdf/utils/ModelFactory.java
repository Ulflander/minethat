package com.ulflander.mining.rdf.utils;


/**
 * Utility class to generate models from RDF URLs.
 *
 * @author Ulflander <xlaumonier@gmail.com>
 * @since 2/21/14
 */
public final class ModelFactory {

    /**
     * Private constructor.
     */
    private ModelFactory() {

    }

    /**
     * Creates a model for given class populated by given URL.
     *
     * @param dataURI URL where data relies
     * @param classT Class to populate with data
     * @param <T> Class T
     * @return Populated instance of class T
     */
    public static <T> T fromURL(final String dataURI, final Class<T> classT) {
        return null;
    }

}
