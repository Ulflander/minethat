package com.ulflander.mining.processors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Extract requirements from annotations and validate document history.
 *
 * @author Ulflander <xlaumonier@gmail.com>
 * @since 2/22/14
 */
public class ProcessorsRequirements {

    /**
     * Logger.
     */
    private static final Logger LOGGER =
        LogManager.getLogger(ProcessorsRequirements.class);

    /**
     * Processors requirements.
     */
    private HashMap<String, String[]> requirements =
        new HashMap<String, String[]>();

    /**
     * Get all requirements.
     *
     * @return A clone of internal representation of requirements
     */
    public final HashMap<String, String[]> getRequirements() {
        return (HashMap<String, String[]>) requirements.clone();
    }

    /**
     * Extract requirements using Requires annotation.
     *
     * @param n Name of processor
     * @param p Processor to extract requirements from
     */
    public final void extract(final String n, final Processor p) {
        Annotation a = p.getClass().getAnnotation(Requires.class);
        if (a == null) {
            return;
        }

        Class aClazz = a.annotationType();
        String[] reqs;

        try {
            reqs = (String[]) aClazz.getMethod("processors").invoke(a);
        } catch (NoSuchMethodException e) {
            return;
        } catch (IllegalAccessException e) {
            return;
        } catch (InvocationTargetException e) {
            return;
        } catch (Exception e) {
            LOGGER.error("Extraction of processor requirements failed", e);
            return;
        }

        requirements.put(n, reqs);
    }

    /**
     * Check in given history if requirements for given processor are met.
     *
     * @param processorName Name of the processor to be run
     * @param history History of previous run processors
     * @return True if requirements are met, e.g. history contains all required
     * processors, false otherwise
     */
    public final boolean areMet(final String processorName,
                                final ArrayList<String> history) {
        if (!requirements.containsKey(processorName)) {
            return true;
        }

        String[] reqs = requirements.get(processorName);
        for (String req : reqs) {
            if (!history.contains(req)) {
                LOGGER.warn("Processor " + processorName + " requires " + req);
                return false;
            }
        }

        return true;
    }
}
