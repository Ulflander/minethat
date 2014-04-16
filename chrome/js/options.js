/*jslint browser:true*/

(function () {
    "use strict";

    // Saves options to localStorage.
    function save_options() {
        localStorage.minethat_private_key = 
            document.getElementById("private_key").value;

        localStorage.minethat_server = 
            document.getElementById("server").value;

        // Update status to let user know options were saved.
        var status = document.getElementById("status");
        status.innerHTML = "Options Saved.";

        setTimeout(function() {
            status.innerHTML = "";
        }, 750);
    }

    // Restores select box state to saved value from localStorage.
    function restore_options() {
        document.getElementById("private_key").value = 
            localStorage.minethat_private_key || '';

        document.getElementById("server").value = 
            localStorage.minethat_server || 'http://minethat.co';
    }

    document.addEventListener('DOMContentLoaded', restore_options);
    document.querySelector('#save')
        .addEventListener('click', save_options);
}());
