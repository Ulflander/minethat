package com.ulflander.mining.processors.extract;

import com.ulflander.app.model.Token;
import com.ulflander.mining.processors.Processor;
import com.ulflander.mining.processors.ProcessorDepthControl;
import com.ulflander.mining.services.DbPediaLookupService;
import com.ulflander.mining.services.EntityLookupService;

/**
 * If some token may be an entity, let's lookup various services and retrieve
 * entities.
 *
 * Created by Ulflander on 5/18/14.
 */
public class UnknownPersonToEntity extends Processor {


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

    }
}
