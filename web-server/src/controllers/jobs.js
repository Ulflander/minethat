/*jslint node:true*/

(function () {
    "use strict";

    var responder = require('../responder.js').responder,
        utils = require('../utils.js').utils,
        Job = require('../models.js').Job;

    exports.jobs = {

        all: function (req, res) {
            utils.mongoSelection(req, res, Job, 'jobs/all', 
                null, 'id status type gateway document customerId meta start end', 
                {sort: {start: 'desc'}});
        },

        one: function (req, res) {
            
        }

    };

}());
