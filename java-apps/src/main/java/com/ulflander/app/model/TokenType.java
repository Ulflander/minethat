package com.ulflander.app.model;

/**
 * Represents the type of a token.
 *
 * @author Ulflander <xlaumonier@gmail.com>
 * @since 2/23/14
 */
public enum TokenType {

    /**
     * Any basic word, should have a PennPOSTag representation if
     * language is supported.
     */
    WORD,

    /**
     * A conjunction, preposition...
     */
    OPERATOR,

    /**
     * A keyword: an important word.
     */
    KEYWORD,

    /**
     *  A comma ",".
     */
    COMMA,

    /**
     * A verb.
     */
    VERB,

    /**
     * A number (1, 3.141592).
     */
    NUMERIC,

    /**
     * An email address.
     */
    EMAIL,

    /**
     * An IPV4 address.
     */
    IPV4,

    /**
     * A money amount ("$15", "50.30€"...).
     */
    MONEY_AMOUNT,

    /**
     * An URI.
     */
    URI,

    /**
     * A phone number.
     */
    PHONE_NUMBER,

    /**
     * A twitter username (@somebody).
     */
    TWITTER_USERNAME,

    /**
     * A hashtag (#ThisIsAHashTag).
     */
    HASHTAG,

    /**
     * A date descriptor.
     */
    DATE_DESCRIPTOR,

    /**
     * A date (30/06/1982, 12-30-2014, November 1974...).
     */
    DATE_PART,

    /**
     * A date (30/06/1982, 12-30-2014, November 1974...).
     */
    DATE,

    /**
     * A time (13h37, 12pm...).
     */
    TIME,

    /**
     * A datetime (12/30/2014 13:37:00...).
     */
    DATETIME,

    /**
     * A location descriptor.
     */
    LOCATION_DESCRIPTOR,

    /**
     * A location.
     */
    LOCATION,

    /**
     * A location part.
     */
    LOCATION_PART,

    /**
     * A nationality - important to keep so we can refine geotagging.
     */
    NATIONALITY,

    /**
     * An organization descriptor.
     */
    ORGANIZATION_DESCRIPTOR,


    /**
     * An organization.
     */
    ORGANIZATION,

    /**
     * A person descriptor.
     */
    PERSON_DESCRIPTOR,

    /**
     * A person.
     */
    PERSON,

    /**
     * A person.
     */
    PERSON_PART,

    /**
     * An acronym.
     */
    ACRONYM,

    /**
     * A host name (google.com, app.co).
     */
    HOST,

    /**
     * A file path (C://my/file.pdf, /usr/home/stuff.gif...).
     */
    FILE_PATH,

    /**
     * A social security number.
     */
    SSN,

    /**
     * A version number (v1.1, 1.5.263541, ...).
     */
    VERSION_NUMBER,

    /**
     * A credit card number.
     */
    CREDIT_CARD_NUMBER,

    /**
     * Open parenthesis "(".
     */
    OPEN_PARENTHESIS,

    /**
     * Close parenthesis ")".
     */
    CLOSE_PARENTHESIS,

    /**
     * Open bracket "[".
     */
    OPEN_BRACKET,

    /**
     * Close bracket "]".
     */
    CLOSE_BRACKET,

    /**
     * Open brace "{".
     */
    OPEN_BRACE,

    /**
     * Close brace "}".
     */
    CLOSE_BRACE
}
