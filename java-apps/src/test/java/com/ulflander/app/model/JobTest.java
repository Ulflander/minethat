package com.ulflander.app.model;


import com.ulflander.AbstractTest;
import com.ulflander.app.Conf;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class JobTest extends AbstractTest {

    @Before
    public void setupProcessors () {

    }

    @Test
    public void BasicTest () {

        d.setSurface("Ce texte est manifestement en français. L'objectif est que la reconnaissance de langue soit efficace et que ces quelques mots soient considérés étant français.");
        s.submit(d);

        Job job = new Job ();
        job.setCustomerId(Conf.getDefaultCID());

        ArrayList<JobProcessor> procs = new ArrayList<JobProcessor>();
        procs.add(new JobProcessor("DocumentSplitter"));

        job.setProcessors(procs);

        Assert.assertEquals("There should be one processor", 1, job.getProcessors().size());

    }

}
