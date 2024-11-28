package org.saltations.mre.common.domain.logic;

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
import org.saltations.mre.common.domain.model.Entity;
import org.saltations.mre.common.domain.model.EntityMapper;
import org.saltations.mre.common.domain.gateway.CrudEntityRepo;

/**
 * Foundation (provides some default functionality) service for creating, finding, replacing, patching and deleting <em>entities</em>
 *
 * @param <ID> Type of the <em>entity</em> identifier .
 * @param <IC> Interface of the <em>core</em> business concept
 * @param <C> Class of the <em>core object</em>
 * @param <E> Class of the <em>entity</em>.
 * @param <ER> Type of the <em>entity repository</em> used by the service
 * @param <EM> Type of the <em>entity mapper</em> used by the service
 */

public abstract class CrudEntityServiceFoundation<ID, IC, C extends IC, E extends Entity<ID>,
                                                ER extends CrudEntityRepo<ID,E>,
                                                EM extends EntityMapper<C,E>> implements CrudEntityService<ID, IC, C, E, ER, EM>
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

    public CrudEntityServiceFoundation(Class<E> entityClass, ER entityRepo, EntityMapper<C,E> entityMapper, Validator validator)
    {
        this.entityRepo = entityRepo;
        this.entityClass = entityClass;
        this.entityMapper = entityMapper;
        this.validator = validator;

        this.jacksonMapper = new ObjectMapper();
        this.jacksonMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public String getEntityName()
    {
        return entityClass.getSimpleName();
    }

    @Override
    public @NonNull Boolean exists(@NotNull ID id)
    {
        return entityRepo.existsById(id);
    }

    @Override
    public Optional<E> find(@NotNull ID id)
    {
        return entityRepo.findById(id);
    }

    @Transactional(Transactional.TxType.REQUIRED)
    @Override
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
            throw new CannotCreateEntity(e, getEntityName(), prototype.toString());
        }

        return created;
    }

    @Transactional(Transactional.TxType.REQUIRED)
    @Override
    public E update(@NotNull @Valid E update) throws CannotUpdateEntity
    {
        try
        {
            return entityRepo.update(update);
        }
        catch (Exception e)
        {
            throw new CannotUpdateEntity(e, getEntityName(), update);
        }
    }

    @Transactional(Transactional.TxType.REQUIRED)
    @Override
    public E patch(@NotNull ID id, @NotNull JsonMergePatch mergePatch) throws CannotPatchEntity
    {
        try
        {
            var retrieved = entityRepo.findById(id).orElseThrow();

            var retrievedAsString = jacksonMapper.writeValueAsString(retrieved);
            var retrievedAsJsonNode = jacksonMapper.readTree(retrievedAsString);

            var patchedAsJsonNode = mergePatch.apply(retrievedAsJsonNode);
            var patchedAsString = jacksonMapper.writeValueAsString(patchedAsJsonNode);

            var patched = jacksonMapper.readValue(patchedAsString, entityClass);

            // Because update does not require a valid POJO We validate it ahead of time

            var violations = validator.validate(patched);

            if (!violations.isEmpty())
            {
                throw new ConstraintViolationException(violations);
            }

            return entityRepo.update(patched);
        }
        catch (Exception e)
        {
            throw new CannotPatchEntity(e, getEntityName(), id);
        }
    }

    @Transactional(Transactional.TxType.REQUIRED)
    @Override
    public void delete(@NotNull ID id) throws CannotDeleteEntity
    {
        try
        {
            entityRepo.deleteById(id);
        }
        catch (Exception e)
        {
            throw new CannotDeleteEntity(e, getEntityName(), id);
        }
    }
}
