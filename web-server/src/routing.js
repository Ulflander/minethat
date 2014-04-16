/*jslint node:true*/


/**
 * Routes.
 *
 * Setup all routes.
 */
(function() {
    'use strict';

    var jobs = require('./controllers/jobs.js').jobs,
        logger = require('./conf.js').logger,
        routes = require('./routes.js').routes,
        /**
         * Setup a route given the method, the route and the callback
         *
         * @param {FlatironApp} app App
         * @param {String} method Controller method name
         * @param {String} route Route
         * @param {Function} callback Callback
         */
        setup = function(app, method, route, callback) {
            app.router[method](route, function() {

                this.req.mt = this.req.mt || {};
                this.req.mt.log = logger;

                // Mark as API if needed
                if (route.indexOf('/api') === 0) {
                    this.req.mt.isAPI = true;
                } else {
                    this.req.mt.isAPI = false;
                }

                // And call controller
                callback(this.req, this.res);
            });
        };


    /**
     * Initialize routing of application
     *
     * @param  {FlatironApp} app
     */
    exports.init = function(app) {

        var controllers = {},
            method,
            controller,
            callback,
            route,
            split_route,
            final_route,
            i,
            l;

        // Loop on given route
        // Load controller if not loaded
        // Setup router with controller and callback
        for (route in routes) {
            method = routes[route][0];
            controller = routes[route][1];
            split_route = route.split('/');



            // If no callback given, consider it's index
            if (controller.indexOf('.') === -1) {
                controller += '.index';
            }
            controller = controller.split('.');
            callback = controller[1];
            controller = controller[0];

            // If controller not loaded, require it now
            if (!controllers[controller]) {
                try {
                    controllers[controller] = require('./controllers/' +
                                controller + '.js')[controller];
                } catch (e) {
                    logger.error('Controller not found: ' + controller, e);
                    process.exit(1);
                }
            }

            // Check for callback
            if (typeof controllers[controller][callback] !== 'function') {
                logger.error('Controller callback not found: ' +
                            controller + '.' + callback);
                process.exit(1);
            }

            // Associate callback to Flatiron router
            setup(app, method, route, controllers[controller][callback]);
        }

    };

}());
