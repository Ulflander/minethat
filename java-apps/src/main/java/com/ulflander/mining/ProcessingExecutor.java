package com.ulflander.mining;

import com.ulflander.application.Conf;
import com.ulflander.application.model.Document;
import com.ulflander.application.model.DocumentStatus;
import com.ulflander.application.model.Job;
import com.ulflander.application.model.JobDocumentType;
import com.ulflander.application.model.JobProcessor;
import com.ulflander.application.model.JobStatus;
import com.ulflander.application.model.JobTarget;
import com.ulflander.application.model.Language;
import com.ulflander.application.model.storage.DocumentStorage;
import com.ulflander.application.utils.UlfTimer;
import com.ulflander.mining.processors.ILocalizedProcessor;
import com.ulflander.mining.processors.Processor;
import com.ulflander.mining.processors.ProcessorFactory;
import com.ulflander.mining.processors.preset.BasicTextStatPreset;
import com.ulflander.mining.processors.preset.IPreset;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Executes a model: check customer key, run processors on all documents.
 *
 * @author Ulflander <xlaumonier@gmail.com>
 * @since 2/21/14
 */
public class ProcessingExecutor {

    /**
     * Is executor verbose.
     */
    private Boolean verbose = true;

    /**
     * Logger.
     */
    private static final Logger LOGGER =
        LogManager.getLogger(ProcessingExecutor.class);

    /**
     * Get verbose value.
     *
     * @return Verbose
     */
    public final Boolean getVerbose() {
        return verbose;
    }

    /**
     * Set verbose value.
     *
     * @param v Verbose
     */
    public final void setVerbose(final Boolean v) {
        this.verbose = v;
    }

    /**
     * Execute a model.
     *
     * @param job Job to be executed.
     */
    public final void execute(final Job job) {
        if (getVerbose()) {
            LOGGER.trace("  - Executing model " + job.getId());
        }

        if (!this.checkCustomerKey(job)) {
            LOGGER.error("Unauthorized model attempt with customer id '"
                + job.getCustomerId() + "'");

            job.setStatus(JobStatus.UNAUTHORIZED);
            return;
        }

        job.setStatus(JobStatus.RUNNING);

        // If document is already defined let's use it
        if (job.getDocument() != null) {
            if (process(job, DocumentStorage.get(job.getDocument()))) {
                job.setStatus(JobStatus.SUCCESSFULL);
            } else {
                job.setStatus(JobStatus.FAILED);
            }
        } else {
            LOGGER.error("Job " + job.getId() + " has no document to process");
            job.setStatus(JobStatus.FAILED);
        }


        job.setEnd(new Date());
    }

    /**
     * Check if customer key is valid.
     *
     * @param job Job to be authorized
     * @return True if model is authorized, false otherwise
     */
    private Boolean checkCustomerKey(final Job job) {
        if (job.getCustomerId() != null
            && job.getCustomerId().equals(Conf.getDefaultCID())) {
            return true;
        }
        return false;
    }

    /**
     * Run processors given by a model on a document.
     *
     * @param job      Job executing
     * @param document Document to be processed
     * @return True if model succeeded, false otherwise
     */
    public final Boolean process(final Job job, final Document document) {


        UlfTimer timer = new UlfTimer();
        Processor processor;
        ArrayList<JobProcessor> procs = job.getProcessors();

        if (procs == null) {
            LOGGER.warn("No processors given with model " + job.getId()
                    + ", using default processors stack");

            IPreset preset = new BasicTextStatPreset();
            procs = preset.getProcessors();
        }

        if (getVerbose()) {
            LOGGER.trace("  - DOCUMENT ANALYSIS STARTED: "
                    + document.getExcerpt());
        }

        for (JobProcessor jobProcessor : procs) {

            try {
                processor = ProcessorFactory.get(jobProcessor.getName());
            } catch (Exception e) {
                LOGGER.error("Processor not found: "
                    + jobProcessor.getName(), e);
                continue;
            }

            if (!ProcessorFactory.requirements().areMet(
                    jobProcessor.getName(),
                    document.getHistory())) {
                LOGGER.error("Processor ["
                    + jobProcessor.getName()
                    + "] called but requirements not met");

                document.setStatus(DocumentStatus.FAILED);
                break;
            }

            // Start populating document with job data
            document.ensureProperty("meta", "customer_id",
                    job.getCustomerId());

            document.ensureProperty("meta", "job_id",
                    job.getId());

            document.ensureProperty("meta", "job_type",
                    job.getType().toString());

            document.ensureProperty("meta", "job_target",
                    job.getTarget().toString());

            if (job.getTarget() == JobTarget.TRAIN) {
                document.ensureProperty("meta", "job_training_classes",
                        job.getClasses());
            }

            if (job.getType() == JobDocumentType.URL) {
                document.ensureProperty("meta", "url", job.getValue());
            }

            // Populate with meta data from job
            HashMap<String, Object> meta = job.getMeta();
            if (meta != null) {
                for (String key : meta.keySet()) {
                    try {
                        document.ensureProperty("meta", key, meta.get(key));
                    } catch (IllegalArgumentException e) {
                        LOGGER.warn("A metadata information is invalid: "
                        + "job " + job.getId() + ", key " + key, e);
                    }
                }
            }

            // Run
            if (runProcessor(document, processor)) {

                // And append to history
                document.appendHistory(jobProcessor.getName());
            }

        }

        long t = timer.end();

        if (getVerbose()) {
            LOGGER.trace("  - DOCUMENT ANALYSIS ENDED: Done in " + t + "ms");
        }

        document.setOriginal(document.getRaw());

        if (document.getExists()) {
            DocumentStorage.update(document);
        }

        return true;
    }

    /**
     * Run one processor on the document.
     *
     * @param document  Document to be processed
     * @param processor Processor to run on document
     * @return True if document was processed, false otherwise
     */
    private boolean runProcessor(final Document document,
                                 final Processor processor) {


        // Finally check if processor is language specific
        if (processor instanceof ILocalizedProcessor) {
            // First check if language is set for the document
            if (document.getLanguage() == Language.UNKNOWN) {
                LOGGER.warn("  |  Language specific processor "
                    + "but document language unknown");
                return false;
            }

            // Second check if that language is available for processor
            String[] languages =
                ((ILocalizedProcessor) processor).getLanguages();

            Boolean found = false;
            for (int i = 0, l = languages.length; i < l; i++) {
                if (languages[i].equals(document.getLanguage().toString())) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                LOGGER.warn("Processor" + processor.getClass().getSimpleName()
                    + "language specific but no language found");
                return false;
            }
        }


        if (getVerbose()) {
            LOGGER.trace("  |  Processing "
                + processor.getClass().getSimpleName() + ": "
                + processor.describe());
        }

        // Second, extract
        processor.extract(document);

        return true;
    }
}
