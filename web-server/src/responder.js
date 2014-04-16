/*jslint node:true*/

(function() {
    'use strict';

    var f = require('fighterr'),
        fs = require('fs'),
        htmlToText = require('html-to-text'),
        nodemailer = require('nodemailer'),
        logger = require('./conf.js').logger,
        smtp,
        ejs = require('ejs'),
        tpls = {};

    /**
     * Utility to quickly respond to HTTP requests from controllers.
     */
    exports.responder = {

        get_template: function(tpl) {
            var t,
                p;

            //if(!tpls[tpl]) {
                p = __dirname + '/views/' + tpl + '.html';
                try {
                    t = fs.readFileSync(p);
                    t = t.toString();
                } catch (e) {
                    this.error(req, res, 500, 'Internal error',
                        'Dont find template "' + p + '"');
                    return;
                }
                tpls[tpl] = t;
            //}

            return tpls[tpl];
        },


        email: function(to, subject, tpl, data, from, text) {
            var html = ejs.render(this.get_template('emails/' + tpl), {
                data: data
            });

            if (!smtp) {
                smtp = nodemailer.createTransport('SMTP', {
                    service: 'Gmail',
                    auth: {
                        user: 'xlaumonier@gmail.com',
                        pass: '$Carlsberg01082010$'
                    }
                });
            }

            smtp.sendMail({
                from: from || 'Xav from Minethat <xav@minethat.co>',
                to: to,
                subject: subject,
                text: text || htmlToText.fromString(html),
                html: html
            }, function(error, response) {
                if (error) {
                    logger.error(error);
                }else {
                    logger.debug('Message sent: ' + response.message);
                }
            });
        },

        /**
         * Respond with given http status, assume an error.
         *
         * @param {string} req - Flatiron request object.
         * @param {string} res - Flatiron response object.
         * @param {number} status - HTTP status code.
         * @param {string} msg - Message to return.
         * @param {string} debug - Message to send to warning log.
         */
        error: function(req, res, status, msg, debug) {
            res.writeHead(f.range(status, 400, 550, 500), {
                'Content-type': 'text/plain'
            });

            req.mt.log.warn('Error ' + status + ' (' + req.url + '): ' + msg +
                (!!debug ? ' / ' + debug : ''));

            res.end(msg + ': ' + debug);
        },

        json_error: function(req, res, status, msg) {
            res.writeHead(f.range(status, 400, 550, 500), {
                'Content-type': 'application/json'
            });

            req.mt.log.warn('Error ' + status + ' (' + req.url + '): ' + msg);

            res.end(JSON.stringify({
                status: 'error',
                service: req.url,
                error: msg
            }));
        },

        /**
         * Respond with plain text page.
         *
         * @param {string} req - Flatiron request object.
         * @param {string} res - Flatiron response object.
         * @param {string} str - Plain text content to return.
         */
        plain: function(req, res, str) {
            res.writeHead(200, {
                'Content-type': 'text/plain'
            });
            res.end(str);
        },

        /**
         * Respond with an HTML template.
         *
         * @param {string} req - Flatiron request object.
         * @param {string} res - Flatiron response object.
         * @param {string} tpl - Name of template file.
         * @param {object} data - Data object
         * @param {string} content_type - Content type, default to HTML
         */
        html: function(req, res, tpl, data, content_type) {
            var str;
            content_type = content_type || 'text/html';

            try {
                str = ejs.render(this.get_template(tpl), {
                    data: data
                });
            } catch (e) {
                logger.error('Unable to render template ' + tpl, e);
            }

            res.writeHead(200, {
                'Content-type': content_type
            });
            res.end(str);
        },

        /**
         * Respond with a CSV.
         *
         * @param {string} req - Flatiron request object.
         * @param {string} res - Flatiron response object.
         * @param {string} tpl - Name of template file.
         * @param {object} data - Data object
         */
        csv: function(req, res, tpl, data) {
            this.html(req, res, tpl, data, 'text/csv');
        },

        /**
         * Respond with a JSON.
         *
         * @param {string} req - Flatiron request object.
         * @param {string} res - Flatiron response object.
         * @param {string} o - Object to convert to JSON.
         */
        json: function(req, res, o) {
            res.writeHead(200, {
                'Content-type': 'application/json'
            });
            if (typeof o === 'string') {
                res.end(o);
            } else {
                res.end(JSON.stringify(o));
            }
        }

    };
}());
