package com.ulflander.application;

import com.ulflander.MinethatTestService;
import com.ulflander.application.Conf;
import org.junit.Assert;
import org.junit.Test;

public class ConfTest {

    @Test
    public void testBaseConf () {
        MinethatTestService service = new MinethatTestService();
        Assert.assertEquals("Conf should be valid for local context", "2c7f9a", Conf.getDefaultCID());
    }
}
