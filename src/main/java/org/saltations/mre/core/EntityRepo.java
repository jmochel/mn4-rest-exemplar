package org.saltations.mre.core;

import io.micronaut.data.repository.CrudRepository;

/**
 * Repository for entities of type E.
 */

public abstract class EntityRepo<ID,E extends Entity<ID>> implements CrudRepository<E,ID>
{
}
