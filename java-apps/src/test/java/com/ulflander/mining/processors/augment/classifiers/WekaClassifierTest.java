package com.ulflander.mining.processors.augment.classifiers;

import com.ulflander.AbstractTest;
import com.ulflander.application.Conf;
import com.ulflander.application.Env;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Ulflander on 4/15/14.
 */
public class WekaClassifierTest extends AbstractTest {

    @Before
    public void setupProcessors () {
        s.addProcessor("extract.DocumentCleaner");
        s.addProcessor("extract.DocumentSplitter");
        s.addProcessor("extract.LanguageDetector");
        s.addProcessor("extract.TokenCleaner");
        s.addProcessor("extract.TokenListGuesser");
        s.addProcessor("augment.classifiers.WekaClassifier");
    }

    @Test
    public void testTrain () {
        Conf.setEnv(Env.TEST);
        d.setRaw("My name is Maria Johns and I live in Paris, Texas.");
        d.addProperty("meta", "job_target", "TRAIN");
        d.addProperty("meta", "job_training_classes", "facts");
        s.submit(d);
    }

}
