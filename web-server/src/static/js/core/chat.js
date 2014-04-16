/*jslint browser: true*/
/*global modular, console, $*/


(function (m) {

    "use strict";

    modular.module("chat", function (deps, store, options) {

        var module = {
            interface: ["display"],
            start: function () {
                return null;
            },

            send: function () {
                return null;
            },

            display: function (msg, type) {
                console.log(msg, type)
                return null;
            }
        };

        return module;
    });

}(modular));

