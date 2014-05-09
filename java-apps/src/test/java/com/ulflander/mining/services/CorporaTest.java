package com.ulflander.mining.services;

import com.ulflander.app.Conf;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Ulflander on 4/23/14.
 */
public class CorporaTest {
    @Test
    public void testQuery() {
        Conf.read();
        String[] c = new String[1];
        c[0] = "places";
        CorporaResponse r = Corpora.query("paris", c);

        Assert.assertEquals(r.getCorpora().get(0), "places");
    }
}
