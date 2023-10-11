package org.saltations.mre.core;

import io.micronaut.core.annotation.NonNull;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.saltations.mre.core.errors.CannotCreateEntity;
import org.saltations.mre.core.errors.CannotDeleteEntity;
import org.saltations.mre.core.errors.CannotUpdateEntity;

import java.util.Optional;

/**
 *  Generic service for processing entities; An object uniquely identified by a single identity attribute
 *
 * @param <ID> Type of the unique identifier .
 * @param <IC> Interface of the business item being represented
 * @param <E> Class of the persistable business item entity. Contains all the same data as C but supports additional
 *           entity specific meta-data.
 * @param <R> Type of the repository used by the service
 */

public abstract class EntityService<ID, IC, C extends IC, E extends Entity<ID>,  R extends EntityRepo<ID,E>, M extends EntityMapper<ID,C,E>>
{
    private final Class<E> clazz;

    private final R repo;

    private final EntityMapper<ID,C,E> mapper;

    /**
     * Primary constructor
     *
     * @param clazz Type of the entity
     * @param repo  Repository for persistence of entities
     */

    public EntityService(Class<E> clazz, R repo, EntityMapper<ID,C,E> mapper)
    {
        this.repo = repo;
        this.clazz = clazz;
        this.mapper = mapper;
    }

    /**
     *      * {@return {@code true} if and only if this class has
     *      * the synthetic modifier bit set}
     * @return the simple name of the entity type
     */

    protected String resourceTypeName()
    {
        return clazz.getSimpleName();
    }

    /**
     * Returns a publisher for existence of the entity for a given id.
     *
     * @param id Identifier. Not null.
     *
     * @return Mono for the boolean result.
     */
    @NonNull
    public Boolean existsById(@NotNull ID id)
    {
        return repo.existsById(id);
    }

    /**
     * Find the entity by its identifier
     *
     * @param id Identifier. Not null.
     *
     * @return Possible result. {@link Optional#empty()} if no entity matching the id is find.
     */

    public Optional<E> findById(@NotNull ID id)
    {
        return repo.findById(id);
    }

    /**
     * Creates an entity of type E from the prototype object.
     *
     * @param prototype Prototype object that contains the attributes necessary to create an entity of type E. Valid and not null.
     *
     * @return Populated entity of type E
     *
     * @throws CannotCreateEntity if the entity could not be created from the prototype
     */

    @Transactional(Transactional.TxType.REQUIRED)
    public E create(@NotNull @Valid C prototype) throws CannotCreateEntity {
        E created;

        try
        {
            var toBeCreated = mapper.createEntity(prototype);

            created = repo.save(toBeCreated);
        }
        catch (Exception e)
        {
            throw new CannotCreateEntity(e, resourceTypeName(), prototype.toString());
        }

        return created;
    }

    /**
     * Updates an entity of type E with the contents of the given entity.
     *
     * @param update is the entity with the modified values and the ID of the entity to be modified. Valid and not null.
     *
     * @return updated entity.
     *
     * @throws CannotUpdateEntity If the entity could not be updated for any reason
     */

    @Transactional(Transactional.TxType.MANDATORY)
    public E update(@NotNull @Valid E update) throws CannotUpdateEntity
    {
        try
        {
            return repo.update(update);
        }
        catch (Exception e)
        {
            throw new CannotUpdateEntity(e, resourceTypeName(), update);
        }
    }

    /**
     * Deletes an entity of type E with the given ID.
     *
     * @param id is the unique identifier for the entity
     *
     * @throws CannotDeleteEntity If the entity could not be deleted for any reason
     */

    @Transactional(Transactional.TxType.MANDATORY)
    public void delete(@NotNull ID id) throws CannotDeleteEntity
    {
        try
        {
            repo.deleteById(id);
        }
        catch (Exception e)
        {
            throw new CannotDeleteEntity(e, resourceTypeName(), id);
        }
    }
}
