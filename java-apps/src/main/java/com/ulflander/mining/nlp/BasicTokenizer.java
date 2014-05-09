package com.ulflander.mining.nlp;

import com.ulflander.app.model.Sentence;
import com.ulflander.app.model.Token;

import java.util.Scanner;

/**
 * Performs basic tokenization using Java Scanner class.
 *
 * Created by Ulflander on 4/16/14.
 */
public class BasicTokenizer extends Tokenizer {

    @Override
    public final void tokenize(final Sentence sentence) {

        Scanner tokenize = new Scanner(sentence.getSurface());
        Token prev = null;
        int total = 0;

        while (tokenize.hasNext()) {
            Token t = new Token(tokenize.next());
            sentence.appendToken(t);

            if (prev != null) {
                prev.setNext(t);
                t.setPrevious(prev);
            }

            prev = t;
            total += 1;
        }
    }
}
