/*jslint browser:true*/

(function (options) {
    "use strict";

    options.save = function() {

        localStorage.minethat_private_key = $("#private_key").val();
        localStorage.minethat_server = $("#server").val();

        $('#status').text("Your options have been saved.");

        setTimeout(function() {
            status.innerHTML = "";
        }, 2000);
    };

    options.restore = function() {
        $("#private_key").val(localStorage.minethat_private_key || '');

        $("#server").val(localStorage.minethat_server || 'http://minethat.co');
    };

    $(function () {
        options.restore();
        $('#save').on('click', options.save);
        hunk();
    });

}(hunk('options')));
