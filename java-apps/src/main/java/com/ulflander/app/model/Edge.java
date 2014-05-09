package com.ulflander.app.model;

/**
 * Edge is a relationship between a document and another resource.
 *
 * Created by Ulflander on 4/9/14.
 */
public class Edge {

    /**
     * Document ID.
     */
    private String id;

    /**
     * Type of vertex.
     */
    private String relType = "cite";

    /**
     * Type of target: RDF URL, URL, document...
     */
    private String targetType;

    /**
     * Target.
     */
    private String target;

    /**
     * Get document id.
     *
     * @return Document id
     */
    public final String getId() {
        return id;
    }

    /**
     * Set document id.
     *
     * @param i Document id
     */
    public final void setId(final String i) {
        this.id = i;
    }

    /**
     * Get relationship type.
     *
     * @return Relationship type
     */
    public final String getRelType() {
        return relType;
    }

    /**
     * Set relationship type.
     *
     * @param rt Relationship type
     */
    public final void setRelType(final String rt) {
        this.relType = rt;
    }

    /**
     * Get target type.
     *
     * @return Target type
     */
    public final String getTargetType() {
        return targetType;
    }

    /**
     * Set target type.
     *
     * @param tt Target type
     */
    public final void setTargetType(final String tt) {
        this.targetType = tt;
    }

    /**
     * Get target.
     *
     * @return Target
     */
    public final String getTarget() {
        return target;
    }

    /**
     * Set target.
     *
     * @param t Target
     */
    public final void setTarget(final String t) {
        this.target = t;
    }


}
