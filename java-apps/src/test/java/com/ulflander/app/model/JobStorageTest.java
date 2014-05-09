package com.ulflander.app.model;

import com.ulflander.app.Conf;
import com.ulflander.app.Env;
import com.ulflander.app.model.storage.JobStorage;
import com.ulflander.utils.MongoAccessor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ulflander <xlaumonier@gmail.com>
 * @since 2/28/14
 */
public class JobStorageTest {

    @Before
    public void readConf() {
        Conf.read();
        MongoAccessor.useDatabase(Env.TEST);
    }

    @Test
    public void testInsertGetNotNull() {

        Job job = new Job();
        job.setCustomerId(Conf.getDefaultCID());
        String id = JobStorage.insert(job);

        Job returned = JobStorage.get(id);

        Assert.assertNotNull("Job shouldn't be null", returned);
    }


    @Test
    public void testInsertGetGoodValue() {

        Job job = new Job();
        job.setCustomerId(Conf.getDefaultCID());
        String id = JobStorage.insert(job);

        Job returned = JobStorage.get(id);

        Assert.assertEquals("Job retrieved should have good id",
            Conf.getDefaultCID(), returned.getCustomerId());
    }



    @Test
    public void testAutoUpdateJobStatus() {

        Job job = new Job();
        job.setCustomerId(Conf.getDefaultCID());

        Assert.assertEquals("Job just created should be VOID",
            JobStatus.VOID, job.getStatus());

        String id = JobStorage.insert(job);

        Job returned = JobStorage.get(id);

        Assert.assertEquals("Job saved in database should be INITED",
            JobStatus.INITED, returned.getStatus());

        Assert.assertEquals("Job sent to database should be INITED",
            JobStatus.INITED, job.getStatus());
    }


    @Test
    public void testUpdateJobStatus() {

        Job job = new Job();
        job.setCustomerId(Conf.getDefaultCID());

        Assert.assertEquals("Job just created should be VOID",
            JobStatus.VOID, job.getStatus());

        String id = JobStorage.insert(job);

        job.setStatus(JobStatus.FAILED);
        JobStorage.update(job);

        System.out.println(id);

        Job returned = JobStorage.get(id);

        Assert.assertEquals("Job sent to database should be INITED",
            JobStatus.FAILED, returned.getStatus());
    }

    @Test
    public void testGetGateway() {

        Job job = new Job();
        job.setCustomerId(Conf.getDefaultCID());


        Assert.assertEquals("Job just created should be from gateway TEST",
            JobGateway.TEST, job.getGateway());

        job.setGateway(JobGateway.WEB);

        String id = JobStorage.insert(job);

        Job returned = JobStorage.get(id);

        Assert.assertEquals("Job retrieved should have good getway",
            JobGateway.WEB, returned.getGateway());
    }

    @Test
    public void testGetJobDocuments() {

        List<String> procs = new ArrayList<String>();
        procs.add("TestProcessor");
        Job job = JobFactory.fromString("this is a test.", procs);

        String id = JobStorage.insert(job);

        Job returned = JobStorage.get(id);

        Assert.assertEquals("Job retrieved should have one proc",
                1, returned.getProcessors().size());

    }


}
