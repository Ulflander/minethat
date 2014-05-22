package com.ulflander.app.model;

/**
 * Created by Ulflander on 5/17/14.
 */
public enum EntityType {

    /**
     * Acronym definition.
     */
    ACRONYM_DEFINITION,

    /**
     * A company.
     */
    COMPANY,

    /**
     * A governmental organization.
     */
    GOV_ORGANIZATION,

    /**
     * A group of people.
     */
    GROUP,

    /**
     * A person.
     */
    PERSON,

    /**
     * A country.
     */
    COUNTRY,

    /**
     * An administrative area.
     */
    ADMINISTRATIVE_AREA,

    /**
     * A city.
     */
    CITY,

    /**
     * An address.
     */
    ADDRESS,


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
    DATETIME;
}
