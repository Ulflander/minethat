/*jslint browser: true*/
/*globals chrome*/


(function (popup) {
    "use strict";

    var source,
        curr_url;


    /**
     * Init popup
     *
     */
    popup.start = function () {

        console.log('Start');

        if (typeof chrome === 'undefined' || !chrome.extension) {
            return;
        }

        // Callback when script has been injected and run
        chrome.extension.onMessage.addListener(function(request, sender) {
            if (request.action !== "getSource") {
                return;
            }

            var k;

            source = request.source;
            if (request.meta && request.meta.title) {
                $('#title').text(request.meta.title);
            }

            if (request.meta && request.meta.feeds) {
                for (k in request.meta.feeds) {
                    popup.check_feed(k, request.meta.feeds[k])
                }
            }

            // Gather URL if not found in meta
            if (request.meta && request.meta.url) {
                chrome.tabs.query ({
                    'active': true,
                    'windowId': chrome.windows.WINDOW_ID_CURRENT
                },
                function (tabs) {

                    // All good
                    if(!!tabs[0].url) {
                        $('#loading').hide();
                        $('#content').show();

                        curr_url = tabs[0].url;
                    } else {
                        popup.status('Page not minable');
                    }
                });
            } else {
                $('#loading').hide();
                $('#content').show();

                curr_url = request.meta.url;
            }
        });

        // Inject script (returns metadata about the page, and the source)
        chrome.tabs.executeScript(null, {
            file: "js/injected.js"
        }, function() {
            if (chrome.extension.lastError) {
                console.log('Error: ' + chrome.extension.lastError.message);
                $('#loading').hide();
                popup.status('Page not minable');
            }
        });


        $('#minethat_page').on('click', popup.onMineClick);
        $('#trainthat_page').on('click', popup.onTrainClick);
    };

    popup.add_feed = function(url, li) {
        console.log('add feed');
        hunk.api.add_source({
            feed_url: url
        }, function(res) {
            console.log(res);

            var icon = $('.fa-spin', li);
            icon.removeClass('fa-circle-o-notch fa-spin');

            if (res.status === 'success') {
                $('.little', li).text('Source has been added!');
                icon.addClass('fa-check color-success');
            } else {
                icon.addClass('fa-warning color-warning');
                $('.little', li).text('An error occured while adding source.');
            }
        })
    };

    popup.check_feed = function(title, url) {

        var li = $('<li></li>'),
            link = $('<a></a>').attr('href', '#'),
            icon = $('<i class="fa fa-circle-o-notch fa-spin"></i>'),
            el = $('<span></span>').html('<i class="fa fa-rss"></i> ' + title),
            span = $('<span class="little"></span>').text('Checking source...'),

            add = function() {
                icon.removeClass('fa-download')
                    .addClass('fa-circle-o-notch fa-spin');
                span.text('Adding source...');
                popup.add_feed(url, li);
            },

            cb = function(result) {
                icon.removeClass('fa-circle-o-notch fa-spin');

                if (result.status === 'exists') {
                    icon.addClass('fa-check color-success');
                    span.text('All good, this source is already mined!');
                } else if (result.status === 'success') {
                    icon.addClass('fa-download');
                    span.html('This source is still not mined by Minethat. <a href="#" class="add_feed">Add it now</a>?');

                    $('a', span).on('click', add);
                    link.on('click', add);

                } else if (result.status === 'error') {
                    icon.addClass('fa-warning color-warning');
                    span.text('An error occured with this source. It\'s probably invalid.');

                    $('a', span).on('click', function() {
                        icon.removeClass('fa-warning color-warning');
                        icon.addClass('fa-circle-o-notch fa-spin');
                        hunk.api.check_source(url, cb);
                    });
                }
            };

        link.append(icon);
        li.append(el)
          .append(link)
          .append($('<br />'))
          .append(span);

        $('#feed_list').append(li);

        hunk.api.check_source(url, cb);
    };

    popup.onMineClick = function () {
        if (!!curr_url) {
            hunk.api.submit(source, tab_url);
        } else {
            console.log('Didnt got URL');
        }
    };

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
