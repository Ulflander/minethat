package com.ulflander.app;

/**
 * Defines environment.
 *
 * @author Ulflander <xlaumonier@gmail.com>
 * @since 2/28/14
 */
public enum Env {

    /**
     * Service run in local environment.
     */
    LOCAL("local"),

    /**
     * Service run in test environment.
     */
    TEST("test"),

    /**
     * Service run in dev environment.
     */
    DEV("dev"),

    /**
     * Service run in prod environment.
     */
    PROD("prod");


    /**
     * Env.
     */
    private final String e;

    /**
     * Initialize Env type.
     *
     * @param t Tag.
     */
    private Env(final String t) {
        this.e = t;
    }

    /**
     * Returns the encoding for this Env.
     *
     * @return A string representing an Env
     * part-of-speech.
     */
    public String toString() {
        return getEnv();
    }

    /**
     * Get Env.
     *
     * @return Env as string.
     */
    protected String getEnv() {
        return this.e;
    }

    /**
     * Get Env value given its string representation.
     *
     * @param value String representation of an env (test, local, dev, prod)
     * @return PennPOSTag representation
     */
    public static Env get(final String value) {
        if (value == null) {
            return Env.TEST;
        }

        for (Env v : values()) {
            if (value.equals(v.getEnv())) {
                return v;
            }
        }

        throw new IllegalArgumentException("Unknown environment: '"
                + value + "'.");
    }
}
