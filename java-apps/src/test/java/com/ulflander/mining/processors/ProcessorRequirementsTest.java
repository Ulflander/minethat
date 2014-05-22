package com.ulflander.mining.processors;

import com.ulflander.AbstractTest;
import org.junit.Test;

public class ProcessorRequirementsTest extends AbstractTest {


    @Test
    public void RequirementOrderTest () {

        s.addProcessor("extract.TokenCleaner");
        s.addProcessor("extract.DocumentSplitter");

        s.submit(d);

    }

}
