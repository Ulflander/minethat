package com.ulflander.mining.processors.extract;

import com.ulflander.app.model.Entity;
import com.ulflander.app.model.Token;
import com.ulflander.mining.processors.Processor;
import com.ulflander.mining.processors.ProcessorDepthControl;
import com.ulflander.mining.services.DbPediaLookupService;
import com.ulflander.mining.services.EntityLookupService;

import java.util.Collection;

/**
 * Finally select entity.
 *
 * Created by Ulflander on 5/18/14.
 */
public class EntitySelector extends Processor {

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
        float max = 0;
        Entity chosen = null;

        if (entities.size() > 1) {
            for (Entity e: entities) {

                if (e.getConfidence() > max) {
                    max = e.getConfidence();
                    chosen = e;
                }
            }
        } else if (entities.size() == 1) {
            for (Entity e: entities) {
                chosen = e;
            }
        }

        token.setEntity(chosen);
    }
}
