package com.ulflander.mining.processors.augment.geoloc;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.AddressNotFoundException;
import com.maxmind.geoip2.model.CityResponse;
import com.ulflander.application.model.Token;
import com.ulflander.application.model.TokenType;
import com.ulflander.application.utils.UlfResourceUtils;
import com.ulflander.mining.processors.Processor;
import com.ulflander.mining.processors.Requires;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetAddress;

/**
 * Processor that augment IPV4 tokens with a geolocation (Using MaxMind GeoIP).
 *
 * @author Ulflander <xlaumonier@gmail.com>
 * @since 2/21/14
 */
@Requires(processors = {
    "extract.TokenRegExpGuesser"
})
public class MaxMindIPExtraction extends Processor {

    /**
     * Logger.
     */
    private static final Logger LOGGER =
        LogManager.getLogger(MaxMindIPExtraction.class);

    /**
     * Reader of MaxMIn GeoIP database.
     */
    private DatabaseReader reader;

    /**
     * Initialize the processor (load GeoLite-City database).
     */
    @Override
    public final void init() {
        String rs = "com/ulflander/whm/GeoLite2-City.mmdb";
        try {
            reader = new DatabaseReader.Builder(
                UlfResourceUtils.getStream(rs)
            ).build();
            setInitialized(true);
        } catch (Exception e) {
            LOGGER.error("MaxMind free IP geoloc unable to start", e);
        }

    }

    /**
     * Provides a description of this Processor.
     *
     * @return Description of this processor
     */
    @Override
    public final String describe() {
        return "Extract geolocation of IPV4 tokens using MaxMind free GeoLite "
            + "database - See http://dev.maxmind.com/geoip/geoip2/geolite2/";
    }

    /**
     * If token is an IP, try to geolocate it.
     *
     * @param token Token to run processor on
     */
    @Override
    public final void extractToken(final Token token) {
        if (token.getType() == TokenType.IPV4) {
            try {
                CityResponse response =
                    reader.city(InetAddress.getByName(token.getRaw()));
                MaxMindIPExtractionResult res = new MaxMindIPExtractionResult();
                if (response.getCity() != null) {
                    res.setCity(response.getCity().getName());
                }
                if (response.getCountry() != null) {
                    res.setCountry(response.getCountry().getName());
                    res.setCountryIsoCode(response.getCountry().getIsoCode());
                }
                if (response.getContinent() != null) {
                    res.setContinent(response.getContinent().getName());
                    res.setContinentCode(response.getContinent().getCode());
                }
                if (response.getLocation() != null) {
                    res.setLatLng(response.getLocation().getLatitude() + ","
                        + response.getLocation().getLongitude());
                    res.setTimezone(response.getLocation().getTimeZone());
                }
                //token.getResults().add(res);
            } catch (AddressNotFoundException e) {
                LOGGER.warn("Address not found", e);
            } catch (Exception e1) {
                LOGGER.error("Unable to get reader return city", e1);
            }
        }
    }
}
