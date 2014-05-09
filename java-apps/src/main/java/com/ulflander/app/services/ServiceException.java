package com.ulflander.app.services;

/**
 * Service exception are really bad: it means the service didn't start at all
 * for some reason.
 *
 * Created by Ulflander on 3/10/14.
 */
public class ServiceException extends Exception {

    /**
     * Create a new service exception.
     *
     * @param message Error message
     * @param s Service in cause
     */
    public ServiceException(final String message,
                            final Service s) {

        super("[" + s.getClass().getSimpleName() + "] " + message);
    }

    /**
     * Create a new service exception from another exception.
     *
     * @param message Error message
     * @param s Service in cause
     * @param e Exception
     */
    public ServiceException(final String message,
                            final Service s,
                            final Exception e) {

        super("[" + s.getClass().getSimpleName() + "] " + message, e);
    }
}
