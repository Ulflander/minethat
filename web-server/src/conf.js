/*jslint node:true*/


/**
 * Module: conf.js
 *
 * Initializes and returns configuration + logger.
 */
(function() {
    'use strict';

    var configuration = {},
        fs = require('fs');

    /**
     * Returns configuration.
     */
    exports.conf = configuration;

    /**
     * Return logger.
     */
    exports.logger = require('tracer').console({
        format: [
            //default format
            '{{timestamp}} <{{title}}> {{message}}',
            {
                // error format
                error: '{{timestamp}} <{{title}}> {{message}} ' +
                        '(in {{file}}:{{line}})\nCall Stack:\n{{stack}}'
            }
        ],
        transport: function(data) {
            if (data.level === 5) {
                console.error(data.output);
            } else {
                console.log(data.output);
            }
        }
    });


}());

