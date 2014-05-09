package com.ulflander.app.model;

import com.ulflander.app.services.email.IMAPEmail;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Ulflander <xlaumonier@gmail.com>
 * @since 3/1/14
 */
public class JobFactoryTest {

    File file = new File(Thread.currentThread()
        .getContextClassLoader()
        .getResource("com/ulflander/utils/example.com.html").getPath());


    @Test
    public void testFromEmailCoreContentSize() {
        IMAPEmail email = new IMAPEmail();
        email.setFrom("xlaumonier@gmail.com");
        email.setContent("This is just a core content");
        Set<Job> js = JobFactory.fromEmail(email);
        Assert.assertEquals("Job from email with core content should have one JobDocument",
            1, js.size());
    }

    @Test
    public void testFromEmailCoreContentType() {
        IMAPEmail email = new IMAPEmail();
        email.setFrom("xlaumonier@gmail.com");
        email.setContent("This is just a core content");
        Set<Job> js = JobFactory.fromEmail(email);
        Iterator it = js.iterator();
        while (it.hasNext()) {
            Job j = (Job) it.next();
            Assert.assertEquals("Job from email with core content should have one doc type as String",
                    JobDocumentType.STRING, j.getType());
            break;
        }
    }

    @Test
    public void testFromEmailURLSize() {
        IMAPEmail email = new IMAPEmail();
        email.setFrom("xlaumonier@gmail.com");
        email.setContent("http://example.com");
        Set<Job> j = JobFactory.fromEmail(email);
        Assert.assertEquals("Job from email with URL should have one JobDocument",
            1, j.size());
    }

    @Test
    public void testFromEmailURLType() {
        IMAPEmail email = new IMAPEmail();
        email.setFrom("xlaumonier@gmail.com");
        email.setContent("http://example.com");
        Set<Job> js = JobFactory.fromEmail(email);
        Iterator it = js.iterator();
        while (it.hasNext()) {
            Job j = (Job) it.next();
            Assert.assertEquals("Job from email with 1 URL should have one doc type as URL",
                JobDocumentType.URL, j.getType());
            break;
        }
    }

    @Test
    public void testFromEmailMultipleURLSize() {
        IMAPEmail email = new IMAPEmail();
        email.setFrom("xlaumonier@gmail.com");
        email.setContent("http://example.com\nhttp://google.com");
        Set<Job> j = JobFactory.fromEmail(email);
        Assert.assertEquals("Job from email with 2 URL should have two JobDocument",
            2, j.size());
    }


    @Test
    public void testFromEmailAttachementSize() {
        IMAPEmail email = new IMAPEmail();
        email.setFrom("xlaumonier@gmail.com");
        email.setContent("This is just a core content but there's an attachement");
        email.addAttachment(file);
        Set<Job> j = JobFactory.fromEmail(email);
        Assert.assertEquals("Job from email with 1 attachement should have one JobDocument",
            1, j.size());
    }
    @Test
    public void testFromEmailAttachementType() {
        IMAPEmail email = new IMAPEmail();
        email.setFrom("xlaumonier@gmail.com");
        email.setContent("This is just a core content but there's an attachement");
        email.addAttachment(file);
        Set<Job> js = JobFactory.fromEmail(email);
        Iterator it = js.iterator();
        while (it.hasNext()) {
            Job j = (Job) it.next();
            Assert.assertEquals("Job from email with 1 attachement should have one doc type as File",
                JobDocumentType.FILE, j.getType());
            break;
        }
    }
    @Test
    public void testFromEmailAttachementsSize() {
        IMAPEmail email = new IMAPEmail();
        email.setFrom("xlaumonier@gmail.com");
        email.setContent("This is just a core content but there's an attachement");
        email.addAttachment(file);
        email.addAttachment(file);
        Set<Job> j = JobFactory.fromEmail(email);
        Assert.assertEquals("Job from email with 2 attachements should have two JobDocument",
            2, j.size());
    }
}
