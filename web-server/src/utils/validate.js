/*jslint node:true*/

(function () {
    "use strict";


    exports.validate = {
        email: function (val) {
            var at_idx,
                dot_idx;

            if (!val || typeof val !== 'string') {
                return false;
            }

            at_idx = val.indexOf('@');
            dot_idx = val.lastIndexOf('.');

            return at_idx > 0 && dot_idx > 3 
                && at_idx < dot_idx && val.length > dot_idx + 2;
        }
    };


}());