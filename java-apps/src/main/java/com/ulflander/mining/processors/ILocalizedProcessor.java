package com.ulflander.mining.processors;

/**
 * Interface for localized processors.
 *
 * @author Ulflander <xlaumonier@gmail.com>
 * @since 2/21/14
 */
public interface ILocalizedProcessor {

    /**
     * Get languages supported by this processor.
     *
     * @return Array of language codes
     */
    String[] getLanguages();

}
