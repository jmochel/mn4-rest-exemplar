package org.saltations.mre.core;

/**
 * Represents an identifiable entity with an identifier of the specified type.
 *
 * @param <ID> Type of the entity identifier
 */

public interface Entity<ID>
{
    ID getId();

    void setId(ID id);
}
