package org.saltations.mre.core.control;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * TODO Summary(ends with '.',third person[gets the X, not Get X],do not use @link) XResults represents xxx OR XResults does xxxx.
 *
 * <p>TODO Description(1 lines sentences,) References generic parameters with {@code <T>} and uses 'b','em', dl, ul, ol tags
 */

public class XResults
{
    @Getter
    @AllArgsConstructor
    public enum GenericFailure implements FailureType {

        GENERAL("Uncategorized error","");

        private final String title;
        private final String detailTemplate;
    }

    public static <VT, FT extends FailureType > XResult<Void,Results.GenericFailure> success()
    {
        return new XSuccess(null);
    }

    public static <VT, FT extends FailureType > XResult<VT, FT> success(VT value)
    {
        return new XSuccess(value);
    }

    public static <VT,FT extends FailureType> XResult<VT, FT> failure(FT failureType, Object...args)
    {
        return new XFailure(null, failureType, args);
    }

    public static <VT,FT extends FailureType> XResult<VT, XResults.GenericFailure> failure(Exception cause, FT failureType, Object...args)
    {
        return new XFailure(cause, failureType, args);
    }

    public static <VT,FT extends FailureType> XResult<VT, XResults.GenericFailure> failure(Exception cause)
    {
        return new XFailure(cause, XResults.GenericFailure.GENERAL, new Object[]{});
    }

    public static <VT,FT extends FailureType> XResult<VT, XResults.GenericFailure> failure(Exception cause, Object...args)
    {
        return new XFailure(cause, XResults.GenericFailure.GENERAL, args);
    }


    public static <VT,FT extends FailureType> XResult<VT, FT> ofThrowable(ThrowableSupplier<VT> supplier)
    {
        XResult<VT, FT> result;

        try
        {
            var value  = supplier.get();
            result = success(value);
        }
        catch (Throwable e)
        {
            if (e instanceof Exception)
            {
                result = new XFailure( (Exception) e, XResults.GenericFailure.GENERAL);
            }
            else {
                result = new XFailure( new Exception(e), XResults.GenericFailure.GENERAL);
            }
        }

        return result;
    }
}
