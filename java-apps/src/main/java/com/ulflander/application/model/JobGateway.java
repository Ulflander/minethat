package com.ulflander.application.model;

/**
 * List of available model gateways.
 *
 * @since 2/24/14
 * @author Ulflander <xlaumonier@gmail.com>
 */
public enum JobGateway {

    /**
     * Email gateway.
     */
    EMAIL,

    /**
     * HTTP API gateway.
     */
    API,

    /**
     * Web submission.
     */
    WEB,

    /**
     * Test (is default at initialization of a Job).
     */
    TEST

}
