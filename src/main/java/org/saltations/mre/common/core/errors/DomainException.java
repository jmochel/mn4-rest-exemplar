package org.saltations.mre.common.core.errors;

import java.util.UUID;

import io.micronaut.serde.annotation.Serdeable;
import lombok.Getter;

/**
 * A business domain error. This is an unchecked exception that indicates that an exceptional event has happened.
 * This exists separately from the Outcomes that are used to indicate the result of a business operation.
 * They may be carried by the Outcomes but the two should not have any dependencies on each other.
 */

@Serdeable
public class DomainException extends FormattedUncheckedException
{
     private static final long serialVersionUID = 1L;

    /**
     * Tracer id for the exception. This is used to track the exception through the system from generation to where it is logged.
     */

    @Getter
    private UUID traceId = UUID.randomUUID();

    public DomainException(String msg, Object... args)
    {
        super(msg, args);
    }

    public DomainException(Throwable e, String msg, Object... args)
    {
        super(e, msg, args);
    }

}
