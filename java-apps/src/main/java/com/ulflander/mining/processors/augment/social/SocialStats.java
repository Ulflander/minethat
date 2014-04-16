package com.ulflander.mining.processors.augment.social;

import com.google.gson.Gson;
import com.ulflander.application.Conf;
import com.ulflander.application.model.Document;
import com.ulflander.application.utils.UlfNetworkUtils;
import com.ulflander.mining.processors.Processor;
import com.ulflander.mining.processors.ProcessorDepthControl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

/**
 * Extract social stats.
 *
 * Created by Ulflander on 4/12/14.
 */
public class SocialStats extends Processor {

    /**
     * Logger.
     */
    private static final Logger LOGGER =
        LogManager.getLogger(SocialStats.class);

    /**
     * Blacklisted domains (local hosts mainly).
     */
    private static final List<String> BLACKLISTED_DOMAINS =
            Arrays.asList("localhost", "127.0.0.1");

    @Override
    public final void init() {
        setDepthControl(ProcessorDepthControl.DOCUMENT);
        setInitialized(true);
    }

    @Override
    public final String describe() {
        return "Retrieve social sharing statistics from sharedcount.com";
    }

    @Override
    public final void extractDocument(final Document doc) {

        if (!doc.hasProperty("meta", "url")) {
            LOGGER.debug("Document has no URL: " + doc.getId());
            return;
        }

        String url = (String) doc.getProperty("meta", "url");
        URL urlObj;

        // Validate URL
        try {
            urlObj = new URL(url);
        } catch (MalformedURLException e) {
            LOGGER.warn("URL " + url + " is malformed, doc" + doc.getId(), e);
            return;
        }

        // Skip blacklisted domains
        if (BLACKLISTED_DOMAINS.contains(urlObj.getHost())) {
            LOGGER.debug("SocialCount skipped, domain " + urlObj.getHost()
                    + " is blacklisted, doc " + doc.getId());
            return;
        }

        String serviceURL;

        // Create service URL
        try {
            serviceURL = "http://api.sharedcount.com/?"
                    + "apikey=" + Conf.getSharedCountKey() + "&"
                    + "url=" + URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            LOGGER.warn("Unable to encode url " + url
                        + ", doc" + doc.getId(), e);
            return;
        }

        // Call service
        String serviceResponse = UlfNetworkUtils.getContent(serviceURL);

        // Read service response
        Gson gson = new Gson();
        SharedCountResult result;
        try {
            result = gson.fromJson(serviceResponse,
                    SharedCountResult.class);
        } catch (Exception e) {
            LOGGER.warn("Unable to decode API result " + serviceResponse
                        + ", doc" + doc.getId(), e);
            return;
        }

        if (result == null) {
            LOGGER.warn("Unable to decode API result " + serviceResponse
                    + ", doc" + doc.getId());
            return;
        }


        doc.addProperty("social_stats", "reddit", result.getReddit());
        doc.addProperty("social_stats", "twitter", result.getTwitter());
        doc.addProperty("social_stats", "linkedin", result.getLinkedIn());
        doc.addProperty("social_stats", "pinterest", result.getPinterest());

        doc.addProperty("social_stats", "googleplus",
                result.getGooglePlusOne());

        doc.addProperty("social_stats", "stumbleupon",
                result.getStumbleUpon());

        doc.addProperty("social_stats", "fb_likes",
                result.getFacebook().getLikeCount());

        doc.addProperty("social_stats", "fb_comments",
                result.getFacebook().getCommentCount()
                + result.getFacebook().getCommentsBoxCount());

        doc.addProperty("social_stats", "fb_share",
                result.getFacebook().getShareCount());

        doc.addProperty("social_stats", "fb_click",
                result.getFacebook().getClickCount());

        doc.addProperty("social_stats", "fb_total",
                result.getFacebook().getTotalCount());
    }
}
