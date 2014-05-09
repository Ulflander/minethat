package com.ulflander.app.services;

import com.ulflander.app.Conf;
import com.ulflander.app.ConfReader;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Abstract Minethat service.
 *
 * @author Ulflander <xlaumonier@gmail.com>
 * @since 2/21/14
 */
public abstract class Service {

    /**
     * Logger.
     */
    private static final Logger LOGGER =
        LogManager.getLogger(Service.class);

    /**
     * Verbose mode.
     */
    private Boolean verbose = true;

    /**
     * Loop mode.
     */
    private Boolean loop = true;

    /**
     * Get loop mode: does service loop undefinitely
     * or just run one time and close.
     *
     * @return Loop mode
     */
    public final Boolean getLoop() {
        return loop;
    }

    /**
     * Set loop mode.
     *
     * @param l Loop mode
     */
    public final void setLoop(final Boolean l) {
        this.loop = l;
    }



    /**
     * Create a new service (read test configuration).
     *
     * @see com.ulflander.app.Conf
     */
    public Service() {
        Conf.read();
    }

    /**
     * Initialize the service (read given configuration, demonize process).
     *
     * Service expect the following arguments:
     *
     * 1) Path to project root
     * 2) Current environment
     *
     * Project root should contain following folders: "datasets", "conf".
     *
     *
     * @param args Arguments
     * @see com.ulflander.app.Conf
     */
    public Service(final String[] args) {
        LOGGER.warn("Service starting with arguments: "
                + StringUtils.join(args, ", "));

        if (args.length < 2) {
            new IllegalArgumentException("Service expects arguments "
                                        + "('path/to/root/', 'env')");
        }

        ConfReader.setRoot(args[0]);
        Conf.read(args[1]);
    }

    /**
     * Run method that should be implemented in concrete services.
     *
     * @throws ServiceException A service exception is service is
     * unable to start
     */
    public abstract void run() throws ServiceException;

    /**
     * Get verbose mode.
     *
     * @return True if verbose mode active, false otherwise
     */
    public final Boolean getVerbose() {
        return verbose;
    }

    /**
     * Set verbose mode.
     *
     * @param v Verbose mode active or not
     */
    public final void setVerbose(final Boolean v) {
        this.verbose = v;
    }
}
