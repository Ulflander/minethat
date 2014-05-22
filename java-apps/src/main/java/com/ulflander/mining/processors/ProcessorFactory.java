package com.ulflander.mining.processors;

import java.util.HashMap;

/**
 * ProcessorFactory instanciate and return processors,
 * initializing them if necessary.
 *
 * @author Ulflander <xlaumonier@gmail.com>
 * @since 2/21/14
 */
public final class ProcessorFactory {

    /**
     * Requirements manager.
     */
    private static ProcessorsRequirements requirements =
        new ProcessorsRequirements();

    /**
     * Private constructor.
     */
    private ProcessorFactory() {
        // no call
    }

    /**
     * Cache of processors.
     */
    private static HashMap<String, Processor> processors =
        new HashMap<String, Processor>();

    /**
     * Get requirements utility.
     *
     * @return Requirements utility
     */
    public static ProcessorsRequirements requirements() {
        return requirements;
    }

    /**
     * Get a processor instance by name.
     *
     * @param name Name of the processor
     * @return Processor instance
     * @throws java.lang.ClassNotFoundException If processor not found
     * @throws java.lang.InstantiationException If processor not instanciable
     * @throws java.lang.IllegalAccessException If processor not accessible
     */
    public static Processor get(final String name) throws
            ClassNotFoundException,
            InstantiationException,
            IllegalAccessException {

        String n = getFullName(name);

        if (processors.containsKey(n)) {
            return processors.get(n);
        }

        Class<?> clazz = Class.forName(n);

        Processor processor = (Processor) clazz.newInstance();

        requirements.extract(name, processor);

        processor.init();

        processors.put(n, processor);

        return processor;
    }

    /**
     * Get a processor instance by class.
     *
     * @param clazz Classe of the processor
     * @return Processor instance
     * @throws java.lang.IllegalAccessException If processor not accessible
     * @throws java.lang.InstantiationException If processor not instanciable
     */
    public static Processor get(final Class clazz) throws
            InstantiationException,
            IllegalAccessException {

        String n = clazz.getCanonicalName();
        if (processors.containsKey(n)) {
            return processors.get(n);
        }

        Processor processor = (Processor) clazz.newInstance();
        processor.init();

        processors.put(n, processor);

        return processor;
    }

    /**
     * Returns full class name (including packages) from simple name.
     * <p/>
     * <code>
     * String full = ProcessorFactory.getFullName("extract.DocumentCleaner");
     * // full == "com.ulflander.mining.processors.extract.DocumentCleaner"
     * </code>
     *
     * @param name Simple name of the name
     * @return Full name of processor class simple name
     */
    public static String getFullName(final String name) {
        return "com.ulflander.mining.processors." + name;
    }


    /**
     * Get requirements for given processor name.
     *
     * @param name Name of processor
     * @return Requirements of given processor
     */
    public static String[] getRequirements(final String name) {
        return null;
    }
}
