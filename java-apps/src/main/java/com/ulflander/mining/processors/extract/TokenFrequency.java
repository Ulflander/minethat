package com.ulflander.mining.processors.extract;

import com.ulflander.app.model.Document;
import com.ulflander.app.model.Entity;
import com.ulflander.app.model.Heading;
import com.ulflander.app.model.HeadingLevel;
import com.ulflander.app.model.KeywordList;
import com.ulflander.app.model.Paragraph;
import com.ulflander.app.model.Token;
import com.ulflander.app.model.TokenType;
import com.ulflander.mining.processors.Processor;
import com.ulflander.mining.processors.ProcessorDepthControl;
import com.ulflander.mining.processors.Requires;

import java.util.HashMap;

/**
 * Creates a list of tokens based on their frequency, populates document
 * "basic_stats.avg_token_frequency" property, also populates average weight in
 * "basic_stats.avg_token_weight", and entity frequency in
 * "basic_stats.avg_entity_frequency".
 *
 * Created by Ulflander on 4/17/14.
 */
@Requires(processors = {
        "extract.DocumentTokenizer"
})
public class TokenFrequency extends Processor {

    @Override
    public final void init() {
        this.setDepthControl(ProcessorDepthControl.TOKEN);
        this.setInitialized(true);
    }

    @Override
    public final String describe() {
        return "Count occurences of each word, keyword and verb";
    }

    /**
     * Map string/token object.
     */
    private HashMap<String, Token> tokens;

    /**
     * Map token object/score.
     */
    private HashMap<Token, Integer> frequency;

    /**
     * Map frequency object/score.
     */
    private HashMap<String, Integer> frequencyEntity;

    /**
     * Total citations for all tokens.
     */
    private int total;

    /**
     * Total citations for all tokens.
     */
    private int totalEntities;

    /**
     * Total weight for all tokens.
     */
    private float totalWeight;

    @Override
    public final void extractDocument(final Document doc) {
        frequency = new HashMap<Token, Integer>();
        frequencyEntity = new HashMap<String, Integer>();
        tokens = new HashMap<String, Token>();
        total = 0;
        totalWeight = 0;
        totalEntities = 0;
    }

    @Override
    public final void extractToken(final Token token) {
        TokenType tt = token.getType();
        if (tt != TokenType.KEYWORD
            && tt != TokenType.WORD
            && tt != TokenType.VERB) {
            return;
        }

        totalWeight += token.getWeight();

        String clean;
        Token nt;
        if (tt == TokenType.WORD) {
            clean = token.getSingular();
        } else {
            clean = token.getClean();
        }
        int score = 1;


        Paragraph p = token.getSentence().getParagraph();
        if (Heading.class.isInstance(p)) {
            score *= 2;
            if (((Heading) p).getLevel() == HeadingLevel.IMPORTANT) {
                score *= 2;
            }
        } else if (KeywordList.class.isInstance(p)) {
            score *= 2;
        }

        if (token.hasEntity()) {
            score *= 2;
        }

        if (token.hasScore(
                TokenType.ACRONYM,
                TokenType.LOCATION_PART,
                TokenType.PERSON_PART,
                TokenType.ORGANIZATION
        )) {
            score *= 2;
        }

        if (!tokens.containsKey(clean)) {
            nt = new Token();
            nt.setSurface(clean);
            nt.setType(token.getType());
            nt.setTag(token.getTag());
            nt.setAbsoluteWeight(token.getWeight());
            tokens.put(clean, nt);
            frequency.put(nt, score);
        } else {
            nt = tokens.get(clean);
            frequency.put(nt, frequency.get(nt) + score);
        }

        if (token.getEntity() != null) {
            totalEntities += 1;

            Entity e = token.getEntity();
            if (!frequencyEntity.containsKey(e.getId())) {
                frequencyEntity.put(e.getId(), 1);
            } else {
                frequencyEntity.put(e.getId(),
                        frequencyEntity.get(e.getId() + 1));
            }
        }

        total += 1;

        nt.score(token.getScores());
    }

    @Override
    public final void onProcessed(final Document doc) {
        doc.setFrequency(frequency);
        doc.addProperty("basic_stats", "avg_token_weight",
                ((float) totalWeight / total));
        doc.addProperty("basic_stats", "avg_token_frequency",
                ((float) total / frequency.size()));
        doc.addProperty("basic_stats", "avg_entity_frequency",
                ((float) totalEntities / frequencyEntity.size()));
    }
}
