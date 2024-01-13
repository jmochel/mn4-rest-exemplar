package org.saltations.mre.core.control;

import java.util.Objects;


public final class Fulfillment<VT, ET> extends OperationResult<VT, ET>
{
    public Fulfillment(VT value)
    {
      super(value, null, null);
    }

    public Fulfillment()
    {
        super(null, null);
    }

    @Override
    public OperationResult<VT, ET> orSuppliedResult(ThrowableSupplier<VT> valueSupplier)
    {
        Objects.requireNonNull(valueSupplier);
        return this;
    }

    @Override
    public <VT2, ET2> OperationResult<VT2, ET2> map(ThrowableFunction<VT, VT2> valueTransform, ThrowableFunction<ET, ET2> errorTransform)
    {
        Objects.requireNonNull(valueTransform);
        Objects.requireNonNull(errorTransform);

        try
        {
            return new Fulfillment<>(valueTransform.apply(getValue()));
        }
        catch (Throwable e)
        {
            return OperationResult.error(e);
        }
    }

    public <VT2,ET2> OperationResult<VT2,ET2> flatMap(ThrowableFunction<VT, OperationResult<VT2, ET2>> valueTransform, ThrowableFunction<ET, OperationResult<VT2, ET2>> errorTransform)
    {
        Objects.requireNonNull(valueTransform);
        Objects.requireNonNull(errorTransform);

        try
        {
            return valueTransform.apply(getValue());
        }
        catch (Throwable e)
        {
            return OperationResult.error(e);
        }
    }

    @Override
    public <VT2> OperationResult<VT2, ET> flatMapValue(ThrowableFunction<VT, OperationResult<VT2, ET>> valueTransform)
    {
        try
        {
            return valueTransform.apply(this.value);
        }
        catch (Throwable e)
        {
            return OperationResult.error(e);
        }
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
