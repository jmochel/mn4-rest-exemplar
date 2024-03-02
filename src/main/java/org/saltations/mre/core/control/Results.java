package org.saltations.mre.core.control;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Results
{
    @Getter
    @AllArgsConstructor
    public enum GenericFailure implements FailureType {

        GENERAL("Uncategorized error","");

        private final String title;
        private final String detailTemplate;
    }

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

    public static <VT> Result<VT, GenericFailure> failure(Exception cause)
    {
        return new OpResult<VT,GenericFailure>(null, new OpFailure<GenericFailure>(GenericFailure.GENERAL, cause,""));
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

    /**
     * Turns a given value supplier of type {@code <VT>}  that can throw an exception to a Result
     *
     * @param supplier Supplies value or throws exception
     * @return Result with value or failure with cause
     *
     * @param <VT> Type of teh value that can be returned by the supplier
     */

    public static <VT> Result<VT, Results.GenericFailure> fromThrowable(ThrowableSupplier<VT> supplier)
    {
        Result<VT, Results.GenericFailure> result;

        try
        {
            var value  = supplier.get();
            result = success(value);
        }
        catch (Throwable e)
        {
            if (e instanceof Exception)
            {
                result = failure( (Exception) e, Results.GenericFailure.GENERAL);
            }
            else {
                result = failure( new Exception(e), Results.GenericFailure.GENERAL);
            }
        }

        return result;
    }

}
