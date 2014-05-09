package com.ulflander.app.model;

/**
 * Status for model executions.
 */
public enum JobStatus {

    /**
     * Status at instanciation of a new model class.
     */
    VOID,

    /**
     * Customer key not validated.
     */
    UNAUTHORIZED,

    /**
     * Job as been inited (properties set).
     */
    INITED,

    /**
     * Job as been queued.
     */
    QUEUED,

    /**
     * Job as been extracted (JobDocument replace by Document).
     */
    EXTRACTED,


    /**
     * Job is currently running.
     */
    RUNNING,

    /**
     * Job successfully finished.
     */
    SUCCESSFULL,

    /**
     * Job didn't finish very well.
     */
    FAILED

}
