package org.saltations.mre.core.control;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * OpFailure is a class that represents a failure of an operation
 *
 * <p>Provides a concrete implementation of {@link Failure}
 *
 * @param <FT> Enumerated type of the failure.
 */

@Getter
@AllArgsConstructor
class OpFailure<FT extends FailureType> implements Failure<FT>
{
    private final FT type;
    private final Exception cause;

    @NotNull
    private final String detail;
}
