package com.ulflander.mining.services;

import com.ulflander.app.model.Entity;

import java.util.List;

/**
 * Result of DBPedia Lookup.
 *
 * @author Ulflander <xlaumonier@gmail.com>
 */
public class DbPediaLookupResult extends Entity {


    /**
     * Categories URIs.
     */
    private List<String> categories;

    /**
     * References count.
     */
    private int refCount = 0;

    /**
     * Check if result has category.
     *
     * @param c Category URI
     * @return True if result has given category
     */
    public final boolean hasCategory(final String c) {
        return categories != null && categories.contains(c);
    }

    /**
     * Get categories.
     *
     * @return Categories
     */
    public final List<String> getCategories() {
        return categories;
    }

    /**
     * Set categories.
     *
     * @param c Categories
     */
    public final void setCategories(final List<String> c) {
        this.categories = c;
    }

    /**
     * Get ref count.
     *
     * @return Ref count
     */
    public final int getRefCount() {
        return refCount;
    }

    /**
     * Set ref count.
     *
     * @param r Ref count
     */
    public final void setRefCount(final int r) {
        this.refCount = r;
    }


}
