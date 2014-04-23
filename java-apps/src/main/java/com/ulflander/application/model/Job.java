package com.ulflander.application.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * A model holds information about documents to be processed,
 * processors to run on them or presets of processors to use,
 * and also about the customer that initiated the model.
 *
 * @since 2/24/14
 * @author Ulflander <xlaumonier@gmail.com>
 */
public class Job implements Serializable, Storable {

    /**
     * Job identifier.
     */
    private String id = "0";

    /**
     * Start timestamp.
     */
    @Expose
    private Date start;

    /**
     * End timestamp.
     */
    @Expose
    private Date end;

    /**
     * Type of model document.
     */
    @Expose
    private JobDocumentType type = JobDocumentType.UNKNOWN;

    /**
     * Value of JobDocument (raw text, url...).
     */
    @Expose
    private String value;

    /**
     * Status of model.
     */
    @Expose
    private JobStatus status = JobStatus.VOID;

    /**
     * Gateway where model was submitted.
     */
    @Expose
    private JobGateway gateway = JobGateway.TEST;

    /**
     * Document to process.
     */
    @Expose
    private String document;

    /**
     * Processors to run.
     */
    @Expose
    private ArrayList<JobProcessor> processors;

    /**
     * Customer id.
     */
    @Expose
    private String customerId;

    /**
     * Email address to send result to.
     */
    @Expose
    private String email;

    /**
     * Meta data from Job submission, will be copied to document properties.
     */
    private HashMap<String, Object> meta = new HashMap<String, Object>();

    /**
     * Target (mining or training, default: mining).
     */
    private JobTarget target = JobTarget.MINE;

    /**
     * If target is training, requires one or more classes.
     */
    private String classes = new String();



    /**
     * Get model ID value.
     *
     * @return Job ID
     */
    public final String getId() {
        return id;
    }

    /**
     * Set model ID value.
     *
     * @param i Job ID
     */
    public final void setId(final String i) {
        this.id = i;
    }


    /**
     * Get start timestamp value.
     *
     * @return Start timestamp
     */
    public final Date getStart() {
        return start;
    }

    /**
     * Set start timestamp value.
     *
     * @param ts Start timestamp
     */
    public final void setStart(final Date ts) {
        this.start = ts;
    }

    /**
     * Get end timestamp value.
     *
     * @return End timestamp
     */
    public final Date getEnd() {
        return end;
    }

    /**
     * Set end timestamp value.
     *
     * @param ts End timestamp
     */
    public final void setEnd(final Date ts) {
        this.end = ts;
    }

    /**
     * Get Job status value.
     *
     * @return Job status
     */
    public final JobStatus getStatus() {
        return status;
    }

    /**
     * Set Job status value.
     *
     * @param js Job status
     */
    public final void setStatus(final JobStatus js) {
        this.status = js;
    }

    /**
     * Get Job gateway value.
     *
     * @return Job gateway
     */
    public final JobGateway getGateway() {
        return gateway;
    }

    /**
     * Set Job gateway value.
     *
     * @param jg Job gateway
     */
    public final void setGateway(final JobGateway jg) {
        this.gateway = jg;
    }



    /**
     * Get list of processors to run value.
     *
     * @return List of processors to run
     */
    public final ArrayList<JobProcessor> getProcessors() {
        return processors;
    }

    /**
     * Set list of processors to run value.
     *
     * @param ps List of processors to run
     */
    public final void setProcessors(final ArrayList<JobProcessor> ps) {
        this.processors = ps;
    }

    /**
     * Get customer ID value.
     *
     * @return Customer ID
     */
    public final String getCustomerId() {
        return customerId;
    }

    /**
     * Set customer ID value.
     *
     * @param cid Customer ID
     */
    public final void setCustomerId(final String cid) {
        this.customerId = cid;
    }

    /**
     * Get email address to send result to.
     *
     * @return Email address to send result to
     */
    public final String getEmail() {
        return email;
    }

    /**
     * Set email address to send result to.
     *
     * @param e Email address to send result to
     */
    public final void setEmail(final String e) {
        this.email = e;
    }

    /**
     * Get document.
     *
     * @return Document
     */
    public final String getDocument() {
        return document;
    }

    /**
     * Set document.
     *
     * @param d Document
     */
    public final void setDocument(final String d) {
        this.document = d;
    }

    /**
     * Get model document type.
     *
     * @return Job document type
     */
    public final JobDocumentType getType() {
        return type;
    }

    /**
     * Set model document type.
     *
     * @param t Job document type
     */
    public final void setType(final JobDocumentType t) {
        this.type = t;
    }

    /**
     * Get value.
     *
     * @return Value
     */
    public final String getValue() {
        return value;
    }

    /**
     * Set value.
     *
     * @param v Value
     */
    public final void setValue(final String v) {
        this.value = v;
    }

    /**
     * Get meta.
     *
     * @return Meta
     */
    public final HashMap<String, Object> getMeta() {
        return meta;
    }

    /**
     * Set meta.
     *
     * @param o Meta
     */
    public final void setMeta(final HashMap<String, Object> o) {
        this.meta = o;
    }

    /**
     * Get job target (mining or training).
     *
     * @return Job target (mining or training)
     */
    public final JobTarget getTarget() {
        return target;
    }

    /**
     * Set job target (mining or training).
     *
     * @param jt Job target (mining or training)
     */
    public final void setTarget(final JobTarget jt) {
        this.target = jt;
    }

    /**
     * Get job training classes.
     *
     * @return Job training classes
     */
    public final String getClasses() {
        return classes;
    }

    /**
     * Set job training classes.
     *
     * @param cs Job training classes
     */
    public final void setClasses(final String cs) {
        this.classes = cs;
    }


}
