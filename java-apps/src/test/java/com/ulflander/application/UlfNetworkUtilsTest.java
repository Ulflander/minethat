package com.ulflander.application;

import com.ulflander.application.utils.UlfFileUtils;
import com.ulflander.application.utils.UlfNetworkUtils;
import org.apache.logging.log4j.LogManager;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Ulflander <xlaumonier@gmail.com>
 * @since 2/27/14
 */
public class UlfNetworkUtilsTest {

    public static final String TEST_URL = "http://example.com/";

    private String getTestFile() {

        URL url = Thread.currentThread()
            .getContextClassLoader()
            .getResource("com/ulflander/application/utils/example.com.html");

        if (url == null) {
            LogManager
                .getLogger(UlfNetworkUtilsTest.class)
                .error("Resource not found: "
                    + "com/ulflander/application/utils/example.com.html");
        }

        return UlfFileUtils.read(new File(url.getPath()));
    }
    @Test
    public void fromContentURLTest () throws MalformedURLException {
        URL u = new URL(TEST_URL);
        String page = UlfNetworkUtils.getContent(u);

        String model = getTestFile();

        Assert.assertEquals("Downloaded content should be as the model", model, page);
    }

    @Test
    public void fromContentStringTest () {
        String page = UlfNetworkUtils.getContent(TEST_URL);

        String model = getTestFile();

        Assert.assertEquals("Downloaded content should be as the model", model, page);
    }


}
