package org.saltations.mre.common.domain;

import java.time.OffsetDateTime;

import lombok.NonNull;

/**
 * Minimum contract for an entity with basic  .
 * <p>
 * TODO Summary(ends with '.',third person[gets the X, not Get X],do not use @link) ${NAME} represents xxx OR ${NAME} does xxxx.
 *
 * <p>TODO Description(1 lines sentences,) References generic parameters with {@code <T>} and uses 'b','em', dl, ul, ol tags
 *
 */

public interface HasChangeDates
{
    OffsetDateTime getCreated();
    OffsetDateTime getUpdated();

    default boolean notUpdatedSince(@NonNull OffsetDateTime since)
    {
        return getUpdated().isAfter(since);
    }

}
