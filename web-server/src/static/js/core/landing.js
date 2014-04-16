/*jslint browser: true*/
/*globals $, ga*/


(function () {
    'use strict';

    var $_GET = {},

        idx = 0,

        first_focus = false,

        submitted = false,

        errors = {},

        get_get = function () {
            var query, i, l, aux;
            if(document.location.toString().indexOf('?') !== -1) {
                query = document.location.toString().replace(/^.*?\?/, '').split('&');

                for(i=0, l=query.length; i<l; i +=1)
                {
                   aux = decodeURIComponent(query[i]).split('=');
                   $_GET[aux[0]] = aux[1];
                }
            }
        },

        show_message = function (msg, length) {
            var n;
            idx += 1;
            n = '_msg_' + idx;
            $('body')
                .append('<div id="' + n + '" class="global message info">' + msg + '</div>')
                .fadeIn();
            setTimeout(function () {                
                $('#'+n).fadeOut();
            }, length || 5000)
        },

        show_error = function (selector) {
            var visible = errors[selector] || false;
            if(!visible) {
                errors[selector] = true;

                $(selector).addClass('invalid');
                $(selector + ' .message').fadeIn();

                setTimeout(function () {
                    hide_error(selector);
                }, 5000);
            } else {
                $(selector + ' .message').fadeOut(200, function(){
                    $(selector + ' .message').fadeIn(200);
                });
            }
        },
        
        hide_error = function (selector) {
            if(!!errors[selector]) {
                errors[selector] = false;

                $(selector).removeClass('invalid');
                $(selector + ' .message')
                    .fadeOut();
            }
        },

        validate = function (hide) {
            var val = $('input[name=email]').val(),
                at_idx = val.indexOf('@'),
                dot_idx = val.lastIndexOf('.');

            if(val === '' && hide === true) {
                hide_error('.input-wrap.email');
                return false;
            }

            if (at_idx > 0 && dot_idx > 3 
                && at_idx < dot_idx && val.length > dot_idx + 2) {
                hide_error('.input-wrap.email');
                return true;
            } else {
                show_error('.input-wrap.email');
            }

            return false;
        };

    $('.input-wrap.email .message').on('click', function() {
        $(this).fadeOut();
    });

    $('#subscribed a').on('click', function () {
        $('input[name=email]').on('focus', function () {
            ga('send', 'event', 'landing', 'share');
        });
    });

    $('input[name=email]').on('focus', function () {
        ga('send', 'event', 'landing', 'focus_input');
    });

    $('input[name=email]').on('blur', function (e) {
        validate(true);
    });

    $('#subscribe').on('submit', function (e) {

        e.preventDefault();

        if(validate()) {
            ga('send', 'event', 'landing', 'subscribe');
            
            $.post('/ajax/landing_subscribe', $('#subscribe').serialize(), 
                function (res) {
                    var status = !!res ? res.status : 'error',
                        error = !!res ? res.error : null;

                    if (status === 'success') {
                        ga('send', 'event', 'landing', 'subscribe');
                        $('#subscribe').hide();
                        $('#subscribed').fadeIn(200);
                    } else if (status === 'exists') {
                        $('form .result').html('It seems that you\'re '
                            + 'already registered. Thanks for that :) Do '
                            + 'you want to  <a href="/subscription?id='
                            + res.id
                            + '">edit your subscription</a>?').show();
                        $('input[name=email]').val('');
                    } else if (status === 'error') {
                        $('form .result').html('Oups, an error occured. You may try again?').show();
                    }
                    
                });

            return;
        }

    });

    if(!!$_GET.r) {
        switch ($_GET.r) {
            case 'r1':
                show_message('<p>Hello sir!</p><p>Thanks a lot taking a few '
                    + 'seconds to checkout my landing page. '
                    + 'I really appreciate it!</p><p>Any feedback will be '
                    + 'constructive for me, so feel free to comment on r/startups!')
        }
    }
}());



