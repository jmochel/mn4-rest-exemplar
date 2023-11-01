package org.saltations.mre.core;

import io.micronaut.data.repository.CrudRepository;

/**
 * Repository for entities of type E.
 *
 * @param <ID> Type of the <em>entity</em> identifier .
 * @param <E> Class of the <em>entity</em>.
 */

public abstract class EntityRepoBase<ID,E extends Entity<ID>> implements CrudRepository<E,ID>
{
}
