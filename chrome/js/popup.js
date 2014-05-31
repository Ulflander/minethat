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
                console.log("Source retrieved");
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
            $('#loading').hide();

            if (chrome.extension.lastError) {
                console.log('Error: ' + chrome.extension.lastError.message);
                popup.status('Page not minable');
            } else {
                $('#content').show();
            }
        });


        $('#minethat_page').on('click', popup.onMineClick);
        $('#trainthat_page').on('click', popup.onTrainClick);
    };

    popup.add_feed = function(url, li) {
        console.log('add feed');
    };

    popup.check_feed = function(title, url) {

        var li = $('<li></li>'),
            link = $('<a></a>').attr('href', '#'),
            icon = $('<i class="fa fa-circle-o-notch fa-spin"></i>'),
            el = $('<span></span>').html('<i class="fa fa-rss"></i> ' + title),
            span = $('<span class="little"></span>');

        link.append(icon);
        li.append(el)
          .append(link)
          .append($('<br />'))
          .append(span);

        $('#feed_list').append(li);

        hunk.api.check_source(url, function(result) {
            icon.removeClass('fa-circle-o-notch fa-spin');

            if (result.status === 'exists') {
                icon.addClass('fa-check color-success');
                span.text('All good, this feed is already mined!');
            } else if (result.status === 'success') {
                icon.addClass('fa-plus color-success');
                span.html('This feed is still not mined by Minethat. <a href="#" class="add_feed">Add it now</a>?');

                $('a', span).on('click', function() {
                    popup.add_feed(url, li);
                });

                link.on('click', function() {
                    popup.add_feed(url, li);
                });

            } else if (result.status === 'error') {
                icon.addClass('fa-warning color-warning');
                span.text('An error occured with this feed. It\'s probably invalid.');
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
                console.log('Didnt got URL');
            }
        });
    };

    popup.onTrainClick = function () {
        popup.submit(function (tab_url) {
            if (!tab_url) {
                console.log('Didnt got URL');
                return;
            }

            var classe = $('[name=train_type]:checked').val();
            hunk.api.train(source, tab_url, classe);
        });
    },

    popup.status = function (msg) {
        $('#messages').text(msg);
    }


    $(function () {
        hunk.conf({
            api_server: localStorage.minethat_server
        });
        hunk();
    });

}(hunk('popup')));
