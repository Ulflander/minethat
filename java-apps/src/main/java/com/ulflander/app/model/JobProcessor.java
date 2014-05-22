package com.ulflander.app.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * A Processor representation as for a model.
 *
 * @since 2/24/14
 * @author Ulflander <xlaumonier@gmail.com>
 */
public class JobProcessor implements Serializable {

    /**
     * Processor name.
     */
    private String name;

    /**
     * Processor class.
     */
    private Class clazz = null;

    /**
     * Processor options.
     */
    private ArrayList<JobProcessorOption> options;

    /**
     * Create a new JobProcessor for given processor name.
     *
     * @param n Name of processor to run
     */
    public JobProcessor(final String n) {
        this(n, null);
    }

    /**
     * Create a new JobProcessor for given processor name.
     *
     * @param c Class
     */
    public JobProcessor(final Class c) {
        this.clazz = c;
        String[] nArray = c.getName().split("\\.");
        String n = "";
        for (String s: nArray) {
            if (s.equals("com") || s.equals("ulflander")
                    || s.equals("mining") || s.equals("processors")) {
                continue;
            }

            n += "." + s;
        }

        this.name = n.substring(1);
    }

    /**
     * Create a new JobProcessor for given processor name and options.
     *
     * @param n Name of processor to run
     * @param os Options for processor
     */
    public JobProcessor(final String n,
                        final ArrayList<JobProcessorOption> os) {
        this.name = n;
        this.options = os;
    }

    /**
     * Get processor name.
     *
     * @return Processor name
     */
    public final String getName() {
        return name;
    }

    /**
     * Set processor name.
     *
     * @param n Processor name
     */
    public final void setName(final String n) {
        this.name = n;
    }

    /**
     * Get processor options list.
     *
     * @return Processor options
     */
    public final ArrayList<JobProcessorOption> getOptions() {
        return options;
    }

    /**
     * Set processor options list.
     *
     * @param pso Processor options
     */
    public final void setOptions(final ArrayList<JobProcessorOption> pso) {
        this.options = pso;
    }


    /**
     * Get clazz.
     *
     * @return Clazz
     */
    public final Class getClazz() {
        return clazz;
    }

    /**
     * Set clazz.
     *
     * @param c Clazz
     */
    public final void setClazz(final Class c) {
        this.clazz = c;
    }

    /**
     * Has class.
     *
     * @return True if a class is set, false otherwise
     */
    public final boolean hasClazz() {
        return this.clazz != null;
    }
}
