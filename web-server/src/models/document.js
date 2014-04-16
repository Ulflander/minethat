/*jslint node:true*/

(function () {
    "use strict";

    var Document;

    exports.get = function (mongoose) {

        if(!!Document) {
            return Document;
        }

        var Schema = mongoose.Schema;
        
        Document = mongoose.model('Document', new Schema({
            rawLength: Number,
            raw: String
        }));

        return Document;
    };

}());