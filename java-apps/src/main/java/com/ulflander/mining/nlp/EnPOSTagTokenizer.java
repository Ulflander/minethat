package com.ulflander.mining.nlp;

import com.ulflander.app.Conf;
import com.ulflander.app.model.Sentence;
import com.ulflander.app.model.Token;
import com.ulflander.app.model.TokenType;
import com.ulflander.utils.UlfStringUtils;
import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;
import org.apache.commons.lang3.StringUtils;
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
     * Score to apply to token when an entity has been recognized by Stanford
     * NER classifier.
     */
    public static final int STANFORD_NER_SCORE = 5;


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
                classifier = CRFClassifier.getClassifier(Conf.getDataPath()
                    + "ner-classifiers/english.muc.7class.distsim.crf.ser.gz");
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

        // While tokenizing, let's run NER
        // and cleanup the result:
        String[] nerTokens = runNER(sentence);


        // Validate NER/POS tagging: must have same length
        if (l != nerTokens.length) {
            LOGGER.error("Unable to use NER result: normalization failed:\n"
                        + "POS: " + raw + "\n"
                        + "NER: " + StringUtils.join(nerTokens, " "));

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
                applyNER(nerTokens[i], t);
            }

            if (prev != null) {
                prev.setNext(t);
                t.setPrevious(prev);
            }

            sentence.appendToken(t);
            prev = t;
        }
    }

    /**
     * Run Stanford NER classifier, also normalize the result so we have
     * the same token indexing compared with POSTagger.
     *
     * @param s Sentence
     * @return Array of tokens annotated with NER classifier result
     */
    private String[] runNER(final Sentence s) {
        String nerResult = null;
        if (classifier != null) {
            nerResult = UlfStringUtils.cleanSpaces(
                    classifier.classifyToString(s.getSurface())
                        // Normalize result so we got something almost the same
                        // Than pos tagging in term of number of spaces/tokens
                        .replaceAll("([^ ]+/MONEY)([0-9]+)", "$1 $2 ")
                        .replaceAll("(['][a-zA-Z])/([A-Z]+)", " $1/$2 ")
                        .replaceAll("([^A-Za-z0-9]+)/([A-Z]+)", " $1/$2 ")
                        .replaceAll("-LRB -/O", "-LRB-/O ")
                        .replaceAll("-RRB -/O", " -RRB-/O")
                        .replaceAll(" ([A-Za-z0-9]+) ([^A-Za-z0-9])", " $1$2")
                        .replaceAll("/O[^ ]", "/O ")
                        .replaceAll("/O GANIZ", "/ORGANIZ")
                        .replaceAll(" /O", "/O")
                        .replaceAll(" /MONEY", "/MONEY")
                        .replaceAll("\\s+", " ")
                        .replaceAll(" - ", " -").trim());
            return nerResult.split(" ");
        }

        return null;
    }

    /**
     * Score token given the NER result.
     *
     * @param nerToken Token annotated using Stanford NER classifier
     * @param t Token to apply score to
     */
    private void applyNER(final String nerToken, final Token t) {

        String[] ner = nerToken.split("/");

        if (ner[1].equals("MONEY")) {
            t.score(TokenType.MONEY_AMOUNT, STANFORD_NER_SCORE);
        } else if (ner[1].equals("ORGANIZATION")) {
            t.score(TokenType.ORGANIZATION, STANFORD_NER_SCORE);
        } else if (ner[1].equals("PERSON")) {
            t.score(TokenType.PERSON_PART, STANFORD_NER_SCORE);
        } else if (ner[1].equals("LOCATION")) {
            t.score(TokenType.LOCATION_PART, STANFORD_NER_SCORE);
        } else if (ner[1].equals("DATE")) {
            t.score(TokenType.DATE_PART, STANFORD_NER_SCORE);
        } else if (ner[1].equals("TIME")) {
            t.score(TokenType.TIME, STANFORD_NER_SCORE);
        }
    }

}
