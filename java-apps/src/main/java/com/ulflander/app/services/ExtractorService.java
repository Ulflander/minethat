package com.ulflander.app.services;

import com.rabbitmq.client.QueueingConsumer;
import com.ulflander.app.Conf;
import com.ulflander.app.model.Document;
import com.ulflander.app.model.DocumentStatus;
import com.ulflander.app.model.Job;
import com.ulflander.app.model.JobDocumentType;
import com.ulflander.app.model.JobStatus;
import com.ulflander.app.model.storage.DocumentStorage;
import com.ulflander.app.model.storage.JobStorage;
import com.ulflander.mining.ExtractionException;
import com.ulflander.mining.TextExtractor;
import com.ulflander.utils.UlfHashUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;

/**
 * This service takes as input jobs coming from MailInputService or web API,
 * then extract documents, and finally send them to MinerService.
 *
 *
 * @author Ulflander <xlaumonier@gmail.com>
 * @since 3/1/14
 */
public class ExtractorService extends RabbitService {


    /**
     * Logger.
     */
    private static final Logger LOGGER =
        LogManager.getLogger(ExtractorService.class);

    /**
     * Creates a new ExtractorService with given arguments.
     *
     * @see com.ulflander.app.services.Service
     * @param args Service arguments
     */
    public ExtractorService(final String[] args) {
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
        Service s = new ExtractorService(args);
        s.run();
    }

    @Override
    public final void run() throws ServiceException {
        if (!initRabbit()) {
            throw new ServiceException("Service not runnable: "
                    + "RabbitMQ not connected.", this);
        }

        QueueingConsumer consumer = new QueueingConsumer(getRabbitChannel());

        try {
            getRabbitChannel().basicConsume(Conf.EXTRACTOR_QUEUE_NAME,
                                            true, consumer);
        } catch (IOException e) {
            throw new ServiceException("Service not runnable: "
                    + "RabbitMQ queue consuming failed.", this, e);
        }

        listen(consumer);
    }

    /**
     * Listen to extractor queue and extract given jobs documents.
     *
     * @param c Rabbit queuing consumer
     */
    private void listen(final QueueingConsumer c) {
        while (true) {
            QueueingConsumer.Delivery delivery;

            try {
                delivery = c.nextDelivery();
            } catch (InterruptedException e) {
                LOGGER.error("A message has been skipped!", e);
                continue;
            }

            String jobId = new String(delivery.getBody());

            // Why would it put some """" ????? Related to node.js API
            if (jobId.substring(0, 1).equals("\"")) {
                jobId = jobId.substring(1, jobId.length() - 1);
            }

            LOGGER.debug("[ExtractorService] Get model " + jobId);

            extract(jobId);
        }
    }

    /**
     * Extract documents for a model.
     *
     * @param jobId ID of model
     */
    private void extract(final String jobId) {
        Job job = JobStorage.get(jobId);

        if (job == null) {
            LOGGER.error("Unable to retrieve model " + jobId);
            return;
        }

        Document doc;

        try {
            doc = TextExtractor.fromJobDocument(job);

            HashMap<String, Object> meta = job.getMeta();
            for (String key: meta.keySet()) {
                doc.ensureProperty("meta", key, meta.get(key));
            }

            if (job.getType() == JobDocumentType.FEED_URL) {
                doc.ensureProperty("meta", "doc_url", job.getValue());
            }

            // URL fingerprint
            if (doc.hasProperty("meta", "doc_url")) {
                doc.addProperty("meta", "url_fingerprint",
                        UlfHashUtils.sha256(
                                (String) doc.getProperty("meta", "doc_url")));

            } else if (doc.hasProperty("meta", "url")) {
                doc.addProperty("meta", "url_fingerprint",
                        UlfHashUtils.sha256(
                                (String) doc.getProperty("meta", "url")));
            }


            if (findDuplicates(
                    (String) doc.getProperty("meta", "doc_fingerprint"),
                    (String) doc.getProperty("meta", "url_fingerprint"))) {

                job.setStatus(JobStatus.DUPLICATE);
                JobStorage.update(job);
                LOGGER.info("Extraction of job " + jobId
                        + " stopped: duplicate.");
                return;
            }

            doc.setStatus(DocumentStatus.EXTRACTED);
            DocumentStorage.insert(doc);
        } catch (ExtractionException e) {
            job.setStatus(JobStatus.FAILED);
            JobStorage.update(job);
            LOGGER.warn("Extraction of model " + jobId + " failed.", e);
            return;
        } catch (Exception e) {
            job.setStatus(JobStatus.FAILED);
            JobStorage.update(job);
            LOGGER.error("Bad failure on extraction " + jobId, e);
            return;
        }

        job.setDocument(doc.getId());
        job.setStatus(JobStatus.EXTRACTED);

        try {
            JobStorage.update(job);
        } catch (Exception e) {
            LOGGER.error("Update after extraction of model "
                        + jobId + " failed.", e);
            return;
        }

        LOGGER.debug("Document extracted and stored: "
                + job.getDocument() + "");

        publish(Conf.EXECUTOR_QUEUE_NAME, jobId, MSG_PUBLICATION_TRIES);
    }

    /**
     * Attempt to find some existing documents with this URL.
     *
     * @param docFingerprint Document text fingerprint
     * @param urlFingerprint Document URL fingerprint
     * @return True if a document exists with that URL, false otherwise
     */
    private boolean findDuplicates(final String docFingerprint,
                                   final String urlFingerprint) {
        return DocumentStorage.getByDocFingerprint(docFingerprint) != null
            && (urlFingerprint == null
                || DocumentStorage.getByUrlFingerprint(urlFingerprint) != null);
    }
}
