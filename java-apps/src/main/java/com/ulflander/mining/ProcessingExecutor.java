package com.ulflander.mining;

import com.ulflander.app.Conf;
import com.ulflander.app.model.Document;
import com.ulflander.app.model.DocumentStatus;
import com.ulflander.app.model.Job;
import com.ulflander.app.model.JobDocumentType;
import com.ulflander.app.model.JobProcessor;
import com.ulflander.app.model.JobStatus;
import com.ulflander.app.model.JobTarget;
import com.ulflander.app.model.Language;
import com.ulflander.app.model.storage.DocumentStorage;
import com.ulflander.mining.processors.ILocalizedProcessor;
import com.ulflander.mining.processors.Processor;
import com.ulflander.mining.processors.ProcessorFactory;
import com.ulflander.mining.processors.preset.BasicPreset;
import com.ulflander.mining.processors.preset.IPreset;
import com.ulflander.utils.UlfTimer;
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
        int i = 0;

        if (procs == null) {
            LOGGER.warn("No processors given with model " + job.getId()
                    + ", using default processors stack");

            IPreset preset = new BasicPreset();
            procs = preset.getProcessors();
        }

        if (getVerbose()) {
            LOGGER.trace("DOCUMENT ANALYSIS STARTED: "
                    + document.getExcerpt());
        }



        ensureMeta(job, document);

        for (JobProcessor jobProcessor: procs) {

            i++;

            if (jobProcessor.hasClazz()) {
                try {
                    processor = ProcessorFactory.get(jobProcessor.getClazz());
                } catch (Exception e) {
                    LOGGER.error("Processor not valid: "
                            + jobProcessor.getName(), e);
                    continue;
                }
            } else {
                try {
                    processor = ProcessorFactory.get(jobProcessor.getName());
                } catch (Exception e) {
                    LOGGER.error("Processor not found: "
                        + jobProcessor.getName(), e);
                    continue;
                }
            }

            if (!ProcessorFactory.requirements().areMet(
                    jobProcessor.getName(),
                    document.getHistory())) {
                LOGGER.error("Processor [" + jobProcessor.getName()
                    + "] called but requirements not met");

                job.setStatus(JobStatus.FAILED);
                document.setStatus(DocumentStatus.FAILED);
                break;
            }

            // Run
            if (runProcessor(document, processor)) {

                // And append to history
                document.appendHistory(jobProcessor.getName());
            }

            // Validate if we have some paragraphs
            if (processor.getClass()
                    .getSimpleName().equals("DocumentSplitter")
                    && document.getChapterAt(0).getParagraphsSize() == 0) {

                LOGGER.warn("Document [" + document.getId()
                        + "] split returned no paragraph, failing.");

                document.setStatus(DocumentStatus.FAILED);
                break;

            }

        }

        long t = timer.end();

        document.addProperty("meta", "job_processors_applied", i);
        document.addProperty("meta", "job_duration_ms", t);

        if (getVerbose()) {
            LOGGER.trace("DOCUMENT ANALYSIS ENDED: Done in " + t + "ms");
        }

        document.setOriginal(document.getSurface());

        if (document.getStatus() != DocumentStatus.FAILED) {
            document.setStatus(DocumentStatus.MINED);
        }

        if (document.getExists() && DocumentStorage.update(document) == null) {
            return false;
        }

        return true;
    }


    /**
     * Set some document meta data based on job data.
     *
     * @param job Job
     * @param document Document to be processed
     */
    private void ensureMeta(final Job job, final Document document) {

        // Start populating document with job data
        document.ensureProperty("meta", "customer_id",
                job.getCustomerId());

        document.ensureProperty("meta", "job_id",
                job.getId());

        document.ensureProperty("meta", "job_type",
                job.getType().toString());

        document.ensureProperty("meta", "job_target",
                job.getTarget().toString());

        document.ensureProperty("meta", "job_start_date",
                System.currentTimeMillis());

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
                LOGGER.warn("Language specific processor "
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
                LOGGER.warn(processor.getClass().getSimpleName()
                    + " is language specific but no language found");
                return false;
            }
        }


        if (getVerbose()) {
            LOGGER.trace(processor.getClass().getSimpleName() + ": "
                + processor.describe());
        }

        // Second, extract
        processor.extract(document);

        return true;
    }
}
