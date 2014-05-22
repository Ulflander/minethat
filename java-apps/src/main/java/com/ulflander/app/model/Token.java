package com.ulflander.app.model;

import com.google.gson.annotations.Expose;
import com.ulflander.mining.nlp.PennPOSTag;

import java.util.Collection;
import java.util.HashMap;

/**
 * A token is the most tiny part of a document: a word, a number, and so on.
 *
 * A token has a type (word, date, number...) and if it is a word, it also has
 * a part-of-speech tag (verb, noun, adverb...).
 *
 * @author Ulflander <xlaumonier@gmail.com>
 * @since 2/23/14
 */
public class Token extends Text {

    /**
     * Is aggregated (has been merged to previous).
     */
    private Boolean aggregated = false;

    /**
     * Sentence.
     */
    private Sentence sentence;

    /**
     * Previous token.
     */
    private Token previous = null;

    /**
     * Next token.
     */
    private Token next = null;

    /**
     * Type of token (Word, twitter username...).
     */
    @Expose
    private TokenType type = TokenType.WORD;

    /**
     * Penn treebank tag.
     */
    @Expose
    private PennPOSTag tag = PennPOSTag.U;

    /**
     * Singular representation of the token, if applicable.
     */
    @Expose
    private String singular = "";

    /**
     * Token type probabilities.
     */
    private HashMap<TokenType, Integer> typeScores =
            new HashMap<TokenType, Integer>();


    /**
     * Eligible entities for this token.
     */
    private HashMap<String, Entity> entities =
            new HashMap<String, Entity>();

    /**
     * Entity linked to token.
     */
    private Entity entity;

    /**
     * Instanciate a token with an empty string.
     */
    public Token() {
        super("");
    }

    /**
     * Instanciate a token with given string.
     *
     * @param raw String to generate token from
     */
    public Token(final String raw) {
        super(raw);
    }


    /**
     * Get best score that this token has - won't return any score if result
     * is ambiguous (e.g. two types have the same score).
     *
     * @param exclude List of types to exclude from search
     * @return Token type that has the best score
     */
    public final TokenType getBestScore(final TokenType... exclude) {
        int max = 0;
        boolean ambiguous = false;
        TokenType t = null;

        for (TokenType tt : typeScores.keySet()) {
            for (TokenType tte: exclude) {
                if (tte == tt) {
                    continue;
                }
            }

            if (typeScores.get(tt) > max) {
                max = typeScores.get(tt);
                ambiguous = false;
                t = tt;
            } else if (typeScores.get(tt) == max) {
                ambiguous = true;
            }
        }

        if (ambiguous) {
            return null;
        }

        return t;
    }

    /**
     * Check if given token share at least one score with another token.
     *
     * @param t Token to compare score to
     * @return True if share a score, false otherwise
     */
    public final boolean shareSomeScore(final Token t) {
        for (TokenType tt : typeScores.keySet()) {
            if (t.hasScore(tt)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Add/substract a score for a token type.
     *
     * @param tt Token type
     * @param i Score to add/substract
     */
    public final void score(final TokenType tt, final int i) {
        if (!typeScores.containsKey(tt)) {
            typeScores.put(tt, 0);
        }

        consolidate(tt, i);
    }

    /**
     * Add all given scores.
     *
     * @param scores Scores to add
     */
    public final void score(final HashMap<TokenType, Integer> scores) {
        for (TokenType tt : scores.keySet()) {
            score(tt, scores.get(tt));
        }
    }


    /**
     * Consolidate a score, only if score exists.
     *
     * @param tt Token type
     * @param i Score to add
     */
    public final void consolidate(final TokenType tt, final int i) {
        if (!typeScores.containsKey(tt)) {
            return;
        }

        typeScores.put(tt, typeScores.get(tt) + i);

        if (typeScores.get(tt) <= 0) {
            typeScores.remove(tt);
        }
    }

    /**
     * Consolidate current scores given on scores object.
     *
     * @param scores Scores to add
     * @param i Score to add
     */
    public final void consolidate(final HashMap<TokenType, Integer> scores,
                                  final int i) {

        for (TokenType tt : scores.keySet()) {
            consolidate(tt, i);
        }
    }

    /**
     * Consolidate a score, only if score exists, and consolidates also
     * sibbling tokens, decreasing score until radius is reached or score is
     * zero.
     *
     * @param tt Token type
     * @param i Score to add
     * @param radius radius
     */
    public final void consolidate(final TokenType tt,
                                  final int i,
                                  final int radius) {
        consolidate(tt, i, radius, TokenConsolidationDirection.BOTH);
    }

    /**
     * Consolidate a score, only if score exists, and consolidates also
     * sibbling tokens, decreasing score until radius is reached or score is
     * zero.
     *
     * @param tt Token type
     * @param i Score to add
     * @param radius radius
     * @param d Direction for propagation
     */
    public final void consolidate(final TokenType tt,
                                  final int i,
                                  final int radius,
                                  final TokenConsolidationDirection d) {
        consolidate(tt, i);

        if (i == 1 || radius == 0) {
            return;
        }

        if (d == TokenConsolidationDirection.FORWARD
            || d == TokenConsolidationDirection.BOTH) {

            if (hasNext()) {
                getNext().consolidate(tt, i - 1, radius - 1,
                        TokenConsolidationDirection.FORWARD);
            }

        }

        if (d == TokenConsolidationDirection.BACKWARD
                || d == TokenConsolidationDirection.BOTH) {
            if (hasPrevious()) {
                getPrevious().consolidate(tt, i - 1, radius - 1,
                        TokenConsolidationDirection.BACKWARD);
            }
        }
    }

    /**
     * Add a score, only if score is equals to zero.
     *
     * @param tt Token type
     * @param i Score to add
     */
    public final void infer(final TokenType tt, final int i) {
        if (typeScores.containsKey(tt)) {
            return;
        }

        typeScores.put(tt, i);
    }

    /**
     * Add a score, only if score is equals to zero, and also infer
     * sibbling tokens, decreasing score until radius is reached or score is
     * zero.
     *
     * @param tt Token type
     * @param i Score to add
     * @param radius radius
     */
    public final void infer(final TokenType tt,
                                  final int i,
                                  final int radius) {
        infer(tt, i, radius, TokenConsolidationDirection.BOTH);
    }


    /**
     * Add a score, only if score is equals to zero, and also infer
     * sibbling tokens in given direction, decreasing score until
     * radius is reached or score is
     * zero.
     *
     * @param tt Token type
     * @param i Score to add
     * @param radius radius
     * @param d Direction for propagation
     */
    public final void infer(final TokenType tt,
                            final int i,
                            final int radius,
                            final TokenConsolidationDirection d) {
        infer(tt, i);

        if (i == 1 || radius == 0) {
            return;
        }

        if (d == TokenConsolidationDirection.FORWARD
                || d == TokenConsolidationDirection.BOTH) {

            if (hasNext()) {
                getNext().infer(tt, i - 1, radius - 1,
                        TokenConsolidationDirection.FORWARD);
            }
        }

        if (d == TokenConsolidationDirection.BACKWARD
                || d == TokenConsolidationDirection.BOTH) {

            if (hasPrevious()) {
                getPrevious().infer(tt, i - 1, radius - 1,
                        TokenConsolidationDirection.BACKWARD);
            }
        }
    }

    /**
     * Discriminate a token type (remove from scores).
     *
     * @param tt Token type
     */
    public final void discriminate(final TokenType tt) {
        if (typeScores.containsKey(tt)) {
            typeScores.remove(tt);
        }
    }

    /**
     * Get current score for a TokenType.
     *
     * @param tt Token type
     * @return Score for given type
     */
    public final int getScore(final TokenType tt) {
        if (typeScores.containsKey(tt)) {
            return typeScores.get(tt);
        }

        return 0;
    }

    /**
     * Get scores for all TokenTypes.
     *
     * @return Scores for all types
     */
    public final HashMap<TokenType, Integer> getScores() {
        return (HashMap<TokenType, Integer>) typeScores.clone();
    }

    /**
     * Check if token has one and only one given score.
     *
     * @param tt Token type
     * @return True if token has the score and has no other score, false
     * otherwise
     */
    public final boolean hasUniqueScore(final TokenType tt) {
        return typeScores.size() == 1 && hasScore(tt);
    }

    /**
     * Check if token as any score.
     *
     * @return True if token has at least one score for one token type, false
     * otherwise
     */
    public final boolean hasScore() {
        return typeScores.size() > 0;
    }


    /**
     * Check if token has scores for given token types.
     *
     * @param types Token types to check
     * @return True if at least one token type is found, false otherwise
     */
    public final boolean hasScore(final TokenType... types) {
        for (TokenType tt:types) {
            if (typeScores.containsKey(tt)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Check if token is likely an entity (based on token type).
     *
     * @return True if token is likely an entity, false otherwise.
     */
    public final boolean isLikelyEntity() {
        return hasScore(TokenType.PERSON,
                TokenType.LOCATION,
                TokenType.LOCATION_PART,
                TokenType.NATIONALITY,
                TokenType.ORGANIZATION
            ) && !hasScore(TokenType.LOCATION_DESCRIPTOR);
    }

    /**
     * Get previous token value.
     *
     * @return Previous token
     */
    public final Token getPrevious() {
        return previous;
    }

    /**
     * Set previous token value.
     *
     * @param o Previous token
     */
    public final void setPrevious(final Token o) {
        this.previous = o;
    }

    /**
     * Get next token value.
     *
     * @return Next token
     */
    public final Token getNext() {
        return next;
    }

    /**
     * Set next token value.
     *
     * @param o Next token
     */
    public final void setNext(final Token o) {
        this.next = o;
    }

    /**
     * Check if token has another next right after in the sentence.
     *
     * @return True if a token exists after this one in the sentence, false
     * otherwise
     */
    public final Boolean hasNext() {
        return next != null;
    }

    /**
     * Check if token has another next right before in the sentence.
     *
     * @return True if a token exists before this one in the sentence, false
     * otherwise
     */
    public final Boolean hasPrevious() {
        return previous != null;
    }

    /**
     * Merge this token to the previous one.
     *
     * @return Previous token instance
     */
    public final Token mergeToPrevious() {
        if (!hasPrevious()) {
            return this;
        }
        String clean = previous.getClean();
        previous.score(getScores());
        previous.append(getSurface());
        previous.setWeight(getWeight());
        previous.setWeight(Text.FULL_WEIGHT);
        previous.setNext(next);
        previous.setAggregated(true);
        for (Entity e: entities.values()) {
            previous.addEntity(e);
        }

        if (hasNext()) {
            next.setPrevious(previous);
        }


        previous.setClean(clean + " " + getClean());
        return previous;
    }


    /**
     * Get token type value.
     *
     * @return Token type
     */
    public final TokenType getType() {
        return type;
    }

    /**
     * Set token type value.
     *
     * @param o Token type
     */
    public final void setType(final TokenType o) {
        this.type = o;
    }

    /**
     * Get Penn treebank Part-of-speech tag value.
     *
     * @return Penn treebank Part-of-speech tag
     */
    public final PennPOSTag getTag() {
        return tag;
    }

    /**
     * Set Penn treebank Part-of-speech tag value.
     *
     * @param o Penn treebank Part-of-speech tag
     */
    public final void setTag(final PennPOSTag o) {
        this.tag = o;
    }

    /**
     * Get singular of current token if exists.
     *
     * Singular of a token is set by specific language TokenSingularization
     * processors.
     *
     * @return Singular if exists, cleaned token otherwise.
     */
    public final String getSingular() {
        if (!singular.equals("")) {
            return singular;
        }
        return getClean();
    }

    /**
     * Set singular of current token.
     *
     * @param s Singular of current word
     */
    public final void setSingular(final String s) {
        if (s != null) {
            this.singular = s;
            return;
        }

        this.singular = "";
    }

    /**
     * Check whether current word is plural in initial document.
     *
     * @return True if word is used as plural, false otherwise
     */
    public final Boolean isPlural() {
        return !singular.equals("") && !singular.equals(getSurface());
    }

    /**
     * Get sentence.
     *
     * @return Sentence
     */
    public final Sentence getSentence() {
        return sentence;
    }

    /**
     * Set sentence.
     *
     * @param s Sentence
     */
    public final void setSentence(final Sentence s) {
        this.sentence = s;
    }

    /**
     * Get aggregated.
     *
     * @return Aggregated
     */
    public final Boolean getAggregated() {
        return aggregated;
    }

    /**
     * Set aggregated.
     *
     * @param a Aggregated
     */
    public final void setAggregated(final Boolean a) {
        this.aggregated = a;
    }

    /**
     * Get entity linked to this token.
     *
     * @return Entity linked to this token
     */
    public final Entity getEntity() {
        return entity;
    }

    /**
     * Set entity linked to this token.
     *
     * @param e Entity linked to this token
     */
    public final void setEntity(final Entity e) {
        this.entity = e;
    }

    /**
     * Add an entity to eligible entities.
     *
     * @param e Entity
     */
    public final void addEntity(final Entity e) {
        if (!entities.containsKey(e.getId())) {
            entities.put(e.getId(), e);
        }
    }

    /**
     * Remove an entity from eligible entities.
     *
     * @param e Entity
     */
    public final void removeEntity(final Entity e) {
        if (entities.containsKey(e.getId())) {
            entities.remove(e.getId());
        }
    }

    /**
     * Return first found entity for a given entity type (use this method when
     * you can safely assume there is only one entity for given type).
     *
     * @param et Entity type
     * @return Entity if found, null otherwise
     */
    public final Entity getFirstEntityByType(final EntityType et) {
        for (Entity e: entities.values()) {
            if (et == e.getType()) {
                return e;
            }
        }

        return null;
    }

    /**
     * Return entity that has the highest confidence score from entities list.
     *
     * @return Entity with the highest confidence score, null of no entity
     */
    public final Entity getMostConfidentEntity() {
        float max = 0;
        Entity res = null;

        for (Entity e: entities.values()) {
            if (max < e.getConfidence()) {
                max = e.getConfidence();
                res = e;
            }
        }

        return res;
    }

    /**
     * Get all eligible entities.
     *
     * @return Eligible entities as a collection
     */
    public final Collection<Entity> getEntities() {
        return entities.values();
    }

    /**
     * Check if has any eligible or chosen entity.
     *
     * @return True if any entity is eligible or associated to this token, false
     * otherwise
     */
    public final boolean hasEntity() {
        return entities.size() > 0 || entity != null;
    }
}
