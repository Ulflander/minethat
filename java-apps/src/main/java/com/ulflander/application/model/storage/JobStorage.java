package com.ulflander.application.model.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import com.ulflander.application.model.Job;
import com.ulflander.application.model.JobStatus;
import com.ulflander.application.utils.MongoAccessor;

/**
 * Store and retrieve jobs from MongoDB.
 *
 *
 * @author Ulflander <xlaumonier@gmail.com>
 * @since 2/28/14
 */
public final class JobStorage extends Storage {

    /**
     * Private constructor.
     */
    private JobStorage() {

    }

    /**
     * Save a model in MongoDB.
     *
     * @param job Job
     * @return Identifier of model
     */
    public static String insert(final Job job) {

        if (job.getStatus() == JobStatus.VOID) {
            job.setStatus(JobStatus.INITED);
        }

        DBObject obj = fromJob(job);
        MongoAccessor.getJobs().insert(obj);

        job.setId(obj.get("_id").toString());

        return job.getId();
    }


    /**
     * Update a model in MongoDB (Only updates status, processingLength).
     *
     * @param job Job
     * @return Identifier of model
     */
    public static String update(final Job job) {
        DBObject obj = fromJob(job);
        DBObject idObj = getDBObject(job.getId(), MongoAccessor.getJobs());
        MongoAccessor.getJobs().update(idObj, obj);

        return idObj.get("_id").toString();
    }

    /**
     * Get a model from MongoDB.
     *
     * @param id Job identifier
     * @return Job object
     */
    public static Job get(final String id) {
        return (Job) get(id, Job.class, MongoAccessor.getJobs());
    }

    /**
     * Create a DBObject from a Job object.
     *
     * @param job Job to convert into DBObject
     * @return DBObject to be used along with MongoAccessor
     */
    private static DBObject fromJob(final Job job) {
        GsonBuilder gsonAdapter = GsonTypeAdapter
                .getGsonBuilder(GsonTypeAdapter.AType.SERIALIZER);
        Gson gson = gsonAdapter.create();
        return (BasicDBObject) JSON.parse(gson.toJson(job));
    }
}
