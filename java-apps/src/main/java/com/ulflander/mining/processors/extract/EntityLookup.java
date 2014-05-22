package com.ulflander.mining.processors.extract;

import com.ulflander.app.model.Entity;
import com.ulflander.app.model.Token;
import com.ulflander.mining.processors.Processor;
import com.ulflander.mining.processors.ProcessorDepthControl;
import com.ulflander.mining.services.DbPediaLookupService;
import com.ulflander.mining.services.EntityLookupService;

import java.util.List;

/**
 * If some token may be an entity, let's lookup various services and retrieve
 * entities.
 *
 * Created by Ulflander on 5/18/14.
 */
public class EntityLookup extends Processor {


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

        if (token.isLikelyEntity()) {
            List<Entity> closedSearch = dbPedia.lookupByClass(token);

            if (closedSearch != null && closedSearch.size() > 0) {
                for (Entity e: closedSearch) {
                    token.addEntity(e);
                }
            }

            List<Entity> openSearch = dbPedia.lookup(token);
            if (openSearch != null && openSearch.size() > 0) {
                for (Entity e: openSearch) {
                    token.addEntity(e);
                }
            }
        }
    }
}
