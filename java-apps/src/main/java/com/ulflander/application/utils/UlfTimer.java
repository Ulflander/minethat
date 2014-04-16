package com.ulflander.application.utils;

/**
 * A very simple timer class.
 *
 * Timer starts when class is instanciated,
 * and ends when end() method is called.
 *
 * @author Ulflander <xlaumonier@gmail.com>
 * @since 2/21/14
 */
public class UlfTimer {

    /**
     * Start timestamp.
     */
    private long start = System.currentTimeMillis();

    /**
     * End timestamp.
     */
    private long end = 0;

    /**
     * Stop timer and returns length.
     *
     * @return number of milliseconds since start
     */
    public final long end() {
        if (end == 0) {
            end = System.currentTimeMillis() - start;
        }
        return end;
    }

}
