package com.ulflander.mining.processors.augment.geoloc;

import com.google.gson.annotations.Expose;

/**
 * Result for MaxMind geolocation analysis on a token.
 *
 * @author Ulflander <xlaumonier@gmail.com>
 * @since 2/23/14
 */
public class MaxMindIPExtractionResult {

    /**
     * Creates a new result object.
     */
    public MaxMindIPExtractionResult() {

    }

    /**
     * String representation of this geolocation result.
     *
     * @return String representation of this geolocation result.
     */
    public final String toString() {
        return "MaxMindIPExtractionResult{city:" + city
                + ",country:" + country + "}";
    }


    /**
     * City.
     */
    @Expose
    private String city;
    /**
     * Country.
     */
    @Expose
    private String country;
    /**
     * Country ISO code.
     */
    @Expose
    private String countryIsoCode;
    /**
     * Latitude and longitude.
     */
    @Expose
    private String latLng;
    /**
     * Continent.
     */
    @Expose
    private String continent;
    /**
     * Continent code.
     */
    @Expose
    private String continentCode;
    /**
     * Timezone.
     */
    @Expose
    private String timezone;

    /**
     * Get city.
     *
     * @return Geolocation city.
     */
    public final String getCity() {
        return city;
    }

    /**
     * Set city.
     *
     * @param c City name
     */
    public final void setCity(final String c) {
        this.city = c;
    }

    /**
     * Get country.
     *
     * @return Geolocation country.
     */
    public final String getCountry() {
        return country;
    }

    /**
     * Set country.
     *
     * @param c Country name
     */
    public final void setCountry(final String c) {
        this.country = c;
    }

    /**
     * Get country ISO code.
     *
     * @return Geolocation country ISO code.
     */
    public final String getCountryIsoCode() {
        return countryIsoCode;
    }

    /**
     * Set country ISO code.
     *
     * @param c Country ISO code
     */
    public final void setCountryIsoCode(final String c) {
        this.countryIsoCode = c;
    }

    /**
     * Get lat/long.
     *
     * @return Geolocation latitude and longitude.
     */
    public final String getLatLng() {
        return latLng;
    }

    /**
     * Set lat/long.
     *
     * @param l Latitude and longitude saparated by a comma (,)
     */
    public final void setLatLng(final String l) {
        this.latLng = l;
    }

    /**
     * Get continent.
     *
     * @return Geolocation continent.
     */
    public final String getContinent() {
        return continent;
    }

    /**
     * Set continent name.
     *
     * @param c Continent name.
     */
    public final void setContinent(final String c) {
        this.continent = c;
    }

    /**
     * Get continent code.
     *
     * @return Geolocation continent code.
     */
    public final String getContinentCode() {
        return continentCode;
    }

    /**
     * Set continent code.
     *
     * @param c Continent code.
     */
    public final void setContinentCode(final String c) {
        this.continentCode = c;
    }

    /**
     * Get timezone.
     *
     * @return Geolocation timezone.
     */
    public final String getTimezone() {
        return timezone;
    }

    /**
     * Set timezone.
     *
     * @param t Timezone
     */
    public final void setTimezone(final String t) {
        this.timezone = t;
    }

}
