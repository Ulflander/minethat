package com.ulflander.mining.nlp;

/**
 * Part-of-speech identifiers (using Penn Treebank standard).
 *
 * Code from Dave Jarvis, Victoria, Canada.
 *
 * Taken at <a href="http://stackoverflow.com/questions/1833252">Stack
 * Overflow</a>.
 * <p/>
 * Represents the English parts-of-speech, encoded using the
 * de facto <a href="http://www.cis.upenn.edu/~treebank/">Penn Treebank
 * Project</a> standard.
 *
 * @see <a href="ftp://ftp.cis.upenn.edu/pub/treebank/doc/tagguide.ps.gz">
 *     Penn Treebank Specification</a>
 *
 * @author Dave Jarvis
 * @since 2/23/14
 */
public enum PennPOSTag {

    /**
     * Unknown token.
     */
    U("*"),
    /**
     * Simple adjective.
     */
    ADJECTIVE("JJ"),
    /**
     * Comparative adjective.
     */
    ADJECTIVE_COMPARATIVE(ADJECTIVE + "R"),
    /**
     * Superlative adjective.
     */
    ADJECTIVE_SUPERLATIVE(ADJECTIVE + "S"),

    /**
     * Adverb.
     *
     * This category includes most words that end in -ly as well as degree
     * words like quite, too and very, posthead modi ers like enough and
     * indeed (as in good enough, very well indeed), and negative markers like
     * not, n't and never.
     */
    ADVERB("RB"),

    /**
     * Comparative adverb.
     *
     * Adverbs with the comparative ending -er but without a strictly
     * comparative meaning, like <i>later</i> in <i>We can always come by
     * later</i>, should simply be tagged as RB.
     */
    ADVERB_COMPARATIVE(ADVERB + "R"),
    /**
     * Superlative adverb.
     */
    ADVERB_SUPERLATIVE(ADVERB + "S"),

    /**
     * Contextual adverbs.
     *
     * This category includes how, where, why, etc.
     */
    ADVERB_WH("W" + ADVERB),

    /**
     * Conjunction coordinating.
     *
     * This category includes and, but, nor, or, yet (as in Y et it's cheap,
     * cheap yet good), as well as the mathematical operators plus, minus, less,
     * times (in the sense of "multiplied by") and over (in the sense of
     * "divided by"), when they are spelled out. <i>For</i> in the sense of
     * "because" is a coordinating conjunction (CC) rather than a
     * subordinating conjunction.
     */
    CONJUNCTION_COORDINATING("CC"),
    /**
     * Conjunction subordinating.
     */
    CONJUNCTION_SUBORDINATING("IN"),
    /**
     * Cardinal number.
     */
    CARDINAL_NUMBER("CD"),
    /**
     * Determiner.
     */
    DETERMINER("DT"),

    /**
     * Contextual determiner.
     *
     * This category includes which, as well as that when it is used as a
     * relative pronoun.
     */
    DETERMINER_WH("W" + DETERMINER),
    /**
     * Existencial there.
     */
    EXISTENTIAL_THERE("EX"),
    /**
     * Foreign word.
     */
    FOREIGN_WORD("FW"),

    /**
     * List item marker.
     */
    LIST_ITEM_MARKER("LS"),

    /**
     * Noun.
     */
    NOUN("NN"),
    /**
     * Plural noun.
     */
    NOUN_PLURAL(NOUN + "S"),
    /**
     * Proper singular noun.
     */
    NOUN_PROPER_SINGULAR(NOUN + "P"),
    /**
     * Proper plural noun.
     */
    NOUN_PROPER_PLURAL(NOUN + "PS"),
    /**
     * Predeterminer.
     */
    PREDETERMINER("PDT"),
    /**
     * Possessive ending.
     */
    POSSESSIVE_ENDING("POS"),
    /**
     * Personal pronoun.
     */
    PRONOUN_PERSONAL("PRP"),
    /**
     * Possessive pronoun.
     */
    PRONOUN_POSSESSIVE("PRP$"),

    /**
     * Contextual possessive pronoun.
     *
     * This category includes the wh-word whose.
     */
    PRONOUN_POSSESSIVE_WH("WP$"),

    /**
     * Contextual pronoun.
     *
     * This category includes what, who and whom.
     */
    PRONOUN_WH("WP"),

    /**
     * Particle.
     */
    PARTICLE("RP"),

    /**
     * Symbol.
     *
     * This tag should be used for mathematical, scientific and technical
     * symbols or expressions that aren't English words. It should not used
     * for any and all technical expressions. For instance, the names of
     * chemicals, units of measurements (including abbreviations thereof)
     * and the like should be tagged as nouns.
     */
    SYMBOL("SYM"),
    /**
     * To.
     */
    TO("TO"),

    /**
     * Interjection.
     *
     * This category includes my (as in M y, what a gorgeous day), oh, please,
     * see (as in See, it's like this), uh, well and yes, among others.
     */
    INTERJECTION("UH"),

    /**
     * Verb.
     */
    VERB("VB"),
    /**
     * Past tense verb.
     */
    VERB_PAST_TENSE(VERB + "D"),
    /**
     * Participle present verb.
     */
    VERB_PARTICIPLE_PRESENT(VERB + "G"),
    /**
     * Participle past verb.
     */
    VERB_PARTICIPLE_PAST(VERB + "N"),
    /**
     * Non-third person singular present verb.
     */
    VERB_SINGULAR_PRESENT_NONTHIRD_PERSON(VERB + "P"),
    /**
     * Third person singular present verb.
     */
    VERB_SINGULAR_PRESENT_THIRD_PERSON(VERB + "Z"),

    /**
     * Modal verb.
     *
     * This category includes all verbs that don't take an -s ending in the
     * third person singular present: can, could, (dare), may, might, must,
     * ought, shall, should, will, would.
     */
    VERB_MODAL("MD"),

    /**
     * Sentence terminator.
     *
     * Stanford.
     */
    SENTENCE_TERMINATOR(".");

    /**
     * Tag.
     */
    private final String tag;

    /**
     * Initialize part-of-speech tag.
     *
     * @param t Tag.
     */
    private PennPOSTag(final String t) {
        this.tag = t;
    }

    /**
     * Returns the encoding for this part-of-speech.
     *
     * @return A string representing a Penn Treebank encoding for an English
     * part-of-speech.
     */
    public String toString() {
        return getTag();
    }

    /**
     * Get tag.
     *
     * @return Tag as string.
     */
    protected String getTag() {
        return this.tag;
    }

    /**
     * Get PennPOSTag value given its string representation.
     *
     * @param value String representation of a part-of-speech (".", "MD"...)
     * @return PennPOSTag representation
     */
    public static PennPOSTag get(final String value) {
        if (value == null) {
            return PennPOSTag.U;
        }

        String val = value.toUpperCase();

        for (PennPOSTag v : values()) {
            if (val.equals(v.getTag())) {
                return v;
            }
        }

        throw new IllegalArgumentException("Unknown part of speech: '"
                                            + value + "'.");
    }
}
