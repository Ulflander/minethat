package com.ulflander.mining;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;
import com.ulflander.app.model.Document;
import com.ulflander.utils.EnInflector;
import com.ulflander.utils.UlfStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Extract meta data from HTML page string using Jsoup and some CSS selectors.
 *
 * Created by Ulflander on 4/24/14.
 */
public final class MetaExtractor {

    /**
     * Private constructor.
     */
    private MetaExtractor() {

    }

    /**
     * Logger.
     */
    private static final Logger LOGGER =
        LogManager.getLogger(MetaExtractor.class);

    /**
     * Is inited or not.
     */
    private static boolean inited = false;

    /**
     * List of tags.
     */
    private static HashMap<String, List<Tag>> meta
            = new HashMap<String, List<Tag>>();


    /**
     * Normalize some various fields: author, dates...
     *
     * @param key Key (doc_author, doc_published_date...)
     * @param value Value to normalize
     * @return Normalized value
     */
    private static Object normalize(final String key,
                                    final String value) {
        if (value == null) {
            return null;
        }

        String res = value.replaceAll("\\\\\"", "\"");

        // For authors, we replace some tokens by a good separator
        // and we camelize words if they are mostly uppercased
        if (key.equals("doc_author")) {
            res = res.replaceAll(";", ", ")
                    .replaceAll(" and ", ", ")
                    .replaceAll("\\s+", " ");

            if (UlfStringUtils.isMostlyUppercase(res)) {
                res = EnInflector.capitalize(res);
            }

        // For dates, we convert them to timestamps
        // using natty
        } else if (key.contains("_date")) {
            Parser parser = new Parser();
            List<DateGroup> groups = parser.parse(value);

            if (groups.size() > 0 && groups.get(0).getDates().size() > 0) {
                return groups.get(0).getDates().get(0).getTime();
            } else {
                LOGGER.warn("Unable to parse date '" + value + "'");
                return null;
            }

        }

        return res.trim();
    }

    /**
     * Extract some meta data from html and populate document properties.
     *
     * @param doc Document
     * @param html HTML source
     */
    public static void extract(final Document doc,
                               final String html) {
        if (!inited) {
            init();
        }

        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(html);

        // For each meta information
        for (String key: meta.keySet()) {
            List<Tag> ts = meta.get(key);

            // Loop over possible tags
            for (Tag t: ts) {
                Elements es = jsoupDoc.select(t.getSelector());
                if (es.size() == 0) {
                    continue;
                }

                // If at least one element found
                // get meta data given type and
                // break this tag loop for this meta
                Element e = es.get(0);
                Object val = null;

                if (t.getType().equals("content")) {
                    val = normalize(key, e.text());

                } else if (t.getType().equals("attr")) {
                    val = normalize(key, e.attr(t.getAttr()));
                }

                if (val != null && !val.equals("Null") && !val.equals("")) {
                    if (doc.hasProperty("meta", key)) {
                        doc.addProperty("meta", key + "_official", val);
                    } else {
                        doc.addProperty("meta", key, val);
                    }
                    break;
                }
            }
        }
    }


    /**
     * Initializes lists.
     */
    private static void init() {
        inited = true;

        meta.put("doc_title", Arrays.asList(
                new Tag("meta[itemprop=headline]", "attr", "content"),
                new Tag("meta[property=og:title]", "attr", "content"),
                new Tag("meta[property=twitter:title]", "attr", "content"),
                new Tag("meta[meta=twitter:title]", "attr", "content"),
                new Tag("title", "content")
        ));


        meta.put("doc_author", Arrays.asList(
                new Tag("meta[name=author]", "attr", "content"),
                new Tag("meta[itemprop=author]", "attr", "content")
        ));


        meta.put("doc_url", Arrays.asList(
                new Tag("html[itemid]", "attr", "itemid"),
                new Tag("link[rel=canonical]", "attr", "href"),
                new Tag("meta[itemprop=url]", "attr", "content"),
                new Tag("meta[property=vr:canonical]", "attr", "content"),
                new Tag("meta[property=og:url]", "attr", "content"),
                new Tag("meta[name=twitter:url]", "attr", "content"),
                new Tag("meta[property=twitter:url]", "attr", "content"),
                new Tag("meta[name=twitter:url]", "attr", "value")
        ));


        meta.put("doc_type", Arrays.asList(
                new Tag("meta[property=og:type]", "attr", "content")
        ));


        meta.put("doc_description", Arrays.asList(
                new Tag("meta[name=description]", "attr", "content"),
                new Tag("meta[itemprop=description]", "attr", "content"),
                new Tag("meta[name=lp]", "attr", "content"),
                new Tag("meta[property=og:description]", "attr", "content"),
                new Tag("meta[name=twitter:description]", "attr", "content"),
                new Tag("meta[property=twitter:description]",
                        "attr", "content"),
                new Tag("meta[name=twitter:description]", "attr", "value")
        ));


        meta.put("doc_image", Arrays.asList(
                new Tag("meta[property=og:image]", "attr", "content"),
                new Tag("meta[name=twitter:image]", "attr", "content"),
                new Tag("meta[property=twitter:image]", "attr", "content"),
                new Tag("meta[name=twitter:image]", "attr", "value"),
                new Tag("meta[itemprop=thumbnailUrl]", "attr", "content")
        ));


        meta.put("doc_twitter_username", Arrays.asList(
                new Tag("meta[name=twitter:site]", "attr", "value"),
                new Tag("meta[property=twitter:site]", "attr", "content"),
                new Tag("meta[name=twitter:site]", "attr", "content")
        ));


        meta.put("doc_keywords", Arrays.asList(
                new Tag("meta[name=keywords]", "attr", "content"),
                new Tag("meta[itemprop=keywords]", "attr", "content"),
                new Tag("meta[name=news_keywords]", "attr", "content")
        ));


        meta.put("doc_publisher_name", Arrays.asList(
                new Tag("meta[name=cre]", "attr", "content"),
                new Tag("meta[itemprop=sourceOrganization]", "attr", "content"),
                new Tag("meta[property=og:site_name]", "attr", "content"),
                new Tag("meta[name=DC.publisher]", "attr", "content"),
                new Tag("meta[name=source]", "attr", "content"),
                new Tag("meta[name=app-name]", "attr", "content")
        ));



        meta.put("doc_classes", Arrays.asList(
                new Tag("meta[name=des]", "attr", "content")
        ));


        meta.put("doc_main_location", Arrays.asList(
                new Tag("meta[itemprop=contentLocation]", "attr", "content")
        ));


        meta.put("doc_published_date", Arrays.asList(
                new Tag("meta[name=pubdate]", "attr", "content"),
                new Tag("meta[itemprop=datePublished]", "attr", "content")
        ));


        meta.put("doc_edited_date", Arrays.asList(
                new Tag("meta[name=lastmod]", "attr", "content"),
                new Tag("meta[itemprop=datePublished]", "attr", "content"),
                new Tag("meta[http-equiv=lastmodified]", "attr", "content")
        ));


        meta.put("doc_created_date", Arrays.asList(
                new Tag("meta[itemprop=dateCreated]", "attr", "content")
        ));
    }


    /**
     * Private class used to register tags.
     */
    private static final class Tag {

        /**
         * Creates a new tag.
         *
         * @param s Selector.
         * @param t Type of tag.
         */
        private Tag(final String s, final String t) {
            this.selector = s;
            this.type = t;
        }

        /**
         * Creates a new tag with a specific attribute.
         *
         * @param s Selector.
         * @param t Type of tag.
         * @param a Attribute name.
         */
        private Tag(final String s,
                    final String t,
                    final String a) {
            this.selector = s;
            this.type = t;
            this.attr = a;
        }


        /**
         * CSS selector.
         */
        private String selector;

        /**
         * Tag type.
         */
        private String type = "content";

        /**
         * Attribute name if needed.
         */
        private String attr = "";

        /**
         * Get selector.
         *
         * @return Selector
         */
        public String getSelector() {
            return selector;
        }

        /**
         * Set selector.
         *
         * @param s Selector
         */
        public void setSelector(final String s) {
            this.selector = s;
        }

        /**
         * Get type.
         *
         * @return Type
         */
        public String getType() {
            return type;
        }

        /**
         * Set type.
         *
         * @param t Type
         */
        public void setType(final String t) {
            this.type = t;
        }

        /**
         * Get attr.
         *
         * @return Attr
         */
        public String getAttr() {
            return attr;
        }

        /**
         * Set attr.
         *
         * @param a Attr
         */
        public void setAttr(final String a) {
            this.attr = a;
        }


    }


}
