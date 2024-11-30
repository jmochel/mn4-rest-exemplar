package org.saltations.mre.common.domain.logic;

import java.util.Optional;

import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import io.micronaut.core.annotation.NonNull;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import org.saltations.mre.common.core.outcomes.FailureParticulars;
import org.saltations.mre.common.core.outcomes.Outcome;
import org.saltations.mre.common.domain.gateway.CrudEntityRepo;
import org.saltations.mre.common.domain.model.Entity;
import org.saltations.mre.common.domain.model.EntityMapper;

/**
 * Minimum contract for the business logic (service) that provides CRUD operations on entities of type E
 *
 * @param <ID> Type of the entity identifier .
 * @param <IC> Interface of the core business concept the entity represents
 * @param <C> Class of the core object the entity represents
 * @param <E> Class of the entity.
 */

public interface CrudEntityService<ID, IC, C extends IC, E extends Entity<ID>,
        ER extends CrudEntityRepo<ID,E>,
        EM extends EntityMapper<C,E>>
{
    /**
     * Provides the entity name of the entity managed by the service.
     * Intended to be used primarily in generating error messages
     */

    String getEntityName();

    /**
     * Checks existence of entity for a given id.
     *
     * @param id Identifier. Not null.
     *
     * @return Mono for the boolean result.
     */
    @NonNull
    Boolean exists(ID id);

    /**
     * Find the entity by its identifier
     *
     * @param id Identifier. Not null.
     *
     * @return Possible result. {@link java.util.Optional#empty()} if no entity matching the id is find.
     */

    Optional<E> find(ID id);

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
    Outcome<FailureParticulars, E> create(C prototype);

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
    E update(E update) throws CannotUpdateEntity;

    /**
     * Patches an entity of type E with the contents of the given patch.
     * TODO - Should this be at the business logic level ?
     *
     * @param id is the unique identifier for the entity
     * @param mergePatch is the patch to apply to the entity. Valid and not null.
     *
     * @return patched entity.
     *
     * @throws CannotPatchEntity If the entity could not be patched for any reason
     */

    @Transactional(Transactional.TxType.REQUIRED)
    E patch(ID id, @NotNull JsonMergePatch mergePatch) throws CannotPatchEntity;

    /**
     * Deletes an entity of type E with the given ID.
     *
     * @param id is the unique identifier for the entity
     *
     * @throws CannotDeleteEntity If the entity could not be deleted for any reason
     */

    @Transactional(Transactional.TxType.REQUIRED)
    void delete(ID id) throws CannotDeleteEntity;
}
