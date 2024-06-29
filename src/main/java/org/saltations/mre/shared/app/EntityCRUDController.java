package org.saltations.mre.shared.app;

import org.saltations.mre.shared.domain.model.EntityMapper;
import org.saltations.mre.shared.domain.model.EntityRepoBase;
import org.saltations.mre.shared.domain.model.Entity;
import org.saltations.mre.shared.domain.services.EntityCRUDServiceBase;

/**
 * Represents common functionality used within an entity controller
 */

public interface EntityCRUDController<ID, IC, C extends IC, E extends Entity<ID>,
                                          ER extends EntityRepoBase<ID, E>,
                                          EM extends EntityMapper<C, E>,
                                          ES extends EntityCRUDServiceBase<ID, IC, C, E, ER, EM>>
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
