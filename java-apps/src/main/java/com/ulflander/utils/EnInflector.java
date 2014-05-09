
package com.ulflander.utils;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Inflector for english language.
 *
 * @author Ulflander <xlaumonier@gmail.com>
 * @since 2/21/14
 */
public final class EnInflector {

    /**
     * Private constructor.
     */
    private EnInflector() {

    }

    /**
     * Utility class to store rules and replacements.
     */
    private static final class RuleAndReplacement {

        /**
         * The rule.
         */
        private final String rule;

        /**
         * The replacement.
         */
        private final String replacement;

        /**
         * RuleAndReplacement constructor.
         *
         * @param rul     The rule
         * @param replace The replacement
         */
        public RuleAndReplacement(final String rul, final String replace) {
            this.rule = rul;
            this.replacement = replace;
        }

        /**
         * Get the replacement.
         *
         * @return Replacement string
         */
        public String getReplacement() {
            return replacement;
        }

        /**
         * Get the rule.
         *
         * @return Rule string
         */
        public String getRule() {
            return rule;
        }
    }

    /**
     * Utility pattern.
     */
    private static final Pattern UNDERSCORE_PATTERN_1 =
        Pattern.compile("([A-Z]+)([A-Z][a-z])");

    /**
     * Utility pattern.
     */
    private static final Pattern UNDERSCORE_PATTERN_2 =
        Pattern.compile("([a-z\\d])([A-Z])");

    /**
     * List of rules and replacements for plurals.
     */
    private static List<RuleAndReplacement> plurals =
        new ArrayList<RuleAndReplacement>();

    /**
     * List of rules and replacements for singulars.
     */
    private static List<RuleAndReplacement> singulars =
        new ArrayList<RuleAndReplacement>();

    /**
     * List of uncountable strings.
     */
    private static List<String> uncountables =
        new ArrayList<String>();

    static {
        plural("$", "s");
        plural("s$", "s");
        plural("(ax|test)is$", "$1es");
        plural("(octop|vir)us$", "$1i");
        plural("(alias|status)$", "$1es");
        plural("(bu)s$", "$1es");
        plural("(buffal|tomat)o$", "$1oes");
        plural("([ti])um$", "$1a");
        plural("sis$", "ses");
        plural("(?:([^f])fe|([lr])f)$", "$1$2ves");
        plural("(hive)$", "$1s");
        plural("([^aeiouy]|qu)y$", "$1ies");
        plural("([^aeiouy]|qu)ies$", "$1y");
        plural("(x|ch|ss|sh)$", "$1es");
        plural("(matr|vert|ind)ix|ex$", "$1ices");
        plural("([m|l])ouse$", "$1ice");
        plural("(ox)$", "$1en");
        plural("(quiz)$", "$1zes");
        singular("s$", "");
        singular("(n)ews$", "$1ews");
        singular("([ti])a$", "$1um");
        singular(
            "((a)naly|(b)a|(d)iagno|(p)arenthe|(p)rogno|(s)ynop|(t)he)ses$",
            "$1$2sis");
        singular("(^analy)ses$", "$1sis");
        singular("([^f])ves$", "$1fe");
        singular("(hive)s$", "$1");
        singular("(tive)s$", "$1");
        singular("([lr])ves$", "$1f");
        singular("([^aeiouy]|qu)ies$", "$1y");
        singular("(s)eries$", "$1eries");
        singular("(m)ovies$", "$1ovie");
        singular("(x|ch|ss|sh)es$", "$1");
        singular("([m|l])ice$", "$1ouse");
        singular("(bus)es$", "$1");
        singular("(o)es$", "$1");
        singular("(shoe)s$", "$1");
        singular("(cris|ax|test)es$", "$1is");
        singular("([octop|vir])i$", "$1us");
        singular("(alias|status)es$", "$1");
        singular("^(ox)en", "$1");
        singular("(vert|ind)ices$", "$1ex");
        singular("(matr)ices$", "$1ix");
        singular("(quiz)zes$", "$1");
        irregular("person", "people");
        irregular("man", "men");
        irregular("child", "children");
        irregular("sex", "sexes");
        irregular("move", "moves");
        uncountable("equipment", "information", "rice", "money",
            "species", "series", "fish", "sheep");
    }

    /**
     * Return camelCase version of a string.
     *
     * @param name Underscored string
     * @return Camel cased string.
     */
    public static String camelCase(final String name) {
        final StringBuilder builder = new StringBuilder();
        for (final String part : name.split("_")) {
            builder.append(Character.toTitleCase(part.charAt(0)))
                    .append(part.substring(1));
        }
        return builder.toString();
    }

    /**
     * Return capitalized version of a string.
     *
     * @param name Underscored string
     * @return Camel cased string.
     */
    public static String capitalize(final String name) {
        final StringBuilder builder = new StringBuilder();
        for (final String part : name.split(" ")) {
            builder.append(Character.toUpperCase(part.charAt(0)))
                    .append(part.substring(1).toLowerCase() + " ");
        }
        return builder.toString().trim();
    }

    /**
     * Store a new irregular rule.
     *
     * @param singular Singular string
     * @param plural   Plural string
     */
    public static void irregular(final String singular, final String plural) {
        plural(singular + "$", plural);
        singular(plural + "$", singular);
    }

    /**
     * Store a plural rule and replacement.
     *
     * @param rule        The rule
     * @param replacement The replacement
     */
    public static void plural(final String rule, final String replacement) {
        plurals.add(0, new RuleAndReplacement(rule, replacement));
    }

    /**
     * Store a singular rule and replacement.
     *
     * @param rule        The rule
     * @param replacement The replacement
     */
    public static void singular(final String rule, final String replacement) {
        singulars.add(0, new RuleAndReplacement(rule, replacement));
    }

    /**
     * Store an uncountable word.
     *
     * @param words One or more uncountable word
     */
    public static void uncountable(final String... words) {
        uncountables.addAll(Arrays.asList(words));
    }

    /**
     * Return the pluralized version of a singular word.
     *
     * @param word Singular word
     * @return Plural word
     */
    public static String pluralize(final String word) {
        if (uncountables.contains(word.toLowerCase())) {
            return word;
        }
        return replaceWithFirstRule(word, plurals);
    }

    /**
     * Replace a word with first rule found.
     *
     * @param word                Word to replace
     * @param ruleAndReplacements Rules to follow
     * @return Replacement
     */
    private static String
    replaceWithFirstRule(final String word, final List<RuleAndReplacement>
        ruleAndReplacements) {

        for (final RuleAndReplacement rar : ruleAndReplacements) {
            final String rule = rar.getRule();
            final String replacement = rar.getReplacement();
            final Matcher matcher =
                Pattern.compile(rule, Pattern.CASE_INSENSITIVE)
                    .matcher(word);
            if (matcher.find()) {
                return matcher.replaceAll(replacement);
            }
        }
        return word;
    }

    /**
     * Return singular version of a plural word.
     *
     * @param word Word to singularize
     * @return Plural
     */
    public static String singularize(final String word) {
        if (uncountables.contains(word.toLowerCase())) {
            return word;
        }

        return replaceWithFirstRule(word, singulars);
    }

    /**
     * Underscores a camel cased word.
     *
     * @param camelCasedWord Camel cased word
     * @return Underscored word
     */
    public static String underscore(final String camelCasedWord) {
        String underscoredWord = UNDERSCORE_PATTERN_1.matcher(camelCasedWord)
            .replaceAll("$1_$2");
        underscoredWord = UNDERSCORE_PATTERN_2.matcher(underscoredWord)
            .replaceAll("$1_$2");
        underscoredWord = underscoredWord.replace('-', '_')
            .toLowerCase();
        return underscoredWord;
    }
}
