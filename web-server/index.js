/*jslint node:true*/

(function() {
    'use strict';

    var flatiron = require('flatiron'),
        session = require('connect').session,
        cookieParser = require('connect').cookieParser,
        app = flatiron.app,
        logger = require('./src/conf.js').logger,
        before = [],
        initRoutes = require('./src/routing.js').init;


    /**
     * Use tracer vs winston for Flatiron logging.
     */
    app.log = logger;

    /**
     * Setup preprocessors
     */
    before =before.concat(cookieParser());
    before = before.concat(session({
        secret: 'aT6biui97Yhvfj8765a3gg8jGv7b4U2afLIsUG76ui8yg'
    }));
    before = before.concat(require('./src/auth.js').auth);
    before = before.concat(require('./src/analytics.js').analytics);

    /**
     * Setup basics.
     */
    app.use(flatiron.plugins.http, {
        before: before
    });

    app.use(flatiron.plugins['static'], {
        dir: __dirname + '/static'
    });



    /**
     * Init routing.
     */
    initRoutes(app);


    /**
     * Start.
     */
    logger.info('Starting web server...');
    app.start(3000);

}());
