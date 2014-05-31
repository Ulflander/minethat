

(function(msg) {
    'use strict';


    msg.display = function (css, msg, timeout) {
        var div = $('<div></div>')
                    .addClass(css)
                    .html(msg);

        $('#messages').append(div);

        timeout = parseInt(timeout, 10);

        if (!!timeout) {
            setTimeout(function (){
                div.remove();
            }, timeout)
        }

        return div;
    };

    msg.success = function (msg, timeout) {
        msg.display('success', msg, timeout);
    };

}(hunk('msg')));