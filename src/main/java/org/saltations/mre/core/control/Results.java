package org.saltations.mre.core.control;

import jakarta.validation.constraints.NotEmpty;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Results
{
    public static <VT, FT extends FailureType > Result<VT, FT> success()
    {
        return new OpResult<VT,FT>(null, null);
    }

    public static <VT, FT extends FailureType > Result<VT, FT> success(VT value)
    {
        return new OpResult<VT,FT>(value, null);
    }

    public static <VT, FT extends FailureType > Result<VT, FT> failure(FT type)
    {
        return new OpResult<VT,FT>(null, new OpFailure<FT>(type, null,""));
    }

    public static <VT, FT extends FailureType > Result<VT, FT> failure(FT type, @NotEmpty String detail)
    {
        return new OpResult<VT,FT>(null, new OpFailure<FT>(type, null, detail));
    }

    public static <VT, FT extends FailureType > Result<VT, FT> formattedFailure(FT type, Object...args)
    {
        return new OpResult<VT,FT>(null, new OpFailure<FT>(type, null, type.formatDetail(args)));
    }

    public static <VT, FT extends FailureType > Result<VT, FT> failure(Exception cause)
    {
        return new OpResult<VT,FT>(null, new OpFailure<FT>(null, cause,""));
    }

    public static <VT, FT extends FailureType > Result<VT, FT> failure(Exception cause, String detail)
    {
        return new OpResult<VT,FT>(null, new OpFailure<FT>(null, cause,detail));
    }

    public static <VT, FT extends FailureType > Result<VT, FT> failure(Exception cause, FT type)
    {
        return new OpResult<VT,FT>(null, new OpFailure<FT>(type, cause,""));
    }

    public static <VT, FT extends FailureType > Result<VT, FT> failure(Exception cause, FT type, String detail)
    {
        return new OpResult<VT,FT>(null, new OpFailure<FT>(type, cause,detail));
    }

    public static <VT, FT extends FailureType > Result<VT, FT> formattedFailure(Exception cause, FT type, Object...args)
    {
        return new OpResult<VT,FT>(null, new OpFailure<FT>(type, cause,type.formatDetail(args)));
    }

}
