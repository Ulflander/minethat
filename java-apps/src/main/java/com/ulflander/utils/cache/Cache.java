package com.ulflander.utils.cache;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Cache is a HashMap where items disappeared once timeout reached.
 *
 * @param <String> Keys of cache items
 * @param <V>      Values of cache items
 *
 * @author Ulflander <xlaumonier@gmail.com>
 * @since 2/21/14
 */
public class Cache<String, V> extends HashMap<String, V> {

    /**
     * Logger.
     */
    private static final Logger LOGGER =
        LogManager.getLogger(Cache.class);

    /**
     * Default timeout: 1 hour (3600 seconds).
     */
    public static final long DEFAULT_TIMEOUT = 3600;

    /**
     * Number of milliseconds in a second.
     */
    private static final long MS_IN_SECOND = 1000;

    /**
     * Timeout in seconds.
     */
    private long timeout = DEFAULT_TIMEOUT;

    /**
     * Array of timestamps.
     */
    private Map<String, Long> timestamps = new HashMap<String, Long>();

    /**
     * Constructs an empty Cache with the specified initial capacity
     * and load factor.
     *
     * @param i Initial capacity
     * @param v Load factor
     */
    public Cache(final int i, final float v) {
        super(i, v);
    }

    /**
     * Constructs an empty Cache with the specified initial capacity
     * and the default load factor (0.75).
     *
     * @param i Initial capacity
     */
    public Cache(final int i) {
        super(i);
    }

    /**
     * Constructs an empty Cache with the default initial capacity (16)
     * and the default load factor (0.75).
     */
    public Cache() {
    }

    /**
     * Constructs an empty Cache.
     * <p/>
     * Initial mappings are DEACTIVATED.
     *
     * @param map Initial mappings.
     */
    public Cache(final Map<? extends String, ? extends V> map) {
        this();
        putMap((Map<String, V>) map);
    }

    /**
     * Get timeout value.
     *
     * @return Time before values are erased from Cache, in seconds
     */
    public final long getTimeout() {
        return timeout;
    }

    /**
     * Set timeout value (in seconds).
     *
     * @param to Timeout value in seconds.
     */
    public final void setTimeout(final long to) {
        this.timeout = to;
    }

    /**
     * Clean values that are older than timeout value.
     */
    public final void clean() {
        Iterator it = timestamps.entrySet().iterator();
        Long l = System.currentTimeMillis() - timeout * MS_IN_SECOND;

        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            if ((Long) pairs.getValue() < l) {
                this.remove(pairs.getKey());
            }
        }
    }

    /**
     * Put a new pair key/value in Cache.
     *
     * @param k Item key
     * @param v Item value
     * @return Item value
     */
    @Override
    public final V put(final String k, final V v) {
        timestamps.put(k, System.currentTimeMillis());
        return super.put(k, v);
    }

    /**
     * Put all function: DEACTIVATED - will trigger a log error.
     *
     * @param map Mapping of key/values
     */
    @Override
    public final void putAll(final Map<? extends String, ? extends V> map) {
        LOGGER.error("Cache.putAll is not usable. You may use Cache.putMap");
    }

    /**
     * Remove an element by its key.
     *
     * @param o Key
     * @return Object removed
     */
    @Override
    public final V remove(final Object o) {
        timestamps.remove(o);
        return super.remove(o);
    }

    /**
     * Clear the map.
     */
    @Override
    public final void clear() {
        timestamps.clear();
        super.clear();
    }

    /**
     * Put a map into the Cache.
     *
     * @param map Mappings to add to cache
     */
    public final void putMap(final Map<String, V> map) {
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            put((String) pairs.getKey(), (V) pairs.getValue());
        }
    }
}
