/*jslint browser:true*/
/*globals modular*/


(function () {
    "use strict";

    chunk ('api', {

        call: function (api_url, content, callback) {
            var xhr = new XMLHttpRequest();

            xhr.open("POST", chunk.conf('api_server') +
                            "/api/v1/" + api_url, true);

            xhr.setRequestHeader("Content-Type", "application/json");

            xhr.onreadystatechange = function() {
                if (xhr.readyState == 4) {
                    var resp = JSON.parse(xhr.responseText);
                    if (typeof callback === 'function') {
                        if (!!resp && resp.status === "success") {
                            callback(null, resp.job_id);
                        } else {
                            callback(true);
                        }
                    }
                }
            };

            xhr.send(JSON.stringify(content));
        },

        submit_url: function (url, callback) {
            chunk('api').call('submit/url', {
                url: url,
                meta: {
                    url: url
                }
            }, callback);
        },

        submit: function (html_string, url, callback) {
            chunk('api').call('submit/html_string', {
                content: html_string,
                meta: {
                    url: url
                }
            }, callback);
        },

        train: function (html_string, url, classes, callback) {
            chunk('api').call('submit/html_string', {
                content: html_string,
                meta: {
                    url: url
                },
                target: 'TRAIN',
                classes: classes
            }, callback);
        },



        check_source: function (url, callback) {
            var xhr = new XMLHttpRequest();

            xhr.open("POST", chunk.conf('api_server') +
                            "/api/v1/sources/check", true);

            xhr.setRequestHeader("Content-Type", "application/json");

            xhr.onreadystatechange = function() {
                if (xhr.readyState == 4) {
                    var resp = JSON.parse(xhr.responseText);
                    if (typeof callback === 'function') {
                        callback(resp);
                    }
                }
            };

            xhr.send(JSON.stringify({
                feed_url: url
            }));
        }
    });
}());