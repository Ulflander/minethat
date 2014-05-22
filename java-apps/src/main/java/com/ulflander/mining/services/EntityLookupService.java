package com.ulflander.mining.services;

import com.ulflander.app.model.Entity;
import com.ulflander.app.model.Token;

import java.util.List;

/**
 * Abstract entity lookup service.
 *
 * @author Ulflander <xlaumonier@gmail.com>
 */
public abstract class EntityLookupService {

    /**
     * Initialize lookup service.
     */
    public abstract void init();


    /**
     * Abstract method to implement in lookup service.
     *
     * @param token Token to test
     * @return Array of eligible entities
     */
    public abstract List<Entity> lookup(final Token token);

    /**
     * Abstract method to implement in lookup service.
     *
     * @param token Token to test
     * @return Array of eligible entities
     */
    public abstract List<Entity> lookupByClass(Token token);
}
