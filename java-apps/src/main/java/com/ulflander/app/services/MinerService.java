package com.ulflander.app.services;

import com.rabbitmq.client.QueueingConsumer;
import com.ulflander.app.Conf;
import com.ulflander.mining.ProcessingExecutor;
import com.ulflander.app.model.Job;
import com.ulflander.app.model.storage.JobStorage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Service that takes model identifier from queue and executes a model.
 *
 * @author Ulflander <xlaumonier@gmail.com>
 * @since 2/27/14
 */
public class MinerService extends RabbitService {

    /**
     * Logger.
     */
    private static final Logger LOGGER =
        LogManager.getLogger(MinerService.class);

    /**
     * Creates a new MinerService with given arguments.
     *
     * @see com.ulflander.app.services.Service
     * @param args Service arguments
     */
    public MinerService(final String[] args) {
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
        Service s = new MinerService(args);
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
            getRabbitChannel().basicConsume(Conf.EXECUTOR_QUEUE_NAME,
                    true, consumer);
        } catch (IOException e) {
            throw new ServiceException("Service not runnable: "
                    + "RabbitMQ queue consuming failed.", this, e);
        }

        listen(consumer);
    }

    /**
     * Listen to executor queue and process given jobs documents.
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

            LOGGER.debug("[MinerService] Get model ["
                    + jobId + "]");

            process(jobId);
        }
    }

    /**
     * Process a model document.
     *
     * @param jobId Id of model
     */
    private void process(final String jobId) {

        Job job = JobStorage.get(jobId);

        if (job == null) {
            LOGGER.error("Unable to retrieve model " + jobId);
            return;
        }

        ProcessingExecutor executor = new ProcessingExecutor();
        executor.setVerbose(getVerbose());
        executor.execute(job);


        publish(Conf.FILTER_QUEUE_NAME, job.getDocument(),
                MSG_PUBLICATION_TRIES);
    }
}
