package com.ulflander.app.services.email;

import com.ulflander.AbstractTest;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
public class IMAP4GmailTest extends AbstractTest {

    @Test
    public void connectionTest () {
        IMAP4Gmail inbox = new IMAP4Gmail();
        inbox.setTestMode(true);
        inbox.connect();

        List<IMAPEmail> unread = inbox.getUnreadMessages();

        Assert.assertNotSame("Get unread messages function should return a list", null, unread);

        inbox.close();
    }

}
