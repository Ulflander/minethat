package com.ulflander.mining.rdf.model;


import com.ulflander.mining.rdf.RDFModel;

/**
 * RDF Model for shema.org/Person FOR TEST PURPOSE.
 *
 * @author Ulflander <xlaumonier@gmail.com>
 * @since 2/21/14
 */
public class Person extends RDFModel {

    /**
     * Full name of person.
     */
    private String fullName;

    /**
     * Get full name.
     *
     * @return Full name
     */
    public final String getFullName() {
        return fullName;
    }

    /**
     * Set full name.
     *
     * @param f Full name
     */
    public final void setFullName(final String f) {
        this.fullName = f;
    }
}
