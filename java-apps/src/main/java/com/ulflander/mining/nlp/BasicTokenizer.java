package com.ulflander.mining.nlp;

import com.ulflander.app.model.Sentence;
import com.ulflander.app.model.Token;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Performs basic tokenization using Java Scanner class.
 *
 * Created by Ulflander on 4/16/14.
 */
public class BasicTokenizer extends Tokenizer {

    @Override
    public final void tokenize(final Sentence sentence) {

        String[] result = tokenize(sentence.getSurface());
        Token prev = null;
        int i,
            l = result.length;

        for (i = 0; i < l; i++) {
            Token t = new Token(result[i]);
            sentence.appendToken(t);

            if (prev != null) {
                prev.setNext(t);
                t.setPrevious(prev);
            }

            prev = t;
        }
    }


    /**
     * Tokenize a string.
     *
     * @param sentence String to tokenize
     * @return Array of String tokens
     */
    public final String[] tokenize(final String sentence) {

        Scanner tokenize = new Scanner(sentence);

        ArrayList<String> res = new ArrayList<String>();

        while (tokenize.hasNext()) {
            res.add(tokenize.next());
        }

        return (String[]) res.toArray(new String[0]);
    }
}
