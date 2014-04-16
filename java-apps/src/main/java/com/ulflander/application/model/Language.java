package com.ulflander.application.model;

import org.apache.commons.lang3.ArrayUtils;

/**
 * Represents a language in Minethat model.
 */
public enum Language {

    /**
     * Text is multilingual.
     */
    MULTILINGUAL("m"),

    /**
     * Language is unknown.
     */
    UNKNOWN("u"),

    /**
     * English language.
     */
    EN("en"),

    /**
     * French language.
     */
    FR("fr"),

    /**
     * Arabian language.
     */
    AR("ar"),

    /**
     * Chinese language.
     */
    ZH("zh"),

    /**
     * German language.
     */
    DE("de"),

    /**
     * Spanish language.
     */
    ES("es"),

    /**
     * Italian language.
     */
    IT("it");

    /**
     * All supported languages.
     */
    static final String[] SUPPORT = new String[]{
        "en",
        "fr",
        "ar",
        "zh",
        "de",
        "es",
        "it"
    };

    /**
     * Private constructor.
     *
     * @param t Initialized value
     */
    private Language(final String t) {
        this.text = t;
    }

    /**
     * Initial value.
     */
    private final String text;

    /* (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return text;
    }

    /**
     * Language by its locale.
     *
     * @param locale Locale representing the language
     * @return Language if found, unknown otherwise
     */
    public static Language get(final String locale) {
        if (!isSupported(locale)) {
            return Language.UNKNOWN;
        }
        return Language.valueOf(locale.toUpperCase());
    }

    /**
     * Check if language is supported.
     *
     * @param locale Locale representing the language
     * @return True if language is somehow supported, false otherwise
     */
    public static Boolean isSupported(final String locale) {
        return ArrayUtils.contains(SUPPORT, locale);
    }
}
