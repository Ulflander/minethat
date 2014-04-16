package com.ulflander;

import com.ulflander.application.model.Document;
import org.junit.Before;

public class AbstractTest {

    protected MinethatTestService s;
    protected Document d;

    @Before
    public final void initialize () {
        s = new MinethatTestService();
        s.run();

        d = new Document("This is a test.");
    }

}
