package com.ulflander.mining.processors.load;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ulflander.app.model.Document;
import com.ulflander.mining.processors.LoadProcessor;
import com.ulflander.mining.processors.ProcessorDepthControl;
import com.ulflander.utils.UlfFileUtils;

/**
 * Processor that saves analysis as JSON.
 *
 * @author Ulflander <xlaumonier@gmail.com>
 * @since 2/21/14
 */
public class JSONFile extends LoadProcessor {

    /**
     * Initialize the processor.
     */
    @Override
    public final void init() {
        setDepthControl(ProcessorDepthControl.DOCUMENT);
        setInitialized(true);
    }

    /**
     * Provides a description of this Processor.
     *
     * @return Description of this processor
     */
    @Override
    public final String describe() {
        return "Save media and media analysis as JSON file";
    }

    /**
     * Save document and analysis result as a JSON on filesystem.
     *
     * @param doc Document to run processor on
     */
    @Override
    public final void extractDocument(final Document doc) {

        Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .excludeFieldsWithoutExposeAnnotation()
            .create();

        UlfFileUtils.write("/tmp/test.json", gson.toJson(doc));
    }
}
