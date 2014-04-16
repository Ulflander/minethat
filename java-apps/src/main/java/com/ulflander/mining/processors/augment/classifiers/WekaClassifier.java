package com.ulflander.mining.processors.augment.classifiers;

import com.ulflander.application.Conf;
import com.ulflander.application.model.Paragraph;
import com.ulflander.mining.processors.Processor;
import com.ulflander.mining.processors.ProcessorDepthControl;
import com.ulflander.mining.processors.Requires;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Classify paragraphs using Weka classifier.
 *
 * Created by Ulflander on 4/15/14.
 */
@Requires(processors = {
        "extract.DocumentSplitter"
})
public class WekaClassifier extends Processor {


    /**
     * J48 Weka classifier.
     */
    private J48 classifier;

    /**
     * String to word vector filter.
     */
    private StringToWordVector filter;

    /**
     * Weka structure.
     */
    private Instances structure;

    /**
     * Weka filtered structure.
     */
    private Instances filtered;

    /**
     * Logger.
     */
    private static final Logger LOGGER =
        LogManager.getLogger(WekaClassifier.class);

    @Override
    public final void init() {
        this.setDepthControl(ProcessorDepthControl.PARAGRAPH);
        this.reset();
        this.setInitialized(true);
    }

    /**
     * Reset classifier.
     */
    private void reset() {

        ArffLoader loader = new ArffLoader();

        try {
            loader.setFile(new File(
                    Conf.getDataPath()
                    + "weka/"
                    + Conf.getEnv().toString().toLowerCase()
                    + "/test.arff"
            ));
            structure = loader.getStructure();
        } catch (IOException e) {
            LOGGER.error("Unable to read training file", e);
            return;
        }


        structure.setClassIndex(0);
        System.out.println(structure.numAttributes());

        filter = new StringToWordVector();
        try {
            filter.setInputFormat(structure);
            filtered = Filter.useFilter(structure, filter);
        } catch (Exception e) {
            LOGGER.error("Unable to convert strings to vector", e);
            return;
        }

        System.out.println(structure.numAttributes());

        filtered.setClassIndex(0);

        try {
            classifier = new J48();
            classifier.buildClassifier(filtered);
        } catch (Exception e) {
            LOGGER.error("Unable to build classifier", e);
            classifier = null;
            return;
        }
    }

    @Override
    public final String describe() {
        return "Train or classify weka classifier using document content";
    }

    @Override
    public final void extractParagraph(final Paragraph paragraph) {

        System.out.println(
                current().getProperty("meta", "job_target") + " / "
                + current().getProperty("meta", "job_target", "MINE")
        );

        if (current()
                .getProperty("meta", "job_target", "MINE")
                .equals("TRAIN")) {

            train(paragraph);

            // Update classifier, will be replaced by
            //
            reset();
        } else {
            LOGGER.debug("Skip training");
        }

        classify(paragraph);
    }

    /**
     * Classify paragraph.
     *
     * @param paragraph Paragraph
     */
    private void classify(final Paragraph paragraph) {
        if (classifier == null) {
            LOGGER.error("Try to run classification but no classifier");
            return;
        }


        // Make separate little test set so that message
        // does not get added to string attribute in m_Data.
        Instances testSet = structure.stringFreeStructure();

        // Make message into test instance.
        Instance instance = makeInstance(paragraph.getRaw(), testSet);

        try {
            // Filter instance.
            filter.input(instance);
            Instance filteredInstance = filter.output();

            // Get index of predicted class value.
            double predicted = classifier.classifyInstance(filteredInstance);

            // Output class value.
            System.err.println("Message classified as : "
                    + structure.classAttribute().value((int) predicted));

        } catch (Exception e) {
            LOGGER.error("Classification failed", e);
        }

    }

    /**
     * Method that converts a text message into an instance.
     *
     * @param text Text
     * @param data Dataset
     * @return Weka instance
     */
    private Instance makeInstance(final String text, final Instances data) {

        // Create instance of length two.
        Instance instance = new Instance(2);

        // Set value for message attribute
        Attribute messageAtt = data.attribute("text");
        instance.setValue(messageAtt, messageAtt.addStringValue(text));

        // Give instance access to attribute information from the dataset.
        instance.setDataset(data);
        return instance;
    }

    /**
     * Train weka classifier.
     *
     * @param paragraph Paragraph
     */
    private void train(final Paragraph paragraph) {
        if (!current().hasProperty("meta", "job_training_classes")) {

            LOGGER.error("Trying to train classifier without "
                        + "providing classes, doc " + current().getId());
            return;
        }

        // Prepare classes
        String[] classes =
                ((String) current()
                        .getProperty("meta", "job_training_classes"))
                        .split(",");

        // Prepare output file
        String filename = Conf.getDataPath() + "weka/"
                + Conf.getEnv().toString().toLowerCase() + "/test.arff";

        try {
            FileWriter fw = new FileWriter(filename, true);
            for (String classe: classes) {
                fw.write(
                    "\n" + classe
                    + ",'"
                    + paragraph.getRaw().replaceAll("'", "")
                    + "'"
                );
            }
            fw.close();
        } catch (IOException ioe) {
            LOGGER.error("Unable to train weka classifier, file error, doc"
                        + current().getId(), ioe);
        }
    }
}
