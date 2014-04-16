/*jslint browser: true, node:true*/

(function (ctx) {
    "use strict";

        /**
         * Modules
         * 
         * @type {Object}
         * @private
         */
    var mods = {},
        /**
         * Configuration
         * 
         * @type {Object}
         * @private
         */
        conf = {},
        /**
         * Is verbose.
         * 
         * @type {Boolean}
         * @private
         */
        vb = false,
        /**
         * Undefined type
         * 
         * @type {String}
         */
        u = "undefined",
        /**
         * Chunk main function: get or register a module.
         *
         * If no definition given it will try to return the module.
         * 
         * @param  {String} name Module identifier
         * @param  {Object} def  Module definition (Optional)
         * @return {mixed}       Chunk object reference if module registering,
         * module if only name given.
         */
        m = function (name, def) {
            if (typeof def == u) {
                return m.get(name);
            }

            mods[name] = def;

            if (typeof def.init === "function") {
                def.init();
            }

            return m;
        };

    /**
     * Set verbose mode.
     *
     * By default, verbose mode is off.
     *
     * In verbose mode, calling a module that doesn't exist generates
     * a warning in the console.
     * 
     * @param  {Boolean} v Is verbose or not
     * @return {chunk} Reference to chunk object for chained command
     */
    m.verbose = function (v) {
        vb = !!v;
        return m;
    };

    /**
     * Get a module or a function in a module.
     * 
     * @param  {String} n Module name
     * @return {Object}   Module reference or function reference.
     */
    m.get = function (n) {
        if (typeof n !== 'string') {
            if (!!vb) {
                console.warn("Trying to get module without providing a name");
            }

            return null;
        }

        if (!mods[n]) {
            if (!!vb) {
                console.warn("Module not found: " + n);
            }
            return null;
        }

        if (n.indexOf('.') > -1) {
            var f = n.split('.');
            n = f[0];
            f = f[1];
            return mods[n][f];
        }

        return mods[n];
    };

    /**
     * Trigger "start" function on all modules.
     *
     * @return {chunk} Reference to chunk object for chained command
     */
    m.start = function () {
        return m.trigger('start');
    };


    /**
     * Trigger "end" function on all modules.
     *
     * @return {chunk} Reference to chunk object for chained command
     */
    m.end = function () {
        return m.trigger('end');
    };

    /**
     * Trigger given function on all modules.
     * 
     * @param  {String} func Name of function to trigger on all modules
     * @return {chunk} Reference to chunk object for chained command
     */
    m.trigger = function (func) {
        for (var n in mods) {
            if (typeof mods[n][func] === "function") {
                mods[n][func]();
            }
        }

        return m;
    };

    /**
     * Set/get/getAll conf value(s)
     *
     * - If no parameter is given, this method returns a reference to
     * the configuration object.
     * - If only a string key is given then it returns the conf value.
     * - If key is an object, then it set all the key/values of object and
     * it returns the chunk object.
     * - If key and val is given, then chunk object is given.
     * 
     * @param  {String|Object} key Conf item key
     * @param  {Object} val Conf item value
     * @return {Object}     Conf object reference, conf value, or chunk 
     * object reference depending on given parameters
     */
    m.conf = function (key, val) {
        if (typeof key == u) {
            return conf;
        } else if (typeof val === u && typeof key === 'string') {
            if (!!vb && typeof conf[key] === u) {
                console.warn("Configuration key not found: " + key);
            }

            return conf[key];
        }

        if (typeof key === 'string') {
            conf[key] = val;
        } else {
            for (var k in key) {
                conf[k] = key[k];
            }
        }

        return m;
    };


    // Apply to context
    ctx.chunk = m;

}(window || exports));


