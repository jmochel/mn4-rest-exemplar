package org.saltations.mre.core;

/**
 * Interface represents an identifiable entity
 *
 * @param <ID> Type of the identifier
 */

public interface Entity<ID>
{
    ID getId();

    void setId(ID id);
}
