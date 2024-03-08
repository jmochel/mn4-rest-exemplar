package org.saltations.mre.core.control;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.function.Supplier;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public final class XSuccess<VT,FT extends FailureType> implements XResult<VT, FT>
{
    private final VT value;

    @Override
    public boolean isSuccess()
    {
        return true;
    }

    @Override
    public boolean isFailure()
    {
        return false;
    }

    @Override
    public VT get() throws Throwable
    {
        return value;
    }

    @Override
    public VT orElse(VT value) {
        return this.value;
    }

    @Override
    public <X extends Exception> VT orThrow(Supplier<X> supplier) throws X
    {
        return value;
    }
}
