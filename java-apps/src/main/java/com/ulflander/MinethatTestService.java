package com.ulflander;

import com.ulflander.application.Conf;
import com.ulflander.application.model.Job;
import com.ulflander.mining.ProcessingExecutor;
import com.ulflander.application.model.JobFactory;
import com.ulflander.application.model.Document;
import com.ulflander.application.services.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

/**
 * This service is intended only for unit test use.
 *
 * @since 2/24/14
 * @author Ulflander <xlaumonier@gmail.com>
 */
public class MinethatTestService extends Service {

    /**
     * Logger.
     */
    private static final Logger LOGGER =
        LogManager.getLogger(MinethatTestService.class);

    /**
     * Service that execute test jobs.
     */
    private ProcessingExecutor executor;

    /**
     * Processors to run for a test.
     */
    private ArrayList<String> processorNames;


    /**
     * Create a new test service.
     */
    public MinethatTestService() {
        super();
    }

    @Override
    public final void run() {
        if (getVerbose()) {
            LOGGER.debug("Minethat starting...");
        }
        executor = new ProcessingExecutor();
        executor.setVerbose(getVerbose());
        this.reset();
    }

    /**
     * Reset test executor.
     */
    public final void reset() {
        processorNames = new ArrayList<String>();
    }

    /**
     * Add a processor by name.
     *
     * @param s Name of processor to run as part of test
     */
    public final void addProcessor(final String s) {
        if (processorNames.contains(s)) {
            LOGGER.error("You can't add twice the same processor");
            return;
        }
        processorNames.add(s);
    }

    /**
     * Remove a processor by name.
     *
     * @param s Processor name
     */
    public final void removeProcessor(final String s) {
        processorNames.remove(s);
    }

    /**
     * Get number of processors.
     *
     * @return Number of processors currently setup
     */
    public final int getProcessorsSize() {
        if (processorNames != null) {
            return processorNames.size();
        }
        return 0;
    }

    /**
     * Submit a document as a simple string.
     *
     * @param s String to process
     */
    public final void submit(final String s) {
        submit(new Document(s));
    }

    /**
     * Submit a model.
     *
     * @param j Job to execute
     */
    public final void submit(final Job j) {
        executor.execute(j);
    }

    /**
     * Submit a document.
     *
     * @param d Document to process.
     */
    public final void submit(final Document d) {
        Job j = JobFactory.fromDocument(d, processorNames);
        j.setCustomerId(Conf.getDefaultCID());
        executor.process(j, d);
    }


    @Override
    public final String toString() {

        return "\nWelcome to Minethat\n"
            + "Minethat is important because it provides ";
    }
}
