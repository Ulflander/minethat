package com.ulflander.application.model;

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
     * A date (30/06/1982, 12-30-2014, November 1974...).
     */
    DATE,

    /**
     * A date (30/06/1982, 12-30-2014, November 1974...).
     */
    DATE_PART,

    /**
     * A time (13h37, 12pm...).
     */
    TIME,

    /**
     * A datetime (12/30/2014 13:37:00...).
     */
    DATETIME,

    /**
     * A location.
     */
    LOCATION,

    /**
     * A location part.
     */
    LOCATION_PART,


    /**
     * An organization.
     */
    ORGANIZATION,

    /**
     * A person.
     */
    PERSON,

    /**
     * A person.
     */
    PERSON_PART,

    /**
     * A host name (google.com, application.co).
     */
    HOST,

    /**
     * A file path (C://my/file.pdf, /usr/home/stuff.gif...).
     */
    FILEPATH,

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
    CREDIT_CARD_NUMBER
}
