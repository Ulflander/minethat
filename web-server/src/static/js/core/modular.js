/*jslint browser: true*/
/*global modular:true, console, $*/

var modular = window.modular || {};

(function (m) {

    "use strict";

    var store = {};

    m.module = function (name, module) {
        if (!!store[name]) {
            console.error("Module " +
                module + " already registered");
            return;
        }

        store[name] = module;
    };

    m.app = function (appName) {
        var modules = [],
            routes = {},
            active = {},
            stores = {},
            app,
            started = false;
        
        app = {

            use: function (name, options) {
                if(!options) {
                    options = {
                        routes: routes,
                        appName: appName,
                        container: null
                    };
                }

                modules.push(store[name](app, options));

                return this;
            },

            consume: function (name) {
                var s = {};
                stores[name] = {
                    get: function (key) {
                        if(!s.hasOwnProperty(key)) {
                            return null;
                        }
                        return s[key];
                    },
                    set: function (key, value) {
                        s[key] = value;
                    },
                    setAll: function (obj) {
                        var k;
                        for (k in obj) {
                            stores[name].set(k, obj[k]);
                        }
                    }
                };
            },

            store: function (name) {
                return stores[name];
            },

            route: function (route, module, options) {

                if(!options) {
                    options = {
                        routes: routes,
                        appName: appName
                    };
                }

                routes[route] = store[name](app, options);

                return this;
            },

            start: function () {

                if(started) {
                    return true;
                }

                var i = 0, l = modules.length;
                for (i; i < l; i += 1) {
                    modules[i].start();
                }

                $(window).on('hashchange', function() {
                    var hash = window.location.hash,
                        route;

                    while (hash.length > 0 && (hash[0] === '#' || hash[0] === '!' || hash[0] === '/')) {
                        hash = hash.substring(1) ;
                    }

                    for (route in routes) {
                        if(route === "*" || route.indexOf(hash) === 0) {
                            route.start();
                        } else {

                        }
                    }
                });

                return this;
            }
        };

        return app;
    };



}(modular));


