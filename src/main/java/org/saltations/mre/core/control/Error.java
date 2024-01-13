package org.saltations.mre.core.control;

import java.util.Objects;

/**
 * Represent an error result for an operation.
 *
 * @param <VT> Type of value for successes
 * @param <ET> Type of error code
 */

public final class Error<VT, ET> extends OperationResult<VT, ET>
{
    public Error(ET errorCode, Throwable thrown)
    {
        super(null, errorCode, thrown);
    }

    /**
     * Map an Error result to a new Error result using a error code to error code transform function.
     *
     * @param valueTransform Transform function that transforms the contained value to the new value
     * @param errorTransform Transform function that transforms the contained error code to a new error code
     */

    public <VT2, ET2> OperationResult<VT2, ET2> map(ThrowableFunction<VT, VT2> valueTransform, ThrowableFunction<ET, ET2> errorTransform)
    {
        Objects.requireNonNull(valueTransform);
        Objects.requireNonNull(errorTransform);

        try
        {
            return new Error<>(errorTransform.apply(super.errorCode), super.thrown);
        }
        catch (Throwable e)
        {
            return OperationResult.error(e);
        }
    }

    /**
     *  Map an Error result to a new Error result using a error code to Error result transform function.
     *
     * @param valueTransform Transform function that transforms the contained value to the new Success result
     * @param errorTransform Transform function that transforms the contained error code to an new Error result transform function.
     */

    @Override
    public <VT2, ET2> OperationResult<VT2, ET2> flatMap(ThrowableFunction<VT, OperationResult<VT2, ET2>> valueTransform, ThrowableFunction<ET, OperationResult<VT2, ET2>> errorTransform)
    {
        Objects.requireNonNull(valueTransform);
        Objects.requireNonNull(errorTransform);

        try
        {
            return errorTransform.apply(this.errorCode);
        }
        catch (Throwable e)
        {
            return OperationResult.error(e);
        }
    }

    @Override
    public <VT2> OperationResult<VT2, ET> flatMapValue(ThrowableFunction<VT, OperationResult<VT2, ET>> valueTransform)
    {
        return OperationResult.error(this.errorCode, this.thrown);
    }

    @Override
    public <ET2> OperationResult<VT, ET2> flatMapError(ThrowableFunction<ET, OperationResult<VT, ET2>> errorTransform)
    {
        try
        {
            return (OperationResult<VT, ET2>) OperationResult.error(errorTransform.apply(this.errorCode), this.thrown);
        }
        catch (Throwable e)
        {
            return OperationResult.error(e);
        }
    }
}
