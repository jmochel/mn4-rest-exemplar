package org.saltations.mre.core.control;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

/**
 * An operation result where the absence of a failure indicates success.
 *
 * May contain a value (could be null) OR a failure.
 * If a failure is present, {@code isSuccess()} returns {@code false}
 *
 *
 * @param <VT> the type of the potential contained value
 * @param <FT> the type of the potential contained failure code
 */

public interface Result<VT, FT extends FailureType>
{
    default boolean isSuccess()
    {
        return getFailure() == null;
    }

    default boolean isFailure()
    {
        return getFailure() != null;
    }

    Failure<FT> getFailure();

    VT getValue();

    default VT orThrow() throws Exception
    {
        if (isFailure())
        {
            if (getFailure().hasCause())
            {
                throw getFailure().getCause();
            }
            else {
                throw new Exception(getFailure().getCause());
            }
        }

        return getValue();
    }

    /**
     * Suppplies a new value if the current result is success;
     *
     * @param supplier An action to execute.
     *
     * @return A result of the executed action, or the current result if it failed.
     */
    default Result<VT,FT> ifSuccess(Supplier<Result<VT,FT>> supplier) {

        requireNonNull(supplier);

        if (isSuccess()) {
            return supplier.get();
        }
        return this;
   }

}
