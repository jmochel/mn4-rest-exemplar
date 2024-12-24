package org.saltations.mre.common.application;

import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.validation.validator.Validator;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.saltations.mre.common.core.outcomes.FailureParticulars;
import org.saltations.mre.common.core.outcomes.Outcome;
import org.saltations.mre.common.core.outcomes.Outcomes;
import org.saltations.mre.common.domain.Entity;
import org.saltations.mre.common.domain.EntityMapper;

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
    public Outcome<FailureParticulars, E> create(@NotNull @Valid C prototype)
    {
        E created;

        try
        {
            var toBeCreated = entityMapper.createEntity(prototype);

            created = entityRepo.save(toBeCreated);
        }
        catch (Exception e)
        {
            return Outcomes.causedFailure(e, CrudFailure.CANNOT_CREATE, getEntityName(), prototype);
        }

        return Outcomes.success(created);
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
