/*jslint node:true*/

(function () {
    "use strict";

    var responder = require('../responder.js').responder,
        validate = require('../utils/validate.js').validate,
        UAParser = require('ua-parser-js'),
        Subscriber = require('../models.js').Subscriber;

    exports.home = {


        /**
         * Landing page.
         * 
         * @param {string} req - Flatiron request object.
         * @param {string} res - Flatiron response object.
         */
        index: function (req, res) {
            responder.html(req, res, 'home', req.mt.analytics);
        },


        /**
         * Subscription: manages subscription for users.
         * 
         * @param {string} req - Flatiron request object.
         * @param {string} res - Flatiron response object.
         */
        subscription: function (req, res) {
            if (!req.query.id) {
                return responder.error(req, res, 500, 
                    'Missing param for unsubscription');
            }

            Subscriber.findById(req.query.id, function (err, sub) {
                var msg = null;

                if(err) {
                    return responder.error(req, res, 500, 'DB query error', err);
                }

                if(!!req.query.action) {
                    if (req.query.action === 'unsubscribe') {
                        sub.unactivated = true;
                        msg = 'Done! You won\'t receive our newsletter anymore.';
                    } else {
                        sub.unactivated = false;
                        msg = 'Thanks! You\'re now subscribed to our newsletter!';
                    }
                    sub.save();
                }

                responder.html(req, res, 'subscribers/subscription', {
                    msg: msg,
                    sub: sub
                });
            });
        },

        /**
         * Subscribe to landing page.
         * 
         * @param {string} req - Flatiron request object.
         * @param {string} res - Flatiron response object.
         */
        subscribe: function (req, res) {
            if (!req.body || !req.body.email) {
                return responder.json_error(req, res, 500, 'miss_param');
            }

            if (!validate.email(req.body.email)) {
                return responder.json_error(req, res, 500, 'invalid_email');
            }

            // First check if user exists
            Subscriber.findOne({'email': req.body.email}, function (err, data) {

                if(err) {
                    return responder.json_error(req, res, 500, 'db_check_failed');
                }

                // Already subscribed
                if(!!data) {
                    return responder.json(req, res, {
                        status: 'exists',
                        id: data._id
                    });
                }

                // Get device/browser
                var ua = (new UAParser()
                        .setUA(req.headers['user-agent'])
                        .getResult()) || {},

                    // Create subscriber
                    sub = new Subscriber({
                        email: req.body.email,
                        ref_campaign: req.body.ref_campaign,
                        ref_domain: req.body.ref_domain,
                        browser: !!ua.browser && !!ua.browser.name ? 
                            ua.browser.name : 'Unknown',
                        os: !!ua.os && !!ua.os.name ? 
                            ua.os.name : 'Unknown',
                        device: !!ua.device && !!ua.device.vendor ? 
                            ua.device.vendor + ' / ' + 
                                (ua.device.model || 'Unknown') : 'Unknown'
                    });

                // Save subscriber
                sub.save(function (err) {
                    if(err) {
                        return responder.json_error(req, res, 500, 'db_save_failed');
                    }

                    // Send email
                    responder.email(req.body.email, 'You\'re in!', 'landing-subscribed', {
                        sub: sub
                    });

                    responder.json(req, res, {
                        status: 'success'
                    });
                });
            });



        }

    };

}());
