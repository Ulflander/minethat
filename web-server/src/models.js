/*jslint node:true*/


/**
 * Expose all the models.
 */
(function() {
    'use strict';

    var mongoose = require('mongoose');
    mongoose.connect('mongodb://localhost/minethat_local');

    exports.Job =
        require('./models/job.js').get(mongoose);

    exports.Document =
        require('./models/document.js').get(mongoose);

    exports.Subscriber =
        require('./models/subscriber.js').get(mongoose);
}());
