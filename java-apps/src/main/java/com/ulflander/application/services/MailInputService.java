package com.ulflander.application.services;

import com.ulflander.application.Conf;
import com.ulflander.application.model.Job;
import com.ulflander.application.model.JobFactory;
import com.ulflander.application.model.JobGateway;
import com.ulflander.application.model.JobStatus;
import com.ulflander.application.model.storage.JobStorage;
import com.ulflander.application.services.email.IMAP4Gmail;
import com.ulflander.application.services.email.IMAPEmail;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Service that retrieve jobs from emails.
 *
 * @author Ulflander <xlaumonier@gmail.com>
 * @since 2/21/14
 */
public class MailInputService extends RabbitService {

    /**
     * Time of sleep in email retrieval infinite loop.
     */
    public static final int SLEEP_TIME = 3000;

    /**
     * Logger.
     */
    private static final Logger LOGGER =
        LogManager.getLogger(MailInputService.class);

    /**
     * Gmail IMAP inbox.
     */
    private IMAP4Gmail inbox;

    /**
     * Creates a new MailInputService.
     *
     * @see com.ulflander.application.services.Service
     */
    public MailInputService() {
        super();
    }

    /**
     * Creates a new MailInputService with given arguments.
     *
     * @see com.ulflander.application.services.Service
     * @param args Service arguments
     */
    public MailInputService(final String[] args) {
        super(args);
    }


    /**
     * Create instance of current class, set verbosity, and run service.
     *
     * @param args No arguments needed!
     * @throws Exception May throw an exception :p
     */
    public static void main(final String[] args) throws Exception {
        LOGGER.debug("Initiating service...");
        Service s = new MailInputService(args);
        s.run();
    }

    /**
     * Run service: initialize, then check inbox regularly.
     *
     * @throws ServiceException In case IMAP inbox or rabbit connections fail
     */
    public final void run() throws ServiceException {

        if (!initInbox()) {
            throw new ServiceException("Service not runnable: "
                                        + "inbox not connected.", this);
        }

        if (!initRabbit()) {
            throw new ServiceException("Service not runnable: "
                                        + "RabbitMQ not connected.", this);
        }

        while (true) {
            emailsToJob(inbox.getUnreadMessages());

            try {
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException e) {
                LOGGER.error("Email service has been interrupted", e);
            }

            if (!getLoop()) {
                break;
            }
        }
    }

    /**
     * Initializes mail inbox connection.
     *
     * @return True if inbox is connected, false otherwise
     */
    private Boolean initInbox() {
        inbox = new IMAP4Gmail();
        return inbox.connect();
    }


    /**
     * Transform each email into a model, store it into mongo, call 0mq for
     * processing the model in MinerService.
     *
     * @param emails List of emails to transform into jobs
     */
    private void emailsToJob(final List<IMAPEmail> emails) {

        for (IMAPEmail e : emails) {
            LOGGER.debug("[MailInputService] Got an email from "
                    + e.getFrom());

            Set<Job> jobs = JobFactory.fromEmail(e);

            for (Job job : jobs) {
                job.setStatus(JobStatus.INITED);
                job.setGateway(JobGateway.EMAIL);
                job.setStart(new Date());

                String id = JobStorage.insert(job);

                if (id == null) {
                    LOGGER.error("Unable to init model: not stored in DB", job);
                    continue;
                }

                publish(Conf.EXTRACTOR_QUEUE_NAME, id, MSG_PUBLICATION_TRIES);
            }
        }

    }

}
