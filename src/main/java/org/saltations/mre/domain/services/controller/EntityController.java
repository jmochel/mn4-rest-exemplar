package org.saltations.mre.domain.services.controller;

import org.saltations.mre.domain.model.EntityMapper;
import org.saltations.mre.domain.services.repo.EntityRepoBase;
import org.saltations.mre.domain.model.Entity;
import org.saltations.mre.domain.services.service.EntityServiceBase;

/**
 * Represents common functionality used within an entity controller
 */

public interface EntityController<ID, IC, C extends IC, E extends Entity<ID>,
                                          ER extends EntityRepoBase<ID, E>,
                                          EM extends EntityMapper<C, E>,
                                          ES extends EntityServiceBase<ID, IC, C, E, ER, EM>>
{
    /**
     * Provides the resource name for the entity managed by the controller.
     * This is intended to be used primarily in generating error messages
     */

    default String getResourceName()
    {
        return getEntityClass().getSimpleName();
    }

    Class<E> getEntityClass();

    ES getEntityService();

}
