package org.saltations.mre.core.control;

import java.util.function.Consumer;

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

}
