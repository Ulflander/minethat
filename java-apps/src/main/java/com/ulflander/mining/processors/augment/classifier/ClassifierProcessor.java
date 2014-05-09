package com.ulflander.mining.processors.augment.classifier;

import com.ulflander.app.model.Document;
import com.ulflander.mining.processors.Processor;
import com.ulflander.mining.processors.ProcessorDepthControl;

/**
 * Classifies a content using core Naive Bayes classifier.
 *
 * Created by Ulflander on 4/16/14.
 */
public class ClassifierProcessor extends Processor {

    @Override
    public final void init() {
        this.setDepthControl(ProcessorDepthControl.DOCUMENT);
        this.setInitialized(true);
    }

    @Override
    public final String describe() {
        return "Classify documents using NaiveBayesClassifier";
    }

    @Override
    public final void extractDocument(final Document doc) {
        super.extractDocument(doc);
    }
}
