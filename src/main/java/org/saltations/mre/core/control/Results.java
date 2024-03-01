package org.saltations.mre.core.control;

import jakarta.validation.constraints.NotEmpty;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Results
{
    public static <VT, FT extends Enum<?> > Result<VT, FT> success()
    {
        return new OpResult<VT,FT>(null, null);
    }

    public static <VT, FT extends Enum<?> > Result<VT, FT> success(VT value)
    {
        return new OpResult<VT,FT>(value, null);
    }

    public static <VT, FT extends Enum<?> > Result<VT, FT> failure(FT type)
    {
        return new OpResult<VT,FT>(null, new OpFailure<FT>(type, null,""));
    }

    public static <VT, FT extends Enum<?> > Result<VT, FT> failure(FT type, @NotEmpty String detail)
    {
        return new OpResult<VT,FT>(null, new OpFailure<FT>(type, null, detail));
    }

    public static <VT, FT extends Enum<?> > Result<VT, FT> failure(Exception cause)
    {
        return new OpResult<VT,FT>(null, new OpFailure<FT>(null, cause,""));
    }

    public static <VT, FT extends Enum<?> > Result<VT, FT> failure(String detail, Exception cause)
    {
        return new OpResult<VT,FT>(null, new OpFailure<FT>(null, cause,detail));
    }

    public static <VT, FT extends Enum<?> > Result<VT, FT> failure(FT type, Exception cause)
    {
        return new OpResult<VT,FT>(null, new OpFailure<FT>(type, cause,""));
    }

    public static <VT, FT extends Enum<?> > Result<VT, FT> failure(FT type, String detail, Exception cause)
    {
        return new OpResult<VT,FT>(null, new OpFailure<FT>(type, cause,detail));
    }

}
