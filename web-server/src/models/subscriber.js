/*jslint node:true*/

(function () {
    "use strict";

    var Subscriber;

    exports.get = function (mongoose) {

        if(!!Subscriber) {
            return Subscriber;
        }

        var Schema = mongoose.Schema;
        
        Subscriber = mongoose.model('Subscriber', new Schema({
            email: String,
            ref_campaign: String,
            ref_domain: String,
            os: String,
            device: String,
            browser: String,
            ts_created: {type: Date, default: Date.now},
            unactivated: {type: Boolean, default: false}
        }));

        return Subscriber;
    };

}());