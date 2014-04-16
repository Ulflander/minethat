package com.ulflander.application.model;

import com.ulflander.application.services.email.IMAPEmail;
import com.ulflander.mining.Patterns;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * JobFactory class creates one or more Job instance from
 * multiple kind of objects: String, Email, File...
 *
 * @see com.ulflander.mining.ProcessingExecutor
 * @since 2/24/14
 * @author Ulflander <xlaumonier@gmail.com>
 */
public final class JobFactory {

    /**
     * Private constructor.
     */
    private JobFactory() {

    }

    /**
     * Creates a Job for a document provided as a string.
     *
     * @param s Text to process
     * @param procs List of processors ro tun
     * @return A Job that can be executed by the ProcessingExecutor
     */
    public static Job fromString(final String s, final List<String> procs) {
        Job j = getJob(procs);
        j.setType(JobDocumentType.STRING);
        j.setValue(s);
        return j;
    }

    /**
     * Creates a Job for a document provided as an IMAP email.
     *
     * @param e Email to process
     * @return A Job that can be executed by the ProcessingExecutor
     */
    public static Set<Job> fromEmail(final IMAPEmail e) {
        return fromEmail(e, null);
    }

    /**
     * Creates a Job for a document provided as an IMAP email and
     * with given processors. This method will try to guess if we deal
     * with attachements,
     *
     * @param e Email to process
     * @param procs List of processors ro tun
     * @return A Job that can be executed by the ProcessingExecutor
     */
    public static Set<Job> fromEmail(final IMAPEmail e,
                                final List<String> procs) {

        // First, check attachement
        if (e.getAttachmentsSize() > 0) {
            return fromEmailAttachments(e, procs);
        }

        // Second, check for URLs
        String[] content = e.getContent().trim().split("\n");

        // Mail content is empty, cant do nothing
        if (content.length == 0) {
            return null;
        }

        if (Patterns.IS_URL.matcher(content[0].trim()).matches()) {
            return fromEmailURLs(content, e, procs);
        }

        HashSet<Job> jobs = new HashSet<Job>();
        jobs.add(fromString(e.getContent(), procs));

        // Last option, we analyse the body of the email
        return jobs;
    }


    /**
     * Creates a Job for a document provided as an IMAP email containing
     * one potential URL per line and with given processors.
     *
     * @param content Email content lines
     * @param e Email to process
     * @param procs List of processors ro tun
     * @return A Job that can be executed by the ProcessingExecutor
     */
    public static Set<Job> fromEmailURLs(final String[] content,
                                    final IMAPEmail e,
                                    final List<String> procs) {

        HashSet<Job> jobs = new HashSet<Job>();

        for (int i = 0, l = content.length; i < l; i++) {
            Job j = getJob(procs);
            content[i] = content[i].trim();
            // If not an URL, skip it
            if (!Patterns.IS_URL.matcher(content[0].trim()).matches()) {
                continue;
            }

            // Otherwise create URL model document
            j.setType(JobDocumentType.URL);
            j.setValue(content[i]);
            jobs.add(j);
        }

        return jobs;
    }


    /**
     * Creates a Job for a document provided as an IMAP email containing
     * attachments and with given processors.
     *
     * @param e Email to process
     * @param procs List of processors ro tun
     * @return A Job that can be executed by the ProcessingExecutor
     */
    public static Set<Job> fromEmailAttachments(final IMAPEmail e,
                                           final List<String> procs) {

        HashSet<Job> jobs = new HashSet<Job>();

        for (int i = 0, l = e.getAttachmentsSize(); i < l; i++) {
            Job j = getJob(procs);
            j.setType(JobDocumentType.FILE);
            j.setValue(e.getAttachment(i).getPath());
            jobs.add(j);
        }

        return jobs;
    }


    /**
     * Creates a Job for a document provided as a Document (testing oriented).
     *
     * @param d Document to process
     * @param procs List of processors ro tun
     * @return A Job that can be executed by the ProcessingExecutor
     */
    public static Job fromDocument(final Document d,
                                   final List<String> procs) {
        Job j = getJob(procs);
        j.setDocument(d.getId());
        return j;
    }


    /**
     * Initialize a new model object with given processors.
     *
     * @param procs Processors names list
     * @return A brand new model, initialized with given processors
     */
    private static Job getJob(final List<String> procs) {
        Job j = getJob();
        setupProcessors(j, procs);
        return j;
    }

    /**
     * Initialize a new model object.
     *
     * @return A brand new model!
     */
    private static Job getJob() {
        Job j = new Job();
        return j;
    }

    /**
     * Add given processors names to model as JobProcessor.
     *
     * @param j Job
     * @param procs Processors names list
     */
    private static void setupProcessors(final Job j,
                                        final List<String> procs) {
        if (procs == null) {
            return;
        }

        ArrayList<JobProcessor> processors = new ArrayList<JobProcessor>();
        for (String p : procs) {
            JobProcessor jp = new JobProcessor(p);
            processors.add(jp);
        }
        j.setProcessors(processors);
    }
}
