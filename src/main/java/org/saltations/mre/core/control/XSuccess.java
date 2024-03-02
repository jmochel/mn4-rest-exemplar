package org.saltations.mre.core.control;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

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

}
