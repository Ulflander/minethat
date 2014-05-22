package com.ulflander.mining.services;

import java.util.ArrayList;

/**
 * CorporaResponse from a query.
 *
 * @author Ulflander <xlaumonier@gmail.com>
 */
public class CorporaResponse {

    /**
     * Number of corpus returned.
     */
    private int l;

    /**
     * List of corpus files where keyword is.
     */
    private ArrayList<String> corpora;

    /**
     * Creates a new CorporaResponse object.
     *
     * @param len Number of findings
     * @param c Corpus files where keyword was found
     */
    public CorporaResponse(final int len, final ArrayList<String> c) {
        if (len > 0 && (c == null || c.size() != l)) {
            new IllegalArgumentException("List of files doesn't have "
                                    + "as many corpus as length indicated");
        }
        this.l = len;
        this.corpora = c;
    }

    /**
     * Get number of corpus returned.
     *
     * @return Number of corpus returned
     */
    public final int length() {
        return l;
    }

    /**
     * Get corpus files list returned.
     *
     * @return Corpus files list returned
     */
    public final ArrayList<String> getCorpora() {
        return corpora;
    }


    /**
     * Create a CorporaResponse object from a services response string.
     *
     * @param s Corpora response as a string
     * @return CorporaResponse object
     */
    public static final CorporaResponse fromString(final String s) {
        if (s == null || s.length() == 0) {
            new IllegalArgumentException("Malformed response string: "
                + "null or empty");
        }

        if (s.length() == 1) {
            if (!s.equals("0")) {
                new IllegalArgumentException("Malformed response string: "
                        + "string length is 1, string should be '0' "
                        + "but is '" + s + "'");
            }

            return new CorporaResponse(0, null);
        }

        String[] parts = s.split(",");
        int i, l = parts.length, length = 0;
        ArrayList<String> files = new ArrayList<String>(l - 1);
        for (i = 0; i < l; i++) {
            if (i == 0) {
                length = Integer.valueOf(parts[i]);
            } else {
                files.add(parts[i]);
            }
        }

        return new CorporaResponse(length, files);
    }

}
