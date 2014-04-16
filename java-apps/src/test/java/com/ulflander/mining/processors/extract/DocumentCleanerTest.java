package com.ulflander.mining.processors.extract;

import com.ulflander.AbstractTest;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

public class DocumentCleanerTest extends AbstractTest {

    @Before
    public void initProcessors () {
        s.addProcessor("extract.DocumentCleaner");

    }

    @Test
    public void rLineTest () {

        d.setRaw("This got a \r new line");
        s.submit(d);

        Assert.assertEquals("\\r new lines should be replaced by \\n new lines", "This got a \n new line", d.getRaw());
    }

    @Test
    public void rnLineTest () {

        d.setRaw("This got a \r\n new line");
        s.submit(d);

        Assert.assertEquals("\\r\\n new lines should be replaced by \\n new lines", "This got a \n new line", d.getRaw());
    }

    @Test
    public void trimTest () {

        d.setRaw("\n  This got a \r\n new line \n\r \n");
        s.submit(d);

        Assert.assertEquals("Triming should remove edge new lines and spaces", "This got a \n new line", d.getRaw());
    }
}
