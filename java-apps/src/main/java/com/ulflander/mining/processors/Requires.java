package com.ulflander.mining.processors;

/**
 * Defines requirements for Processors.
 *
 * @author Ulflander <xlaumonier@gmail.com>
 * @since 2/21/14
 */
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@java.lang.annotation.Target({
    java.lang.annotation.ElementType.TYPE
})
@java.lang.annotation.Inherited
public @interface Requires {

    /**
     * List of processors required.
     */
    String[] processors();

}
