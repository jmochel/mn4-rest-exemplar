package org.saltations.mre.core;

import org.mapstruct.MappingTarget;

/**
 * Provides standard mapping functionality between <em>core objects</em>, and their corresponding <em>entities</em>
 *
 * @param <C> Class of the <em>core</em> business item
 * @param <E> Class of the <em>entity</em>.
 */

public interface EntityMapper<C, E>
{
    /**
     * Maps a (Core) prototype to an Entity.
     *
     * @param proto prototype with core attributes to create an Entity.
     *
     * @return Valid Entity
     */

    E createEntity(C proto);

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