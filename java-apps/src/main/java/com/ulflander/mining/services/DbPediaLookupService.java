package com.ulflander.mining.services;

import com.ulflander.app.Conf;
import com.ulflander.app.model.Entity;
import com.ulflander.app.model.EntitySource;
import com.ulflander.app.model.Token;
import com.ulflander.app.model.TokenType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Query object for DBPedia Lookup service.
 *
 * @author Ulflander <xlaumonier@gmail.com>
 */
public final class DbPediaLookupService extends EntityLookupService {

    /**
     * Logger.
     */
    private static final Logger LOGGER =
        LogManager.getLogger(DbPediaLookupService.class);

    /**
     * API endpoint.
     */
    private static String endpoint =
            "http://10.255.0.24:1111/";

    /**
     * API path.
     */
    private static String path = "api/search.asmx/KeywordSearch?";

    /**
     * Confidence for entities found with a query done with a specific class.
     */
    public static final Float CLASS_QUERY_CONFIDENCE = 0.5f;

    /**
     * Confidence for entities found with a query done with no specific class.
     */
    public static final Float GLOBAL_QUERY_CONFIDENCE = 0.4f;



    /**
     * XML Parser factory.
     */
    private DocumentBuilderFactory factory =
            DocumentBuilderFactory.newInstance();

    /**
     * Get URL for DbPedia lookup service given a token.
     *
     * @param token Token to search in DbPedia
     * @return URL for DbPedia query
     * @throws UnsupportedEncodingException In case URL is invalid
     */
    private String getLookupURL(final Token token)
            throws UnsupportedEncodingException {

        return endpoint
                + path
                + "QueryClass=&QueryString="
                + URLEncoder.encode(token.getClean(), "UTF-8");
    }

    /**
     * Get DbPedia lookup class (ontology) from token type.
     *
     * @param tt Token type
     * @return DbPedia class if found, null otherwise
     */
    private String getClassFromType(final TokenType tt) {
        if (tt == TokenType.ORGANIZATION) {
            return "Organisation";
        } else if (tt == TokenType.PERSON) {
            return "Person";
        } else if (tt == TokenType.LOCATION || tt == TokenType.LOCATION_PART) {
            return "Place";
        }

        return null;
    }

    /**
     * Get URL with class selection for DbPedia lookup service given a token.
     *
     * @param token Token being looked up
     * @param clean Clean string to lookup in DbPedia
     * @return URL for DbPedia query
     * @throws UnsupportedEncodingException In case URL is invalid
     */
    private String getClassLookupURL(final Token token, final String clean)
            throws UnsupportedEncodingException  {

        TokenType type = token.getBestScore(TokenType.PERSON_PART);
        if (type == null) {
            return null;
        }

        String clazz = getClassFromType(type);
        if (clazz == null) {
            return null;
        }

        return endpoint
                + path
                + "QueryClass="
                + clazz
                + "&QueryString="
                + URLEncoder.encode(clean, "UTF-8");
    }

    @Override
    public void init() {
        endpoint = Conf.getDbPediaLookupHost();
    }

    /**
     * Look up by class and by giving actual string to lookup.
     *
     * @param token Token being looked up
     * @param clean Clean string to lookup in DbPedia
     * @return Results of lookup
     */
    private List<Entity> lookupByClass(final Token token,
                                       final String clean) {

        ArrayList<Entity> results = new ArrayList<Entity>();

        try {
            String url = getClassLookupURL(token, clean);
            if (url == null) {
                return null;
            }
            ArrayList<DbPediaLookupResult> lookedUp = lookup(url);
            for (DbPediaLookupResult res: lookedUp) {
                res.setConfidence(CLASS_QUERY_CONFIDENCE);
                results.add(res);
            }
        } catch (Exception e) {
            LOGGER.error("Unable to lookup '" + clean + "'", e);
        }

        return results;
    }

    @Override
    public List<Entity> lookupByClass(final Token token) {

        List<Entity> results = lookupByClass(token, token.getClean());

        // If nothing found, and a person, and one or more one letter
        // subparts ("V" in Vladimir V Putin) let's try without this
        // subpart
        if (token.getScore(TokenType.PERSON) > 0) {
            String clean = token.getClean().replaceAll(" \\w ", " ");

            if (!clean.equals(token.getClean())) {
                if (results == null) {
                    results = new ArrayList<Entity>();
                }
                List<Entity> results2 = lookupByClass(token, clean);
                if (results2 != null) {
                    results.addAll(results2);
                }
            }
        }

        return results;
    }

    @Override
    public List<Entity> lookup(final Token token) {
        ArrayList<Entity> results = new ArrayList<Entity>();

        try {
            String url = getLookupURL(token);
            ArrayList<DbPediaLookupResult> lookedUp = lookup(url);

            for (DbPediaLookupResult res: lookedUp) {
                res.setConfidence(GLOBAL_QUERY_CONFIDENCE);
                results.add(res);
            }
        } catch (Exception e) {
            LOGGER.error("Unable to lookup '" + token.getClean() + "'", e);
        }
        return results;
    }


    /**
     * Normalize result from DbPedia.
     *
     * @param results Results to normalize
     * @return Normalized results (is a reference to "results" parameter)
     */
    private ArrayList<DbPediaLookupResult> normalize(
            final ArrayList<DbPediaLookupResult> results) {
        for (DbPediaLookupResult result: results) {
            if (result.hasCategory(
                    "http://dbpedia.org/resource/Category:Living_people")) {
                result.addClass("http://schema.org/Person");
            }
        }

        return results;
    }

    /**
     * Query service and parse response using java XML parser.
     *
     * @param uri URI of service
     * @return Array of results
     * @throws ParserConfigurationException Parser exception
     * @throws SAXException SAX exception
     * @throws IOException IO exception
     */
    private ArrayList<DbPediaLookupResult> lookup(final String uri)
            throws ParserConfigurationException,
            SAXException,
            IOException {

        ArrayList<DbPediaLookupResult> results =
                new ArrayList<DbPediaLookupResult>();

        DocumentBuilder b = factory.newDocumentBuilder();
        Document d = b.parse(uri);
        d.getDocumentElement().normalize();


        NodeList nList = d.getElementsByTagName("Result");
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element el = (Element) nNode;
                DbPediaLookupResult result = new DbPediaLookupResult();

                result.setSource(EntitySource.RDF);

                result.setLabel(el.getElementsByTagName("Label")
                        .item(0).getTextContent());

                result.setDescription(el.getElementsByTagName("Description")
                        .item(0).getTextContent());

                result.setValue(el.getElementsByTagName("URI")
                        .item(0).getTextContent());

                result.setRefCount(Integer.valueOf(
                        el.getElementsByTagName("Refcount")
                                .item(0).getTextContent()));

                result.setClasses(extractSubList(el, "Class"));
                result.setCategories(extractSubList(el, "Category"));

                results.add(result);
            }

        }
        return normalize(results);
    }

    /**
     * Extract class or categories list from DOM element.
     *
     * @param el DOM element
     * @param tagName Tag name to extract list from ("Class", "Category")
     * @return List of classes or categories
     */
    private ArrayList<String> extractSubList(
            final Element el,
            final String tagName) {

        NodeList classes = el.getElementsByTagName(tagName);
        ArrayList<String> clazzes = new ArrayList<String>();

        if (classes.getLength() > 0) {
            for (int j = 0; j < classes.getLength(); j++) {
                Node clazz = classes.item(j);

                if (clazz.getNodeType() == Node.ELEMENT_NODE) {
                    clazzes.add(
                            ((Element) clazz).getElementsByTagName("URI")
                                    .item(0).getTextContent());
                }
            }
        }

        return clazzes;
    }
}
