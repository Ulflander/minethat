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

            document.getElementById('minethat_page')
                .addEventListener('click', chunk('popup.onMineClick'));
        },

        /**
         * Get current tab and submit source with URL.
         * 
         * @return {[type]} [description]
         */
        submit: function () {
            chrome.tabs.query ({
                'active': true, 
                'windowId': chrome.windows.WINDOW_ID_CURRENT
            },
            function (tabs) {
                if(!!tabs[0].url) {
                    chunk('api').submit(source, tabs[0].url);
                }
            });
        },

        onMineClick: function () {
            chunk.get('popup').submit();
        },

        onTrainClick: function () {
        },
        
        status: function (msg) {
            document.getElementById('status').innerText = msg;
        }

    });

    
    document.addEventListener('DOMContentLoaded', function () {
        chunk
            .conf({
                api_server: localStorage.minethat_server
            })
            .start();
    });
}());
