package com.ulflander.mining.processors.preset;

import com.ulflander.app.model.JobProcessor;

import java.util.ArrayList;

/**
 * Interface to deal with some presets of processors.
 *
 * @author Ulflander <xlaumonier@gmail.com>
 * @since 2/23/14
 */
public interface IPreset {

    /**
     * Return a list of processors to run in the preset.
     *
     * @return A list of all processors to run on documents using the preset.
     */
    ArrayList<JobProcessor> getProcessors();

}
