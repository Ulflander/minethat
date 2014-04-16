/*jslint node:true*/

(function () {
    "use strict";

    var responder = require('../responder.js').responder,
        utils = require('../utils.js').utils,
        Subscriber = require('../models.js').Subscriber;

    exports.subscribers = {

        all: function (req, res) {
            utils.mongoSelection(req, res, Subscriber, 'subscribers/all');
        },

        all_csv: function (req, res) {
            Subscriber.count({}, function( err, count){
                Subscriber.find({}, {}, 
                    utils.mongoPage(req, count + 1), 
                    utils.mongoCB(req, res, function (err, docs) {
                        if(!!err) {
                            responder.plain(req, res, 'Error occured: ' + err);
                        } else {
                            responder.csv(req, res, 'subscribers/all.csv', docs);
                        }
                    }));
            });
        },

        one: function (req, res) {
            
        }

    };

}());
