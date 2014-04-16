/*jslint node:true*/


/**
 * Expose all the models.
 */
(function() {
    'use strict';


    var responder = require('./responder.js').responder,

        ITEMS_PER_PAGE = 20,

        utils = {


        mongoSelection: function(req, res, model, template, 
                                conditions, fields, options) {

            var page = parseInt(req.query.page, 10) || 1;
            model.count({}, function(err, count) {
                if (!!err) {
                    responder.plain(req, res, 'Error occured: ' + err);
                    return;
                }
                model.find(conditions || {}, fields || {},
                    utils.mongoPage(req, ITEMS_PER_PAGE, options),
                    utils.mongoCB(req, res, function(err, docs) {
                        if (!!err) {
                            responder.plain(req, res, 'Error occured: ' + err);
                        } else {
                            responder.html(req, res, template, {
                                docs: docs,
                                limit: ITEMS_PER_PAGE,
                                page: page,
                                hasNext: ITEMS_PER_PAGE * page < count,
                                hasPrevious: page > 1,
                                count: count
                            });
                        }
                    }));
            });
        },

        mongoCB: function(req, res, callback) {
            return function(err, docs) {

                if (req.isAPI) {
                    if (!!err) {
                        responder.json(req, res, JSON.stringify({
                            error: 'Query error'
                        }));
                    } else {
                        responder.json(req, res, docs);
                    }
                }

                if (!!callback) {
                    callback(err, docs);
                }
            };
        },

        /**
         * Returns good pagination object given request
         *
         * @param  {Request} req Request that could contain pagination info
         * @param  {Number} limit Number of items
         * @param  {Number} page Page
         * @return {Object} Mongo pagination object
         */
        mongoPage: function(req, limit, options) {
            var skip = 0,
                res,
                k;

            limit = limit || 10;

            res = {
                skip: limit * (parseInt(req.query.page) - 1 || 0),
                limit: limit
            };

            if (!!options) {
                for (k in options) {
                    if (options.hasOwnProperty(k)) {
                        res[k] = options[k];
                    }
                }
            }

            return res;
        }
    };

    exports.utils = utils;

}());
