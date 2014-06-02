
(function(blacklist) {
    'use strict';


    var bl = [
        '^.*google\..*$',
        '^.*minethat\.co$',
        '^.*facebook\.com$',
        '^.*localhost$',
        '^.*localhost:.*$'
    ];

    /**
     * Check if a domain name is blacklisted.
     *
     * @param  {String} domain Domain to check
     * @return {Boolean}       True if domain is blacklisted, false otherwise
     */
    blacklist.blacklisted = function (domain) {
        var i, l = bl.length;

        if(!domain) {
            return false;
        }

        for (i = 0; i < l; i += 1) {
            if (domain.match(new RegExp(bl[i], 'ig'))) {
                return true
            }
        }

        return false;
    };


}(hunk('blacklist')));
