package org.saltations.mre.common.presentation;

import org.saltations.mre.common.domain.Entity;
import org.saltations.mre.common.domain.EntityMapper;
import org.saltations.mre.common.application.CrudEntityRepo;
import org.saltations.mre.common.application.CrudEntityService;

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
