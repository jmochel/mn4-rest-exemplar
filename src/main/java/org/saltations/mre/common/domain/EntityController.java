package org.saltations.mre.common.domain;

import org.saltations.mre.common.domain.model.Entity;
import org.saltations.mre.common.domain.model.EntityMapper;
import org.saltations.mre.common.domain.outport.CrudEntityRepo;
import org.saltations.mre.common.domain.logic.CrudEntityService;

/**
 * Minimum contract for common functionality used within a controller that allows operations on an entity
 */

public interface EntityController<ID, IC, C extends IC, E extends Entity<ID>,
                                          ER extends CrudEntityRepo<ID, E>,
                                          EM extends EntityMapper<C, E>,
                                          ES extends CrudEntityService<ID, IC, C, E, ER, EM>>
{
    /**
     * Provides the resource name for the entity managed by the controller.
     * This is intended to be used primarily in generating error messages
     */

    default String getEntityName()
    {
        return getEntityClass().getSimpleName();
    }

    Class<E> getEntityClass();

    ES getEntityService();
}
