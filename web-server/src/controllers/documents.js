/*jslint node:true*/

(function () {
    "use strict";

    var responder = require('../responder.js').responder,
        utils = require('../utils.js').utils,
        Document = require('../models.js').Document;

    exports.documents = {

        all: function (req, res) {
            utils.mongoSelection(req, res, Document, 
                'documents/all', null, null, {sort: 'created -1'});
        },

        one: function (req, res) {
            responder.html(req, res, 'documents/document');
        }

    };

}());
