package org.saltations.mre.core;

/**
 * Generates example core objects and entities that implement the core interface (IC)
 * <p>
 * Provides example objects, methods to characterize the objects being tested, and methods used to make common
 * assertions about the objects. It provides core objects, which are POJOs that contain all the properties needed to
 * create an entity. It also provides entities which contain all the properties of the core objects plus whatever
 * tracking metadata is needed for the entity.
 * </p>
 *
 * @param <IC> Interface of the domain item being represented
 * @param <C>  Class of the domain item
 * @param <E>  Class of the persistable domain entity.
 *             Contains all the same data as C but supports additional entity specific meta-data.
 */

public interface EntityOracle<IC, C extends IC, E extends IC>
{
    /**
     * Returns a core object populated with all the data needed to create an entity
     */

    C corePrototype();

    /**
     * Returns a core object with different values from the core prototype
     */

    C modifiedCore();

    /**
     * Returns a simple entity with the same values as the core prototype and the metadata an entity needs
     */

    E entityPrototype();

    /**
     * Confirms that the objects have the same core data
     */

    void hasSameCoreContent(IC expected, IC actual);

    default void hasSameCoreContentAsPrototype(IC actual)
    {
        hasSameCoreContent(corePrototype(), actual);
    }
}
