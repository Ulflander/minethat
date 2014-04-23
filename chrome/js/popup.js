/*jslint browser: true*/
/*globals chrome*/


(function () {
    "use strict";

    var source;

    chunk ('popup', {

        /**
         * Init popup
         * 
         */
        start: function () {

            chrome.extension.onMessage.addListener(function(request, sender) {
                if (request.action == "getSource") {
                    chunk('popup').status("Source retrieved");
                    source = request.source;
                }
            });

            chrome.tabs.executeScript(null, {
                file: "js/injected.js"
            }, function() {
                if (chrome.extension.lastError) {
                    chunk('popup').status(
                        'There was an error injecting script : \n' + 
                        chrome.extension.lastError.message);
                } else {
                    chunk('popup').status('Script injected');
                }
            });


            $('#minethat_page').on('click', chunk('popup.onMineClick'));
            $('#trainthat_page').on('click', chunk('popup.onTrainClick'));
        },

        /**
         * Get current tab and submit source with URL.
         * 
         * @return {[type]} [description]
         */
        submit: function (cb) {
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
        },

        onMineClick: function () {
            chunk('popup').submit(function (tab_url) {
                if (!!tab_url) {
                    chunk('api').submit(source, tab_url);
                } else {
                    chunk('popup').status('Didnt got URL');
                }
            });
        },

        onTrainClick: function () {
            chunk('popup').submit(function (tab_url) {
                if (!tab_url) {
                    chunk('popup').status('Didnt got URL');
                    return;
                }

                var classe = $('[name=train_type]:checked').val();
                chunk('api').train(source, tab_url, classe);
            });
        },
        
        status: function (msg) {
            $('#status').innerText = msg;
        }

    });

    
    $(function () {
        chunk.conf({
                api_server: localStorage.minethat_server
            })
            .start();
    });
}());
