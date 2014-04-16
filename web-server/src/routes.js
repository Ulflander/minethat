/*jslint node:true*/


/**
 * List all application routes.
 */
(function() {
    'use strict';

    /**
     * Routes (route > [method, controller.function])
     */
    exports.routes = {

        ///////////////////////
        // PUBLIC
        '/': ['get', 'home'],
        '/subscription': ['get', 'home.subscription'],
        '/ajax/landing_subscribe': ['post', 'home.subscribe'],


        ///////////////////////
        // app
        '/app/jobs': ['get', 'jobs.all'],
        '/app/job/:id': ['get', 'jobs.one'],

        '/app/documents': ['get', 'documents.all'],


        ///////////////////////
        // admin
        '/admin/subscribers': ['get', 'subscribers.all'],
        '/admin/subscribers.csv': ['get', 'subscribers.all_csv'],
        '/admin/subscriber/:id': ['get', 'subscribers.one'],


        ///////////////////////
        // REST API
        '/api/v1/jobs': ['get', 'jobs.all'],
        '/api/v1/job/:jobId': ['get', 'jobs.one'],


        '/api/v1/documents': ['get', 'documents.all'],


        ///////////////////////
        // REST API
        '/api/v1/submit/url': ['post', 'submit.url'],
        '/api/v1/submit/string': ['post', 'submit.string'],
        '/api/v1/submit/html_string': ['post', 'submit.html_string']
    };

}());
