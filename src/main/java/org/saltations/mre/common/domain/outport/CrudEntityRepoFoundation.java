package org.saltations.mre.common.domain.outport;

import java.util.List;

import io.micronaut.core.annotation.NonNull;
import org.saltations.mre.common.domain.model.Entity;

/**
 * Foundation (provides some default functionality) repository for entities of type E.
 *
 * @param <ID> Type of the <em>entity</em> identifier .
 * @param <E> Class of the <em>entity</em>.
 */

public abstract class CrudEntityRepoFoundation<ID,E extends Entity<ID>> implements CrudEntityRepo<ID,E>
{
    /**
     * Find all entities by their identifiers.
     *
     * @param ids entity identifiers. Non-null.
     *
     * @return Entities with the given identifiers. If an entity is not found, it is not included in the result. Empty
     *  list if no entities are found.
     */

    public abstract List<E> findAllByIdIn(@NonNull Iterable<ID> ids);

    /**
     * Delete entities by their identifiers.
     *
     * @param ids entity identifiers. Non-null.
     */

    public abstract void deleteByIdIn(@NonNull Iterable<ID> ids);
}
