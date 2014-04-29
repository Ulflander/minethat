package com.ulflander.mining.processors;

import com.ulflander.AbstractTest;
import com.ulflander.app.model.DocumentStatus;
import org.junit.Assert;
import org.junit.Test;

public class ProcessorRequirementsTest extends AbstractTest {


    @Test
    public void RequirementOrderTest () {

        s.addProcessor("extract.TokenCleaner");
        s.addProcessor("extract.DocumentSplitter");

        s.submit(d);

        Assert.assertEquals("Fucking up with order of processors should set document status to FAILED", DocumentStatus.FAILED, d.getStatus());
    }

}
