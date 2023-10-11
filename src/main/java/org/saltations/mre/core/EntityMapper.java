package org.saltations.mre.core;

import org.mapstruct.MappingTarget;

/**
 * Represents standard mapping between core business objects and entities
 *
 * @param <ID>
 * @param <C> Class of the business item
 * @param <E> Class of the persistable business item entity. Contains all the same data as C but supports additional
 *           entity specific meta-data.
 */

public interface EntityMapper<ID, C, E extends Entity<ID>>
{
    /**
     * Maps a (Core) prototype to an Entity.
     *
     * @param proto prototype with core attributes to create an Entity.
     *
     * @return Valid Entity
     */

    E createEntity(C proto);

//    C createCore(E source);

    /**
     * Patches the entity with non-null values from the patch object
     *
     * @param patch object with core attributes used to update the entity.
     * @param entity object to be updated
     *
     * @return Patched entity
     */

    E patchEntity(C patch, @MappingTarget E entity);
}