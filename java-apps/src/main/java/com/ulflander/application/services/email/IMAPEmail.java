package com.ulflander.application.services.email;


import com.ulflander.application.utils.UlfStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.InternetAddress;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * IMAPEmail is a Mail object retrieved or sent via IMAP.
 *
 * @author Ulflander <xlaumonier@gmail.com>
 * @since 2/21/14
 */
public class IMAPEmail {

    /**
     * Logger.
     */
    private static final Logger LOGGER =
        LogManager.getLogger(IMAPEmail.class);

    /**
     * 4k.
     */
    private static final int SIZE_4096 = 4096;

    /**
     * Used by hashCode.
     */
    private static final int SUBJECT_HASHCODE_MULTIPLIER = 17;

    /**
     * Used by hashCode.
     */
    private static final int CONTENT_HASHCODE_MULTIPLIER = 31;

    /**
     * Email subject.
     */
    private String subject = "";

    /**
     * Email content.
     */
    private String content = "";

    /**
     * Email sender address.
     */
    private String from = "";

    /**
     * Email sent date.
     */
    private long sentDate = 0;

    /**
     * List of attachments as files.
     */
    private List<File> attachments = new ArrayList<File>();

    /**
     * Add an attachment.
     *
     * @param f Attached file
     */
    public final void addAttachment(final File f) {
        attachments.add(f);
    }

    /**
     * Remove an attachment.
     *
     * @param f Attached file to remove
     */
    public final void removeAttachment(final File f) {
        attachments.remove(f);
    }

    /**
     * Get number of attached files.
     *
     * @return Number of files attached to this email
     */
    public final int getAttachmentsSize() {
        return attachments.size();
    }

    /**
     * Get an attachment by index.
     *
     * @param index Index of attached file
     * @return Attached file
     */
    public final File getAttachment(final int index) {
        if (index >= getAttachmentsSize()) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return attachments.get(index);
    }

    /**
     * Get subject.
     *
     * @return Email subject
     */
    public final String getSubject() {
        return subject;
    }

    /**
     * Set subject.
     *
     * @param s Email subject
     */
    public final void setSubject(final String s) {
        this.subject = s;
    }

    /**
     * Get email content.
     *
     * @return Email content
     */
    public final String getContent() {
        return content;
    }

    /**
     * Set email content.
     *
     * @param c Email content
     */
    public final void setContent(final String c) {
        this.content = c;
    }

    /**
     * Set email content from an InputStream.
     *
     * @param c Email content as InputStream
     */
    public final void setContent(final InputStream c) {
        this.content = UlfStringUtils.getStringFromInputStream(c);
    }

    /**
     * Get email sent date as timestamp.
     *
     * @return Email sent date
     */
    public final long getSentDate() {
        return sentDate;
    }

    /**
     * Set email sent date as timestamp.
     *
     * @param s Email sent date
     */
    public final void setSentDate(final long s) {
        this.sentDate = s;
    }

    /**
     * Get sender email address.
     *
     * @return Email sender address
     */
    public final String getFrom() {
        return from;
    }

    /**
     * Set sender email address.
     *
     * @param f Sender email address
     */
    public final void setFrom(final String f) {
        this.from = f;
    }

    /**
     * Check if this IMAPEmail if equals with another one.
     *
     * @param obj An object to compare to
     * @return True if object given is the same IMAPEmail, false otherwise
     */
    @Override
    public final boolean equals(final Object obj) {
        if (obj instanceof IMAPEmail) {
            IMAPEmail msg = (IMAPEmail) obj;

            if (this.subject == null && msg.getSubject() != null) {
                return false;
            }

            if (this.content == null && msg.getContent() != null) {
                return false;
            }

            if (this.from == null && msg.getFrom() != null) {
                return false;
            }

            if (this.subject.equals(msg.getSubject())
                && this.content.equals(msg.getContent())
                && this.from.equals(msg.getFrom())
                && this.sentDate == (msg.getSentDate())) {

                return true;
            }
        }
        return false;
    }

    /**
     * Get the hash code of this IMAPEmail.
     *
     * @return Hash code
     */
    @Override
    public final int hashCode() {

        int hash = 1;
        int s = 0;
        int c = 0;

        if (subject != null) {
            s = subject.hashCode();
        }

        if (content != null) {
            c = content.hashCode();
        }

        hash = hash * SUBJECT_HASHCODE_MULTIPLIER + s;
        hash = hash * CONTENT_HASHCODE_MULTIPLIER + c;

        return hash;
    }

    /**
     * Convert a javax.mail.Message to an IMAPEmail.
     *
     * @param message Message to convert
     * @return IMAPEmail generated from Message
     * @throws IOException        In case of attachement error
     * @throws MessagingException In case of conversion error
     */
    public static IMAPEmail convert(final Message message)
        throws IOException, MessagingException {

        IMAPEmail msg = new IMAPEmail();

        msg.setSubject(message.getSubject());
        msg.setContent(message.getInputStream());
        msg.setFrom(InternetAddress.toString(message.getFrom()));
        msg.setSentDate(message.getSentDate().getTime());
        extractAttachments(msg, (Multipart) message.getContent());

        return msg;
    }

    /**
     * Extract attachments.
     *
     * @param email     Email object
     * @param multipart Message Multipart content
     * @throws MessagingException In case of conversion error
     */
    private static void extractAttachments(final IMAPEmail email,
                                           final Multipart multipart)
        throws MessagingException {

        for (int i = 0, l = multipart.getCount(); i < l; i++) {
            BodyPart bodyPart = multipart.getBodyPart(i);

            if (!Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition())
                && !StringUtils.isNotBlank(bodyPart.getFileName())) {
                continue; // dealing with attachments only
            }

            FileOutputStream fos = null;
            InputStream is = null;
            File f = null;

            try {
                is = bodyPart.getInputStream();
                f = new File("/tmp/" + bodyPart.getFileName());
                fos = new FileOutputStream(f);
            } catch (IOException e) {
                LOGGER.error("Unable to retrieve body part", e);
                continue;
            }

            byte[] buf = new byte[SIZE_4096];
            int bytesRead;

            try {
                while ((bytesRead = is.read(buf)) != -1) {
                    fos.write(buf, 0, bytesRead);
                }
            } catch (IOException e) {
                LOGGER.error("Unable to read body part", e);
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        LOGGER.error("Unable to close "
                            + "body part file output stream", e);
                    }
                }
            }


            email.addAttachment(f);
        }
    }

}
