package com.ulflander.mining.processors.extract;

import com.ulflander.mining.processors.ILocalizedProcessor;
import com.ulflander.mining.processors.Processor;
import com.ulflander.mining.processors.ProcessorDepthControl;

import java.util.regex.Pattern;

/**
 * Processor that cleanup article contents fromInternet slang such as
 * "Read comments".
 *
 * Created by Ulflander on 4/14/14.
 */
public class RawArticleCleaner extends Processor
        implements ILocalizedProcessor {

    /**
     * Artifacts to remove.
     */
    private Pattern[] artifacts = {
        Pattern.compile("(signup|subscribe) (now|today)"),
        Pattern.compile("create an account"),
        Pattern.compile("log in"),
        Pattern.compile("(\\([0-9]+\\))")
    };

    @Override
    public final String[] getLanguages() {
        return new String[]{"en"};
    }

    @Override
    public final void init() {
        this.setDepthControl(ProcessorDepthControl.DOCUMENT);
        this.setInitialized(true);
    }

    @Override
    public final String describe() {
        return "Remove some artifacts from start and end of an article, "
                + "such as 'Subscribe now' or this kind of sentences.";
    }
}
