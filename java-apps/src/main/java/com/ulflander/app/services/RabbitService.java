package com.ulflander.app.services;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import com.ulflander.app.Conf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;

/**
 * Provides connection to the rabbit for concrete services.
 *
 * Created by Ulflander on 3/12/14.
 */
public abstract class RabbitService extends Service {

    /**
     * In case of errors when sending message, number of times we retry to
     * send it.
     */
    public static final int MSG_PUBLICATION_TRIES = 3;

    /**
     * Logger.
     */
    private static final Logger LOGGER =
        LogManager.getLogger(RabbitService.class);

    /**
     * Creates a new RabbitService.
     *
     * @see com.ulflander.app.services.Service
     */
    public RabbitService() {
        super();
    }

    /**
     * Creates a new RabbitService with given arguments.
     *
     * @see com.ulflander.app.services.Service
     * @param args Service arguments
     */
    public RabbitService(final String[] args) {
        super(args);
    }

    /**
     * RabbitMQ connection.
     */
    private Connection rabbitConnection;

    /**
     * RabbitMQ channel.
     */
    private Channel rabbitChannel;

    /**
     * Initializes RabbitMQ connection, channel and exchange.
     *
     * @return True if rabbit is connected, false otherwise
     */
    protected final Boolean initRabbit() {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(Conf.getRabbitHost());

        try {
            rabbitConnection = factory.newConnection();
            setRabbitChannel(rabbitConnection.createChannel());
        } catch (IOException e) {
            LOGGER.error("Unable to connect to RabbitMQ", e);
            return false;
        }

        AMQP.Queue.DeclareOk res, res2;

        try {
            res = getRabbitChannel().queueDeclare(Conf.EXTRACTOR_QUEUE_NAME,
                    true, false, false, new HashMap<String, Object>());

            res2 = getRabbitChannel().queueDeclare(Conf.EXECUTOR_QUEUE_NAME,
                    true, false, false, new HashMap<String, Object>());

            LOGGER.debug("[" + this.getClass().getSimpleName() + "] "
                        + "connected on channel and declared queues '"
                        + Conf.EXTRACTOR_QUEUE_NAME + "' and '"
                        + Conf.EXECUTOR_QUEUE_NAME + "'");

        } catch (IOException e) {
            LOGGER.error("Unable to declare queue for RabbitMQ", e);
            return false;
        }

        if (res == null || res2 == null) {
            LOGGER.error("Unable to declare queue for RabbitMQ: "
                    + "DeclareOK response is null");
            return false;
        }

        return true;
    }

    /**
     * Get rabbit channel.
     *
     * @return Rabbit channel
     */
    public final Channel getRabbitChannel() {
        return rabbitChannel;
    }

    /**
     * Set rabbit channel.
     *
     * @param rc Rabbit channel
     */
    public final void setRabbitChannel(final Channel rc) {
        this.rabbitChannel = rc;
    }

    /**
     * Publish a message on a queue.
     *
     * @param queue Name of the queue
     * @param msg Message to publish
     * @param tries Number of retries in case of failure
     */
    protected final void publish(final String queue,
                         final String msg,
                         final Integer tries) {

        int t = tries;
        while (t > 0) {
            t--;

            try {
                rabbitChannel.basicPublish("", queue,
                        MessageProperties.PERSISTENT_TEXT_PLAIN,
                        msg.getBytes(Charset.forName("UTF-8")));

                LOGGER.debug("Message ["
                        + msg + "] sent on [" + queue + "]");

                return;
            } catch (IOException e) {
                LOGGER.error("Unable to publish message to queue ["
                        + queue
                        + "]. Will retry: " + t + " times.");
            }
        }
    }


}
