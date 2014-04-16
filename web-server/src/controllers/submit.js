/*jslint node:true*/

(function () {
    "use strict";

    var responder = require('../responder.js').responder,
        utils = require('../utils.js').utils,
        Job = require('../models.js').Job,
        amqp = require('amqp'),
        connection,
        ready = false,
        submit = {

            publish: function (jobId) {
                if(!ready) {
                    setTimeout(function () {
                        submit.publish(jobId);
                    }, 500);
                    return;
                }

                console.log('[RabbitMQ] Submitted job ' + jobId);
                connection.publish('extract', jobId);
            },

            html_string: function (req, res) {
                var content;

                console.log(req.body);

                if (!req.body.content) {
                    return responder.json_error(req, res, 500, 'miss_param');
                }
                
                submit.exec(req, res, 'HTML_STRING', req.body.content);
            },


            string: function (req, res) {
                if (!req.body || !req.body.string) {
                    return responder.json_error(req, res, 500, 'miss_param');
                }
                
                submit.exec(req, res, 'STRING', req.body.string);
            },


            url: function (req, res) {
                if (!req.body || !req.body.url) {
                    return responder.json_error(req, res, 500, 'miss_param');
                }

                submit.exec(req, res, 'URL', req.body.url);
            },

            exec: function (req, res, type, value) {

                console.log('[API] Got request');

                // Create job
                var job = new Job({
                        customerId: '2c7f9a',
                        gateway: 'API',
                        status: 'VOID',
                        type: type,
                        value: value
                    });

                if (!!req.body.meta) {
                    job.meta = req.body.meta;
                }

                job.save(function (err) {
                    if(!err) {
                        submit.publish(job._id);
                        return responder.json(req, res, {
                            status: 'success',
                            job: job._id
                        });
                    }


                    return responder.json_error(req, res, 500, 'db_error');
                });
                
            }

        };

    connection = amqp.createConnection({ host: "localhost", port: 5672 });

    connection.on('ready', function () {
        console.log('[RabbitMQ] Connection established');

        connection.queue('extract', {
            durable: true,
            'exclusive': false,
            'autoDelete': false
        }, function(q){
            console.log('[RabbitMQ] Queue declared');
            ready = true;
        });
    });

    connection.on('error', function () {
        ready = false;
        console.log('[RabbitMQ] Error', arguments);
    });

    exports.submit = submit;

}());
