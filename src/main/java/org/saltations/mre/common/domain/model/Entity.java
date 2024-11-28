package org.saltations.mre.common.domain.model;

/**
 * Minimum contract for an entity with a unique identifier of the specified type.
 *
 * @param <ID> Type of the identifier
 */

public interface Entity<ID>
{
    ID getId();

    void setId(ID id);
}
