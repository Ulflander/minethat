package com.ulflander.mining.processors.extract;

import com.ulflander.app.model.Entity;
import com.ulflander.app.model.Token;
import com.ulflander.mining.processors.Processor;
import com.ulflander.mining.processors.ProcessorDepthControl;
import com.ulflander.mining.services.DbPediaLookupService;
import com.ulflander.mining.services.EntityLookupService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Cleanup entities which have confidence under
 * threshold and/or that have no classe.
 *
 * Created by Ulflander on 5/18/14.
 */
public class EntityCleaner extends Processor {

    /**
     * Confidence threshold.
     */
    public static final float THRESHOLD = 0f;

    /**
     * DBPedia lookup service.
     */
    private EntityLookupService dbPedia;

    @Override
    public final void init() {
        this.setDepthControl(ProcessorDepthControl.TOKEN);
        dbPedia = new DbPediaLookupService();
        dbPedia.init();
        this.setInitialized(true);
    }

    @Override
    public final String describe() {
        return "Lookup various services (DBPedia..) and check for entities.";
    }

    @Override
    public final void extractToken(final Token token) {
        Collection<Entity> entities = token.getEntities();

        if (entities.size() > 1) {
            List<Entity> toRemove = new ArrayList<Entity>();
            for (Entity e: entities) {
                List<String> classes = e.getClasses();
                if (e.getConfidence() < THRESHOLD
                        || classes == null
                        || classes.size() == 0) {
                    toRemove.add(e);
                }
            }

            for (Entity e: toRemove) {
                token.removeEntity(e);
            }
        }
    }
}
