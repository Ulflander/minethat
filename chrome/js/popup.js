/*jslint browser: true*/
/*globals chrome*/


(function (popup) {
    "use strict";

    var source;


    /**
     * Init popup
     *
     */
    popup.start = function () {

        console.log('Start');

        if (typeof chrome === 'undefined' || !chrome.extension) {
            return;
        }

        chrome.extension.onMessage.addListener(function(request, sender) {
            if (request.action == "getSource") {
                popup.status("Source retrieved");
                source = request.source;
                if (request.meta && request.meta.feeds) {

                    for (var k in request.meta.feeds) {
                        popup.check_feed(k, request.meta.feeds[k])
                    }
                }
            }
        });

        chrome.tabs.executeScript(null, {
            file: "js/injected.js"
        }, function() {
            if (chrome.extension.lastError) {
                popup.status(
                    'There was an error injecting script : \n' +
                    chrome.extension.lastError.message);
            } else {
                popup.status('Script injected');
            }
        });


        $('#minethat_page').on('click', popup.onMineClick);
        $('#trainthat_page').on('click', popup.onTrainClick);
    };

    popup.check_feed = function(title, url) {
        var el = $('<li></li>').text(title);
        $('#feed_list').append(el);

        hunk.api.check_source(url, function(result) {
            if (result.status === 'exists') {
                el.text('Exists: ' + title);
            } else if (result.status === 'success') {
                el.text('Can be added: ' + title);
            } else if (result.status === 'error') {
                el.text('Error: ' + title);
            }
        });
    };

    /**
     * Get current tab and submit source with URL.
     *
     * @return {[type]} [description]
     */
    popup.submit = function (cb) {
        chrome.tabs.query ({
            'active': true,
            'windowId': chrome.windows.WINDOW_ID_CURRENT
        },
        function (tabs) {
            if(!!tabs[0].url) {
                cb(tabs[0].url);
            } else {
                cb(false);
            }
        });
    };

    popup.onMineClick = function () {
        popup.submit(function (tab_url) {
            if (!!tab_url) {
                hunk.api.submit(source, tab_url);
            } else {
                popup.status('Didnt got URL');
            }
        });
    };

    popup.onTrainClick = function () {
        popup.submit(function (tab_url) {
            if (!tab_url) {
                popup.status('Didnt got URL');
                return;
            }

            var classe = $('[name=train_type]:checked').val();
            hunk.api.train(source, tab_url, classe);
        });
    },

    popup.status = function (msg) {
        $('#status').text(msg);
    }


    $(function () {
        hunk.conf({
            api_server: localStorage.minethat_server
        });
        hunk();
    });

}(hunk('popup')));
