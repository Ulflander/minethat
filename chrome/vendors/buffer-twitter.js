;(function() {

    $('head').append([
        '<style>',
            '.tweet .action-buffer-container span.icon, .tweet.opened-tweet .action-buffer-container span.icon, .tweet.opened-tweet.hover .action-buffer-container span.icon  {',
                'background-position: -3px -3px !important;} ',
            '.tweet:hover .action-buffer-container span.icon {',
                'background-position: -3px  -21px !important;} ',
            '.gallery-tweet .tweet .action-buffer-container span {',
                'background-position: -3px  -38px !important; margin-top: -1px;} ',
            '.tweet:hover .action-buffer-container span.Icon {',
                'background-position: -5px -28px !important;} ',
            '.tweet .action-buffer-container span.Icon:hover {',
                'background-color: #8899a6 !important;} ',
            'div.stream-item-footer > ul.tweet-actions > li.action-buffer-container i {margin-top: 0px ;} ',
            '#profile_popup-body > ol > li > div > div.content > div.stream-item-footer > ul.tweet-actions > li.action-buffer-container {display: none;}',
        '</style>'
    ].join(''));

    var buildElement = function buildElement (parentConfig) {
        
        var temp = document.createElement(parentConfig[0]);
        if( parentConfig[1] ) temp.setAttribute('class', parentConfig[1]);
        if( parentConfig[2] ) temp.setAttribute('style', parentConfig[2]);

        if ( parentConfig.length > 3 ) {
            var i = 3, l = parentConfig.length;
            for(; i < l; i++) {
                temp.appendChild(buildElement(parentConfig[i]));
            }
        }
        
        return temp;
        
    };
    
    var config = {};
    config.time = {
        success: {
            delay: 2000
        }
    };
    var should_be_native_retweet = true;
    config.buttons = [
        {
            name: "buffer-action",
            text: "Buffer",
            container: '.tweet-actions',
            after: '.action-fav-container',
            default: '',
            className: 'buffer-action',
            selector: '.buffer-action',
            style: '',
            hover: '',
            active: '',
            create: function (btnConfig) {

                var li = document.createElement('li');
                li.className = "action-buffer-container";

                var a = document.createElement('a');
                a.setAttribute('class', btnConfig.className + " with-icn");
                a.setAttribute('href', '#');

                var i = document.createElement('span');
                i.setAttribute('class', 'icon sm-reply'); // let Twitter set the bg colors
                i.setAttribute('style', 'position: relative; top: 0px; margin-right: 4px; background-image: url(' + xt.data.get('data/shared/img/twttr-sprite-small.png') + ')!important; background-repeat: no-repeat;');

                $(a).append(i);

                var b = document.createElement('b');
                $(b).text(btnConfig.text);

                $(a).append(b);

                $(li).append(a);

                return li;


            },
            data: function (elem) {
                var c = $(elem).closest('.tweet');
                // Grab the tweet text
                var text = c.find('.js-tweet-text').first();
                // Iterate through all links in the text
                $(text).children('a').each(function () {
                    // Don't modify the screenames and the hastags
                    if( $(this).attr('data-screen-name') ) return;
                    if( $(this).hasClass('twitter-atreply') ) return;
                    if( $(this).hasClass('twitter-hashtag') ) return;
                    // swap the text with the actual link
                    var original = $(this).text();
                    $(this).text($(this).attr("href")).attr('data-original-text', original);
                });
                // Build the RT text
                var rt = 'RT ' + c.find('.username').first().text().trim() + ': ' + $(text).text().trim() + '';
                // Put the right links back
                $(text).children('a').each(function () {
                    if( ! $(this).attr('data-original-text') ) return;
                    $(this).text($(this).attr('data-original-text'));
                });
                // Send back the data
                if (should_be_native_retweet) {
                    return {
                        text: rt,
                        placement: 'twitter-permalink',
                        // grab info for retweeting
                        retweeted_tweet_id: c.data('feedback-key').replace('stream_status_', ''),
                        retweeted_user_id: c.data('user-id'),
                        retweeted_user_name: c.data('screen-name'),
                        retweeted_user_display_name: c.data('name')
                    };
                } else {
                    return {
                        text: rt,
                        placement: 'twitter-feed'
                    };
                }
            },
            clear: function (elem) {
            },
            activator: function (elem, btnConfig) {

                if( $(elem).closest('.in-reply-to').length > 0 ) {
                    $(elem).find('i').css({'background-position-y': '-21px'});
                }
            }
        },
    ];

    var insertButtons = function () {

        var i, l=config.buttons.length;
        for ( i=0 ; i < l; i++ ) {

            var btnConfig = config.buttons[i];
            
            $(btnConfig.container).each(function () {
                
                var container = $(this);
                
                if( !! btnConfig.ignore ) {
                    if( btnConfig.ignore(container) ) return;
                }
                
                if ( $(container).hasClass('buffer-inserted') ) return;

                $(container).addClass('buffer-inserted');

                var btn = btnConfig.create(btnConfig);

                $(container).find(btnConfig.after).after(btn);

                if ( !! btnConfig.activator) btnConfig.activator(btn, btnConfig);
                
                var getData = btnConfig.data;
                var clearData = btnConfig.clear;
                
                var clearcb = function () {};

                $(btn).click(function (e) {
                    clearcb = function () { // allow clear to be called for this button
                        if ( !! clearData ) clearData(btn);
                    };
                    xt.port.emit("buffer_click", getData(btn));
                    e.preventDefault();
                });
                
                xt.port.on("buffer_embed_clear", function () {
                    clearcb();
                    clearcb = function () {}; // prevent clear from being called again, until the button is clicked again
                });
                
            });

        }

    };

    /**
     * Remove extra buttons that are not needed or wanted
     */
    var removeExtras = function () {
        $('.replies .buffer-tweet-button').remove();
        $('.inline-reply-tweetbox .buffer-tweet-button').remove();
    };

    var twitterLoop = function twitterLoop() {
        insertButtons();
        removeExtras();
        setTimeout(twitterLoop, 500);
    };

    // Wait for xt.options to be set
    ;(function check() {
        // If twitter is switched on, start the main loop
        if ( !xt.options) {
            setTimeout(check, 0);
        }
        else if( xt.options['buffer.op.twitter'] === 'twitter') {
            twitterLoop();
        } else {
            setTimeout(check, 2000);
        }
    }());

    
}());