/*jslint node:true*/

(function() {
    'use strict';

    var auth = require('http-auth'),
        logger = require('./conf.js').logger,
        // For private developers doc
        basic = auth.basic({
            authRealm: 'Private Minethat documentations',
            file: __dirname + '/../users.htpasswd'
        });


    exports.auth = [
        /**
         * Auth strategy for developers/private.
         *
         * @param {string} req - Flatiron request object.
         * @param {string} res - Flatiron response object.
         */
        function(req, res) {
            if (req.request.url.indexOf('/private') === 0 ||
                req.request.url.indexOf('/admin') === 0 ||
                req.request.url.indexOf('/app') === 0) {
                basic.check(req, res, function(username) {
                    res.emit('next');
                });
                return;
            }

            res.emit('next');

        },
        /**
         * Auth strategy for admin.
         *
         * @param {string} req - Flatiron request object.
         * @param {string} res - Flatiron response object.
         */
        function(req, res) {
            if (req.request.url.indexOf('/admin') !== 0) {
                res.emit('next');
                return;
            }
            res.emit('next');
        },
        /**
         * Auth strategy for REST API.
         *
         * @param {string} req - Flatiron request object.
         * @param {string} res - Flatiron response object.
         */
        function(req, res) {
            if (req.request.url.indexOf('/api/v1') !== 0) {
                res.emit('next');
                return;
            }
            res.emit('next');
        }
    ];

}());
