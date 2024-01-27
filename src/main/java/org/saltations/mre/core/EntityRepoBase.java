package org.saltations.mre.core;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.data.repository.CrudRepository;
import jakarta.validation.Valid;

/**
 * Repository for entities of type E.
 *
 * @param <ID> Type of the <em>entity</em> identifier .
 * @param <E> Class of the <em>entity</em>.
 */

public abstract class EntityRepoBase<ID,E extends Entity<ID>> implements CrudRepository<E,ID>
{
}
