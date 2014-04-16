/*jslint node:true*/

(function() {
    'use strict';

    var auth = require('http-auth'),
        logger = require('./conf.js').logger,
        url = require('url'),
        ref_campaigns = {
            'r1': 'Reddit landing feedback',
            't1': 'Twitter link',
            'e1': 'Xav email footer'
        };


    exports.analytics = [
        /**
         * Check for referrers
         *
         * @param {string} req - Flatiron request object.
         * @param {string} res - Flatiron response object.
         */
        function(req, res) {
            var d = {
                    ref_campaign: 'None',
                    ref_domain: 'Unknown'
                };

            if (!!req.query.r) {
                d.ref_campaign = ref_campaigns[req.query.r] || req.query.r;
            }

            if (!!req.headers.referer) {
                d.ref_domain = url.parse(req.headers.referer).hostname;
                if (d.ref_domain.indexOf('www.') === 0) {
                    d.ref_domain = d.ref_domain.substr(4);
                }
            }

            req.mt = req.mt || {};

            req.mt.analytics = d;

            res.emit('next');
        }
    ];

}());
