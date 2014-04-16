package com.cybozu.labs.langdetect;

import com.cybozu.labs.langdetect.util.LangProfile;
import com.google.gson.Gson;
import com.ulflander.application.utils.UlfResourceUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Language Detector Factory Class
 * <p/>
 * This class manages an initialization and constructions of {@link Detector}.
 * <p/>
 * Before using language detection library,
 * load profiles with {@link DetectorFactory#loadProfile(String[])} method
 * and set initialization parameters.
 * <p/>
 * When the language detection,
 * construct Detector instance via {@link DetectorFactory#create()}.
 * See also {@link Detector}'s sample code.
 * <p/>
 * <ul>
 * <li>4x faster improvement based on Elmer Garduno's code. Thanks!</li>
 * </ul>
 *
 * @author Nakatani Shuyo
 * @see Detector
 */
public class DetectorFactory {

    /**
     * Logger.
     */
    private static final Logger LOGGER =
        LogManager.getLogger(DetectorFactory.class);

    private ArrayList<String> profilesLoaded = new ArrayList<String>();

    public HashMap<String, double[]> wordLangProbMap;
    public ArrayList<String> langlist;
    public Long seed = null;

    private DetectorFactory() {
        wordLangProbMap = new HashMap<String, double[]>();
        langlist = new ArrayList<String>();
    }

    static private DetectorFactory instance_ = new DetectorFactory();


    /**
     * Load profiles from specified directory.
     * This method must be called once before language detection.
     *
     * @param listFiles List of profiles to load
     * @throws LangDetectException Can't open profiles(error code = {@link ErrorCode#FileLoadError})
     *                             or profile's format is wrong (error code = {@link ErrorCode#FormatError})
     */
    public static void loadProfile(String[] listFiles) throws LangDetectException {

        if (listFiles == null) {
            throw new LangDetectException(ErrorCode.NeedLoadProfileError, "Not found profile: listFiles is null");
        }

        int langSize = listFiles.length, index = 0;
        for (String file : listFiles) {
            if (instance_.profilesLoaded.contains(file)) {
                ++index;
                continue;
            }
            instance_.profilesLoaded.add(file);

            String profileStr = UlfResourceUtils.get("com/cybozu/labs/langdetect/profiles/" + file);

            if (profileStr.equals("")) {
                LOGGER.error("Unable to get language profile for " + file);
                ++index;
                continue;
            }

            Gson gson = new Gson();
            LangProfile profile = gson.fromJson(profileStr, LangProfile.class);
            addProfile(profile, index, langSize);
            ++index;

        }
    }

    /**
     * @param profile
     * @param langsize
     * @param index
     * @throws LangDetectException
     */
    static /* package scope */ void addProfile(LangProfile profile, int index, int langsize) throws LangDetectException {
        String lang = profile.name;
        if (instance_.langlist.contains(lang)) {
            LOGGER.error("Duplicate the same language profile: " + lang);
            return;
        }
        instance_.langlist.add(lang);
        for (String word : profile.freq.keySet()) {
            if (!instance_.wordLangProbMap.containsKey(word)) {
                instance_.wordLangProbMap.put(word, new double[langsize]);
            }
            int length = word.length();
            if (length >= 1 && length <= 3) {
                double prob = profile.freq.get(word).doubleValue() / profile.n_words[length - 1];
                instance_.wordLangProbMap.get(word)[index] = prob;
            }
        }
    }

    /**
     * for only Unit Test
     */
    static public void clear() {
        instance_.langlist.clear();
        instance_.wordLangProbMap.clear();
    }

    /**
     * Construct Detector instance
     *
     * @return Detector instance
     * @throws LangDetectException
     */
    static public Detector create() throws LangDetectException {
        return createDetector();
    }

    /**
     * Construct Detector instance with smoothing parameter
     *
     * @param alpha smoothing parameter (default value = 0.5)
     * @return Detector instance
     * @throws LangDetectException
     */
    public static Detector create(double alpha) throws LangDetectException {
        Detector detector = createDetector();
        detector.setAlpha(alpha);
        return detector;
    }

    static private Detector createDetector() throws LangDetectException {
        if (instance_.langlist.size() == 0)
            throw new LangDetectException(ErrorCode.NeedLoadProfileError, "need to load profiles");
        Detector detector = new Detector(instance_);
        return detector;
    }

    public static void setSeed(long seed) {
        instance_.seed = seed;
    }

    public static List<String> getLangList() {
        return Collections.unmodifiableList(instance_.langlist);
    }
}
