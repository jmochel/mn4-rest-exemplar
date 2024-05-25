package org.saltations.mre.domain.services.service;

import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.validation.validator.Validator;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.saltations.mre.domain.model.Entity;
import org.saltations.mre.domain.model.EntityMapper;
import org.saltations.mre.domain.services.repo.EntityRepoBase;

import static java.util.stream.Collectors.joining;

/**
 * Generic service for creating, finding, replacing, patching and deleting <em>entities</em>
 *
 * @param <ID> Type of the <em>entity</em> identifier .
 * @param <IC> Interface of the <em>core</em> business concept
 * @param <C> Class of the <em>core object</em>
 * @param <E> Class of the <em>entity</em>.
 * @param <ER> Type of the <em>entity repository</em> used by the service
 * @param <EM> Type of the <em>entity mapper</em> used by the service
 */

public abstract class EntityServiceBase<ID, IC, C extends IC, E extends Entity<ID>,
                                                ER extends EntityRepoBase<ID,E>,
                                                EM extends EntityMapper<C,E>>
{
    private final Class<E> entityClass;

    private final ER entityRepo;

    private final EntityMapper<C,E> entityMapper;

    private final ObjectMapper jacksonMapper;

    private final Validator validator;

    /**
     * Primary constructor
     *
     * @param entityClass Type of the entity
     * @param entityRepo  Repository for persistence of entities
     */

    public EntityServiceBase(Class<E> entityClass, ER entityRepo, EntityMapper<C,E> entityMapper, Validator validator)
    {
        this.entityRepo = entityRepo;
        this.entityClass = entityClass;
        this.entityMapper = entityMapper;
        this.validator = validator;

        this.jacksonMapper = new ObjectMapper();
        this.jacksonMapper.registerModule(new JavaTimeModule());
    }

    protected String resourceTypeName()
    {
        return entityClass.getSimpleName();
    }

    /**
     * Returns a publisher for existence of the entity for a given id.
     *
     * @param id Identifier. Not null.
     *
     * @return Mono for the boolean result.
     */
    @NonNull
    public Boolean exists(@NotNull ID id)
    {
        return entityRepo.existsById(id);
    }

    /**
     * Find the entity by its identifier
     *
     * @param id Identifier. Not null.
     *
     * @return Possible result. {@link Optional#empty()} if no entity matching the id is find.
     */

    public Optional<E> find(@NotNull ID id)
    {
        return entityRepo.findById(id);
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
    public E create(@NotNull @Valid C prototype) throws CannotCreateEntity
    {
        E created;

        try
        {
            var toBeCreated = entityMapper.createEntity(prototype);

            created = entityRepo.save(toBeCreated);
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

    @Transactional(Transactional.TxType.REQUIRED)
    public E update(@NotNull @Valid E update) throws CannotUpdateEntity
    {
        try
        {
            return entityRepo.update(update);
        }
        catch (Exception e)
        {
            throw new CannotUpdateEntity(e, resourceTypeName(), update);
        }
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public E patch(@NotNull ID id, @NotNull JsonMergePatch mergePatch) throws CannotUpdateEntity, CannotPatchEntity
    {
        try
        {
            var retrieved = entityRepo.findById(id).get();

            var retrievedAsString = jacksonMapper.writeValueAsString(retrieved);
            var retrievedAsJsonNode = jacksonMapper.readTree(retrievedAsString);

            var patchedAsJsonNode = mergePatch.apply(retrievedAsJsonNode);
            var patchedAsString = jacksonMapper.writeValueAsString(patchedAsJsonNode);

            var patched = jacksonMapper.readValue(patchedAsString, entityClass);

            // Because update does not require a valid POJO We validate it ahead of time

            var violations = validator.validate(patched);

            if (!violations.isEmpty())
            {
                var violationsAsStr = violations.stream().map(v -> v.getMessage())
                        .collect(joining(",", "[","]"));

                throw new ConstraintViolationException(violations);
            }


            return entityRepo.update(patched);
        }
        catch (Exception e)
        {
            throw new CannotPatchEntity(e, resourceTypeName(), id);
        }
    }

    /*
        var objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        var retrievedAsString = objectMapper.writeValueAsString(retrieved);
        var retrievedAsJsonNode = objectMapper.readTree(retrievedAsString);

        var mergePatchAsString = "{ \"first_name\" : \"Sandama\"  }";
        var mergePatch = objectMapper.readValue(mergePatchAsString, JsonMergePatch.class);

        var patchedAsJsonNode = mergePatch.apply(retrievedAsJsonNode);
        var patchedAsString = objectMapper.writeValueAsString(patchedAsJsonNode);

        var patched = objectMapper.readValue(patchedAsString, PersonEntity.class);
     */

    /**
     * Deletes an entity of type E with the given ID.
     *
     * @param id is the unique identifier for the entity
     *
     * @throws CannotDeleteEntity If the entity could not be deleted for any reason
     */

    @Transactional(Transactional.TxType.REQUIRED)
    public void delete(@NotNull ID id) throws CannotDeleteEntity
    {
        try
        {
            entityRepo.deleteById(id);
        }
        catch (Exception e)
        {
            throw new CannotDeleteEntity(e, resourceTypeName(), id);
        }
    }
}
