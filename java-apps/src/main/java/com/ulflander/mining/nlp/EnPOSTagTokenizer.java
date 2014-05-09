package com.ulflander.mining.nlp;

import com.ulflander.app.Conf;
import com.ulflander.app.model.Sentence;
import com.ulflander.app.model.Token;
import com.ulflander.app.model.TokenType;
import com.ulflander.utils.UlfStringUtils;
import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Tokenize sentence based on Stanford POSTagger result.
 *
 * It also applies Stanford NER on sentences.
 *
 * Created by Ulflander on 4/16/14.
 */
public final class EnPOSTagTokenizer extends Tokenizer {

    /**
     * Classifier.
     */
    private static AbstractSequenceClassifier<CoreLabel> classifier =
            null;

    /**
     * Logger.
     */
    private static final Logger LOGGER =
        LogManager.getLogger(EnPOSTagTokenizer.class);


    /**
     * Creates a new tokenizer.
     */
    public EnPOSTagTokenizer() {
        if (classifier == null) {
            try {
                classifier = CRFClassifier.getClassifier(Conf.getDataPath() +
                    "ner-classifiers/english.muc.7class.distsim.crf.ser.gz");
            } catch (IOException e) {
                LOGGER.error("Unable to load NER classifier", e);
            } catch (ClassNotFoundException e) {
                LOGGER.error("Unable to instanciate NER classifier", e);
            }
        }
    }

    @Override
    public void tokenize(final Sentence sentence) {

        String raw = sentence.getRawPartOfSpeech();
        String[] tokens = raw.split(" ");
        int i, l = tokens.length;
        Token prev = null;
        String nerResult = null;
        String[] nerTokens = null;

        // While tokenizing, let's run NER
        // and cleanup the result:
        if (classifier != null) {
            nerResult = UlfStringUtils.cleanSpaces(
                    classifier.classifyToString(sentence.getSurface())
                        // Normalize result so we got something almost the same
                        // Than pos tagging in term of number of spaces/tokens
                        .replaceAll("([^ ]+/MONEY)([0-9]+)", "$1 $2")
                        .replaceAll("(['][a-zA-Z])/([A-Z]+)", " $1/$2")
                        .replaceAll("([^A-Za-z0-9]+)/([A-Z]+)", " $1/$2")
                        .replaceAll("-LRB -/O", "-LRB-/O ")
                        .replaceAll("-RRB -/O", " -RRB-/O")
                        .replaceAll(" ([A-Za-z0-9]+) ([^A-Za-z0-9])", " $1$2")
                        .replaceAll("/O[^R ]", "/O ")
                        .replaceAll(" - ", " -").trim());
            nerTokens = nerResult.split(" ");
        }

        // Validate NER/POS tagging: must have same length
        if (l != nerTokens.length) {
            LOGGER.error("Unable to use NER result: normalization failed:\n"
                        + "POS: " + raw + "\n"
                        + "NER: " + nerResult);

            nerTokens = null;
        }

        for (i = 0; i < l; i += 1) {
            String definition = tokens[i];
            int index = definition.lastIndexOf("_");

            String s = definition.substring(0, index);
            if (s.equals(".")) {
                continue;
            }

            // Initialize token
            Token t = new Token(s);
            String rawTag = definition.substring(index + 1);
            PennPOSTag tag = PennPOSTag.get(rawTag);
            if (tag != null) {
                t.setTag(tag);
            }



            // Parenthesis
            // -LRB-_-LRB- ... -RRB-_-RRB-
            if (s.equals("-LRB-") && rawTag.equals("-LRB-")) {
                t.setType(TokenType.OPEN_PARENTHESIS);
            } else if (s.equals("-RRB-") && rawTag.equals("-RRB-")) {
                t.setType(TokenType.CLOSE_PARENTHESIS);

            // Comma and operators, with exception for currency chars > word
            } else if (s.matches("[$€£¢¥₣₤₧]+")) {
                t.setType(TokenType.WORD);
            } else if (s.matches("^[^a-zA-Z0-9,]+$")) {
                t.setType(TokenType.COMMA);
            } else if (s.equals(",")) {
                t.setType(TokenType.COMMA);
            } else if (tag == PennPOSTag.U) {
                t.setType(TokenType.OPERATOR);

            // Numeric
            } else if (tag == PennPOSTag.CARDINAL_NUMBER) {
                t.setType(TokenType.NUMERIC);

            // Verbs
            } else if (
                tag == PennPOSTag.VERB
                || tag == PennPOSTag.VERB_MODAL
                || tag == PennPOSTag.VERB_PARTICIPLE_PAST
                || tag == PennPOSTag.VERB_PARTICIPLE_PRESENT
                || tag == PennPOSTag.VERB_PAST_TENSE
                || tag == PennPOSTag.VERB_SINGULAR_PRESENT_NONTHIRD_PERSON
                || tag == PennPOSTag.VERB_SINGULAR_PRESENT_THIRD_PERSON
            ) {
               t.setType(TokenType.VERB);

            // Nouns
            } else if (
                tag == PennPOSTag.NOUN
                || tag == PennPOSTag.NOUN_PLURAL
                || tag == PennPOSTag.NOUN_PROPER_PLURAL
                || tag == PennPOSTag.NOUN_PROPER_SINGULAR
            ) {
                /*
                    Special case for english,
                    we never consider "The" and "the" as keywords.
                 */
                if (t.getClean().equals("the")) {
                    t.setType(TokenType.OPERATOR);
                } else {
                    t.setType(TokenType.KEYWORD);
                }

            // Other are considers as operators
            } else if (
                tag == PennPOSTag.ADVERB
                || tag == PennPOSTag.ADVERB_COMPARATIVE
                || tag == PennPOSTag.ADVERB_SUPERLATIVE
                || tag == PennPOSTag.ADVERB_WH
                || tag == PennPOSTag.CONJUNCTION_COORDINATING
                || tag == PennPOSTag.CONJUNCTION_SUBORDINATING
                || tag == PennPOSTag.DETERMINER
                || tag == PennPOSTag.DETERMINER_WH
                || tag == PennPOSTag.EXISTENTIAL_THERE
                || tag == PennPOSTag.LIST_ITEM_MARKER
                || tag == PennPOSTag.PREDETERMINER
                || tag == PennPOSTag.POSSESSIVE_ENDING
                || tag == PennPOSTag.PARTICLE
                || tag == PennPOSTag.TO
                || tag == PennPOSTag.INTERJECTION
                || tag == PennPOSTag.PRONOUN_PERSONAL
                || tag == PennPOSTag.PRONOUN_POSSESSIVE
            ) {
                t.setType(TokenType.OPERATOR);
            }


            // If we have NER result, let's apply it now
            if (nerTokens != null && nerTokens[i].indexOf('/') > -1) {

                String[] ner = nerTokens[i].split("/");

                if (ner[1].equals("MONEY")) {
                    t.score(TokenType.MONEY_AMOUNT, 5);
                } else if (ner[1].equals("ORGANIZATION")) {
                    t.score(TokenType.ORGANIZATION, 5);
                } else if (ner[1].equals("PERSON")) {
                    t.score(TokenType.PERSON_PART, 5);
                } else if (ner[1].equals("LOCATION")) {
                    t.score(TokenType.LOCATION_PART, 5);
                } else if (ner[1].equals("DATE")) {
                    t.score(TokenType.DATE_PART, 5);
                } else if (ner[1].equals("TIME")) {
                    t.score(TokenType.TIME, 5);
                }
            }


            if (prev != null) {
                prev.setNext(t);
                t.setPrevious(prev);
            }

            sentence.appendToken(t);
            prev = t;
        }
    }

}
