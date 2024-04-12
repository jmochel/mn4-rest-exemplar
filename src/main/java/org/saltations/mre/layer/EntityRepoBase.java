package org.saltations.mre.layer;

import io.micronaut.data.repository.CrudRepository;

import java.util.List;

/**
 * Repository for entities of type E.
 *
 * @param <ID> Type of the <em>entity</em> identifier .
 * @param <E> Class of the <em>entity</em>.
 */

public abstract class EntityRepoBase<ID,E extends Entity<ID>> implements CrudRepository<E,ID>
{
    public abstract List<E> findAllByIdIn(Iterable<ID> ids);

    public abstract void deleteByIdIn(Iterable<ID> ids);
}
