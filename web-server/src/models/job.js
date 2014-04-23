/*jslint node:true*/

(function () {
    "use strict";

    var Job;

    exports.get = function (mongoose) {

        if(!!Job) {
            return Job;
        }

        var Schema = mongoose.Schema,
            Document = require('./document.js').get(mongoose);
        
        Job = mongoose.model('Job', new Schema({
            start: {type: Date, default: Date.now},
            end: Date,
            status: String,
            gateway: String,
            value: String,
            type: String,
            document: String,
            customerId: {
                type: String,
                index: true
            },
            meta: {
                url: String,
                title: String,
                author: String,
                organization: String
            },
            email: String,
            target: {
                type: String,
                default: 'MINE'
            },
            classes: String
        }));

        return Job;
    };

}());