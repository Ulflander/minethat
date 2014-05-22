package com.ulflander.app.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * An entity.
 *
 * @author Ulflander <xlaumonier@gmail.com>
 * @since 3/1/14
 */
public class Entity {


    /**
     * Label.
     */
    private String label;

    /**
     * Get label.
     *
     * @return Label
     */
    public final String getLabel() {
        return label;
    }

    /**
     * Set label.
     *
     * @param l Label
     */
    public final void setLabel(final String l) {
        this.label = l.trim();
    }


    /**
     * Description.
     */
    private String description;

    /**
     * Get description.
     *
     * @return Description
     */
    public final String getDescription() {
        return description;
    }

    /**
     * Set description.
     *
     * @param d Description
     */
    public final void setDescription(final String d) {
        this.description = d.trim();
    }

    /**
     * If entity is related to a token type, the token type.
     */
    private TokenType tokenType;

    /**
     * Confidence score.
     */
    private Float confidence = 0f;

    /**
     * Entity URL where original data relies, or entity raw value.
     */
    private String value;

    /**
     * Source of entity (RDF, Techcrunch...).
     */
    private EntitySource source;

    /**
     * Type of entity (Organization, person...).
     */
    private EntityType type;

    /**
     * Classes URIs.
     */
    private List<String> classes;

    /**
     * Initialize an entity with a raw source.
     */
    public Entity() {
        this.source = EntitySource.RAW;
    }

    /**
     * Initialize an entity with a source and a value.
     *
     * @param s Entity source
     * @param v Entity value depending on source
     */
    public Entity(final EntitySource s, final String v) {
        this.source = s;
        this.value = v;
    }

    /**
     * Initialize an entity.
     *
     * @param s Entity source
     * @param v Entity value depending on source
     * @param et Entity type
     */
    public Entity(final EntitySource s,
                  final String v,
                  final EntityType et) {
        this.source = s;
        this.value = v;
        this.type = et;
    }


    /**
     * Initialize an entity.
     *
     * @param s Entity source
     * @param v Entity value depending on source
     * @param et Entity type
     * @param tt Token type equivalent to entity type, if eligible
     */
    public Entity(final EntitySource s,
                  final String v,
                  final EntityType et,
                  final TokenType tt) {
        this.source = s;
        this.value = v;
        this.type = et;
        this.tokenType = tt;
    }

    /**
     * Initialize an entity.
     *
     * @param s Entity source
     * @param v Entity value depending on source
     * @param et Entity type
     * @param c Confidence score
     */
    public Entity(final EntitySource s,
                  final String v,
                  final EntityType et,
                  final Float c) {
        this.source = s;
        this.value = v;
        this.type = et;
        this.confidence = c;
    }


    /**
     * Fully initialize an entity.
     *
     * @param s Entity source
     * @param v Entity value depending on source
     * @param et Entity type
     * @param tt Token type equivalent to entity type, if eligible
     * @param c Confidence score
     */
    public Entity(final EntitySource s,
                  final String v,
                  final EntityType et,
                  final TokenType tt,
                  final Float c) {
        this.source = s;
        this.value = v;
        this.type = et;
        this.tokenType = tt;
        this.confidence = c;
    }


    @Override
    public final String toString() {
        return source + ":" + value + " {" + type + "/" + confidence + "}";
    }

    /**
     * Get unique identifier of entity (source + ":" + value).
     *
     * @return Unique ID for this entity
     */
    public final String getId() {
        return source + ":" + value;
    }

    /**
     * Get classes.
     *
     * @return Classes
     */
    public final List<String> getClasses() {
        return classes;
    }

    /**
     * Set classes.
     *
     * @param c Classes
     */
    public final void setClasses(final List<String> c) {
        this.classes = c;
    }

    /**
     * Check if result has class.
     *
     * @param c Class URI
     * @return True if result has given class
     */
    public final boolean hasClass(final String c) {
        return classes != null && classes.contains(c);
    }

    /**
     * Add a class.
     *
     * @param c Class URI
     */
    public final void addClass(final String c) {
        if (classes == null) {
            classes = new ArrayList<String>();
        } else if (classes.contains(c)) {
            return;
        }

        classes.add(c);
    }

    /**
     * Get entity URL or raw value.
     *
     * @return Entity URL or raw value
     */
    public final String getValue() {
        return value;
    }

    /**
     * Set entity RDF or raw value.
     *
     * @param v Entity RDF or raw value
     */
    public final void setValue(final String v) {
        this.value = v;
    }

    /**
     * Entity attributes.
     */
    private HashMap<String, String> attributes = new HashMap<String, String>();

    /**
     * Get an attribute.
     *
     * @param k Key
     * @return Value
     */
    public final String get(final String k) {
        return attributes.get(k);
    }

    /**
     * Set an attribute.
     *
     * @param k Key
     * @param v Value
     */
    public final void set(final String k, final String v) {
        attributes.put(k, v);
    }

    /**
     * Get confidence score.
     *
     * @return Confidence score
     */
    public final Float getConfidence() {
        return confidence;
    }

    /**
     * Set confidence score.
     *
     * @param c Confidence score
     */
    public final void setConfidence(final Float c) {
        this.confidence = c;
    }

    /**
     * Get token type, if eligible.
     *
     * @return Token type or null ot no token type is eligible
     */
    public final TokenType getTokenType() {
        return tokenType;
    }

    /**
     * Set token type.
     *
     * @param tt Token type
     */
    public final void setTokenType(final TokenType tt) {
        this.tokenType = tt;
    }

    /**
     * Get entity source (RDF, techcrunch).
     *
     * @return Entity source
     */
    public final EntitySource getSource() {
        return source;
    }

    /**
     * Set entity source (RDF, techcrunch).
     *
     * @param s Entity source
     */
    public final void setSource(final EntitySource s) {
        this.source = s;
    }

    /**
     * Get entity type (company, organization...).
     *
     * @return Entity type
     */
    public final EntityType getType() {
        return type;
    }

    /**
     * Set entity type (company, organization...).
     *
     * @param t Entity type
     */
    public final void setType(final EntityType t) {
        this.type = t;
    }


}
