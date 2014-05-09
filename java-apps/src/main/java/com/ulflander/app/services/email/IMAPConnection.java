package com.ulflander.app.services.email;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.FlagTerm;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * An IMAPConnection to be extended.
 *
 * @author Ulflander <xlaumonier@gmail.com>
 * @since 2/21/14
 */
public abstract class IMAPConnection {

    /**
     * Logger.
     */
    private static final Logger LOGGER =
        LogManager.getLogger(IMAPConnection.class);

    /**
     * Hostname.
     */
    private String host;

    /**
     * Login.
     */
    private String login;

    /**
     * Password.
     */
    private String pass;

    /**
     * Mail store.
     */
    private Store store;

    /**
     * Test mode.
     */
    private Boolean testMode = false;

    /**
     * Get current test mode value.
     *
     * @return True if test mode active, false otherwise
     */
    public final Boolean getTestMode() {
        return testMode;
    }

    /**
     * Set test mode.
     *
     * @param t True if test mode activation required, false otherwise
     */
    public final void setTestMode(final Boolean t) {
        this.testMode = t;
    }

    /**
     * Get hostname.
     *
     * @return Host
     */
    public final String getHost() {
        return host;
    }

    /**
     * Set hostname.
     *
     * @param h Host name
     */
    public final void setHost(final String h) {
        this.host = h;
    }

    /**
     * Get login.
     *
     * @return Login
     */
    public final String getLogin() {
        return login;
    }

    /**
     * Set login.
     *
     * @param l Login to use
     */
    public final void setLogin(final String l) {
        this.login = l;
    }

    /**
     * Set pass.
     *
     * @param p Pass to use
     */
    public final void setPass(final String p) {
        this.pass = p;
    }

    /**
     * Connect to IMAP.
     *
     * @return True if store is connected, false otherwise
     */
    public final Boolean connect() {

        Properties props = System.getProperties();
        props.setProperty("mail.store.protocol", "imaps");

        try {

            Session session = Session.getDefaultInstance(props, null);
            store = session.getStore("imaps");
            store.connect("imap." + this.host + ".com", this.login, this.pass);

        } catch (NoSuchProviderException e) {

            LOGGER.error("An exception occurs at "
                + this.getClass().getName() + ".connect()", e);

            return false;
        } catch (MessagingException e) {

            LOGGER.error("An exception occurs at "
                + this.getClass().getName() + ".connect()", e);

            return false;
        }

        return store.isConnected();
    }

    /**
     * Get a list of unread messages.
     *
     * @return List of emails.
     */
    public final List<IMAPEmail> getUnreadMessages() {

        List<IMAPEmail> result = new ArrayList<IMAPEmail>();

        if (!store.isConnected()) {
            return result;
        }

        Folder folder = null;

        try {
            folder = store.getFolder("INBOX");
        } catch (MessagingException e1) {
            LOGGER.error("Unable to get folder INBOX", e1);
        }


        try {
            if (folder == null || !folder.exists()) {
                return result;
            }
        } catch (MessagingException e1) {
            LOGGER.error("Folder INBOX does not exists", e1);
        }


        try {
            folder.open(Folder.READ_WRITE);
        } catch (MessagingException e1) {
            LOGGER.error("Unable to open folder INBOX", e1);
        }

        FlagTerm ft = new FlagTerm(new Flags(Flags.Flag.SEEN), false);
        Message[] messages;

        try {

            folder.getMessages();
            messages = folder.search(ft);

            for (Message message : messages) {
                IMAPEmail msg = IMAPEmail.convert(message);
                if (!result.contains(msg)) {
                    result.add(msg);
                }

                if (getTestMode()) {
                    message.setFlag(Flags.Flag.SEEN, false);
                }
            }


        } catch (Exception e) {
            LOGGER.error("Error while reading messages "
                + this.getClass().getName(), e);
        }

        try {
            folder.close(false);
        } catch (MessagingException e1) {
            LOGGER.error("Unable to get folder INBOX", e1);
        }

        return result;
    }

    /**
     * Close IMAP connection.
     */
    public final void close() {
        if (store != null && store.isConnected()) {
            try {
                store.close();
            } catch (MessagingException e) {
                LOGGER.error("Unable to close store", e);
            }
        }
    }
}
