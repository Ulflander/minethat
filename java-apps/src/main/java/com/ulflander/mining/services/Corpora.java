package com.ulflander.mining.services;

import com.ulflander.app.Conf;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tika.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

/**
 * Send a message via a socket to services service.
 * It's no more than a socket client call handler.
 *
 * Created by Ulflander on 4/23/14.
 */
public final class Corpora {

    /**
     * Max call length.
     */
    private static final int MAX_CALL_LENGTH = 255;

    /**
     * Max calls.
     */
    private static final int MAX_CALLS = 5;

    /**
     * Wait time in milliseconds in case of error.
     */
    private static final int WAIT_TIME = 60;

    /**
     * Private constructor.
     */
    private Corpora() {

    }

    /**
     * Logger.
     */
    private static final Logger LOGGER =
        LogManager.getLogger(Corpora.class);

    /**
     * Send a message to services service via a socket and returns response.
     *
     * @param msg Message to send
     * @param calls Retries if failed
     * @return Message returned
     */
    private static String call(final String msg, final int calls) {

        if (msg.length() > MAX_CALL_LENGTH) {
            new IllegalArgumentException("Messages for services socket "
                                        + "cant be longer than 255 (Trying "
                                        + "to send '" + msg + "'");
        }

        String res = null;
        PrintStream out = null;
        BufferedReader in = null;
        Socket s = null;

        try {
            s = new Socket(Conf.getCorporaHost(),
                    Conf.getCorporaPort());
            out = new PrintStream(s.getOutputStream());
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out.println(msg);
            res = in.readLine();
        } catch (IOException e) {
            if (calls == 0) {
                LOGGER.warn("Unable to connect to socket server "
                            + "(tried 3 times). Last error: ", e);
            } else {
                try {
                    /*
                        THIS IS A HACKKKKKK

                         When performing lot of queries sometime connection
                         is considered as resetted
                         (while it's not on socket server side).

                         This hack prevents retry to fail.
                     */
                    Thread.sleep(WAIT_TIME);
                } catch (InterruptedException e2) {
                    LOGGER.error("Unable to wait for next socket query.", e2);
                    return null;
                }
                return call(msg, calls - 1);
            }
        } finally {
            if (out != null) {
                IOUtils.closeQuietly(out);
            }
            if (in != null) {
                IOUtils.closeQuietly(in);
            }
            if (s != null && s.isConnected()) {
                try {
                    s.close();
                } catch (IOException e) {
                    LOGGER.error("Unable to close socket", e);
                }
            }
        }

        return res;
    }

    /**
     * Query and get response.
     *
     * @param keyword Keyword
     * @param corpora Corpus files
     * @return A CorporaResponse object or null if query failed
     */
    public static CorporaResponse query(final String keyword,
                                final String[] corpora) {
        String res = call(build(keyword, corpora), MAX_CALLS);

        if (res == null) {
            LOGGER.warn("Corpora query failed");
            return null;
        }

        return CorporaResponse.fromString(res);
    }

    /**
     * Create query message.
     *
     * @param keyword Keyword
     * @param corpora Corpus files
     * @return A string readable by services service
     */
    private static String build(final String keyword,
                         final String[] corpora) {


        return StringUtils.join(corpora, ",") + " " + keyword;
    }

}
