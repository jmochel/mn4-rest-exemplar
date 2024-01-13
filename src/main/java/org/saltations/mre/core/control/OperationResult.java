package org.saltations.mre.core.control;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * Disjoint union type that can contain an ok result or an error. Roughly equivalent to the Kotlin result class.
 * This can be read an outcome that is either an <em>error result</em> or an <em>ok result</em>. An ok result could be
 * a success or an acceptable failure.
 *
 * @param <VT> The class (type) of the value that can be contained.
 * @param <ET> The class (type) of the error that can be contained.
 */

@Getter
@AllArgsConstructor
public abstract class OperationResult<VT,ET>
{
    protected final VT value;
    protected final ET errorCode;
    protected final Throwable thrown;
    protected boolean isVoid;

    public OperationResult(VT value, ET errorCode, Throwable thrown)
    {
        this.value = value;
        this.errorCode = errorCode;
        this.thrown = thrown;
        this.isVoid = false;
    }

    public OperationResult(ET errorCode, Throwable thrown)
    {
        this.value = null;
        this.errorCode = errorCode;
        this.thrown = thrown;
        this.isVoid = true;
    }

    public VT getValue()
    {
        if (isVoid)
        {
            throw new UnsupportedOperationException("Return value is Void. You cannot get this");
        }

        return value;
    }

    public boolean isError()
    {
        return this.errorCode != null || this.thrown != null;
    }

    public boolean isSuccess()
    {
        return !isError();
    }

    /**
     * Construct an OK result
     *
     * @param value Value to be returned with the result
     */

    public static <VT,ET> OperationResult<VT,ET> fulfilled(VT value)
    {
        return new Fulfillment<>(value);
    }


    public static <Void,ET> OperationResult<Void,ET> fulfilled()
    {
        return new Fulfillment<>(null);
    }

    /**
     * Construct an error outcome with an error code
     *
     * @param errorCode Value of the error code associated with the error
     */

    public static <VT,ET> OperationResult<VT,ET> error(ET errorCode)
    {
        return error(errorCode, null);
    }

    /**
     * Construct an error outcome with an exception
     *
     * @param e Throwable associated with the error
     */

    public static <VT,ET> OperationResult<VT,ET> error(Throwable e)
    {
        return error(null, e);
    }

    /**
     * Construct an error outcome with an exception and error code
     *
     * @param errorCode Value of the error code associated with the error
     * @param e Throwable associated with the error
     */

    public static <VT,ET> OperationResult<VT,ET> error(ET errorCode, Throwable e)
    {
        return new Error<>(errorCode, e);
    }

    /**
     * Construct an outcome from a supplier that can throw an exception
     *
     * @param valueSupplier Supplier function that can throw an exception
     */

    public static <VT, ET> OperationResult<VT, ET> ofThrowable(ThrowableSupplier<VT> valueSupplier)
    {
        Objects.requireNonNull(valueSupplier);

        try
        {
          return OperationResult.fulfilled(valueSupplier.get());
        }
        catch (Throwable e)
        {
            return  OperationResult.error(null,e);
        }
    }

    /**
     * Construct an outcome from an error code and a supplier that can throw an exception
     *
     * @param errorCode Value of the error code associated with the error
     * @param valueSupplier Supplier function that can throw an exception
     */

    public static <VT,ET> OperationResult<VT,ET> ofThrowable(ET errorCode, ThrowableSupplier<VT> valueSupplier) {

        Objects.requireNonNull(valueSupplier);

        try {
            return OperationResult.fulfilled(valueSupplier.get());
        }
        catch (Throwable e) {
            return OperationResult.error(errorCode,e);
        }
    }

    /**
     * Returns the given value if the operating result is an error
     *
     * @param altValue value to provide if operating result is a result
     *
     * @return value of type {@literal VT}
     */

    public VT elseValue(VT altValue)
    {
        if (isError() )
        {
            return altValue;
        }

        return this.value;
    }

    /**
     * Returns the value from the supplier if the operating result is an error
     *
     * @param valueSupplier function to provide value if operating result is a result
     *
     * @return value of type {@literal VT}
     * <p>
     * <h4>Important Note</h4>
     * Only use supplier functions that cannot throw an exception.
     * TODO Otherwise use ....
     */

    public VT elseValue(Supplier<VT> valueSupplier)
    {
        if (isError() )
        {
            return valueSupplier.get();
        }

        return this.value;
    }

    /**
     * Return if present, otherwise return the values derived from the value supplier
     */

    public OperationResult<VT,ET> orSuppliedResult(ThrowableSupplier<VT> valueSupplier)
    {
        if (isError())
        {
            return OperationResult.ofThrowable(valueSupplier);
        }

        return this;
    }

    /**
     * Return the contained value, if present, otherwise throw an exception to be created by the provided supplier
     */

    public OperationResult<VT,ET> orThrow(Supplier<? extends Exception> exceptionSupplier) throws Throwable
    {
        if (isError())
        {
            throw exceptionSupplier.get();
        }

        return this;
    }

    /**
     * If a contained value is present, apply the appropriate mapping function to it. If a contained error is present,
     * apply the appropriate mapping function to it.
     *
     * @param valueTransform Transform function that transforms the contained value to the new value type
     * @param errorTransform Transform function that transforms the contained error code  to a new error code
     */

    public abstract <VT2,ET2> OperationResult<VT2,ET2> map(ThrowableFunction<VT,VT2> valueTransform, ThrowableFunction<ET,ET2> errorTransform);

    /**
     * If contain value is present, apply the appropriate mapping function to it. If it contained error is present
     * return the newly created error outcome
     *
     * @param valueTransform Transform function that transforms the contained value to the new value type
     */

    public <VT2,ET> OperationResult<VT2,ET> mapFulfillment(ThrowableFunction<VT,VT2> valueTransform)
    {
        Objects.requireNonNull(valueTransform);

        return (OperationResult<VT2, ET>) map(valueTransform, ThrowableFunction.identity());
    }


    /**
     * If contained error is present, apply the appropriate error transform. If the contained value is present,
     * return the newly created result outcome
     *
     * @param errorTransform Transform function that transforms the contained error code  to a new error code
     */

    public <VT,ET2> OperationResult<VT,ET2> mapError(ThrowableFunction<ET,ET2> errorTransform)
    {
        Objects.requireNonNull(errorTransform);

        return (OperationResult<VT, ET2>) map(ThrowableFunction.identity(), errorTransform);
    }

    /**
     * If a contained value is present, apply the appropriate mapping function to it. If a contained error is present,
     * apply the appropriate mapping function to it.
     *
     * @param valueTransform Transform function that transforms the contained value to the new value type
     * @param errorTransform Transform function that transforms the contained error code  to a new error code
     */

    public abstract <VT2,ET2> OperationResult<VT2,ET2> flatMap(ThrowableFunction<VT, OperationResult<VT2, ET2>> valueTransform, ThrowableFunction<ET, OperationResult<VT2, ET2>> errorTransform);

    public abstract <VT2> OperationResult<VT2,ET> flatMapValue(ThrowableFunction<VT, OperationResult<VT2,ET>> valueTransform);

    public abstract <ET2> OperationResult<VT,ET2> flatMapError(ThrowableFunction<ET, OperationResult<VT, ET2>> errorTransform);
}

