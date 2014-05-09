package com.ulflander.mining.nlp;

import com.ulflander.app.model.Sentence;

/**
 * Abstract class for tokenizers.
 *
 * Created by Ulflander on 4/16/14.
 */
public abstract class Tokenizer {

    /**
     * Tokenize a sentence.
     *
     * @param sentence Sentence to tokenize
     */
    public abstract void tokenize(final Sentence sentence);

}
