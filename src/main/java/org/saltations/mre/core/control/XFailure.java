package org.saltations.mre.core.control;

import io.micronaut.data.intercept.async.FindAllAsyncInterceptor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

@Getter
@Slf4j
@EqualsAndHashCode
public final class XFailure<VT, FT extends FailureType> implements XResult<VT, FT>
{
    private final Exception cause;

    /**
     * THe type will always be populated
    */

    private final FT type;

    private final String detail;

    public XFailure(Exception cause, FT type, Object...args)
    {
        this.cause = cause;
        this.type = type;

        //
        // Populate detail
        //

        var numOfExpectedParams = (int) type.detailTemplateParamCounts();
        var numOfArgs = args.length;

        if (numOfExpectedParams == 0)
        {
            //  No params are expected
            //      args == 0, detail template is detail
            //      args == 1, arg is detail
            //      args > 1, arg[0] is template, rest are args

            this.detail = switch(numOfArgs) {
                case 0 -> type.getDetailTemplate();
                case 1 -> (String) args[0];
                default -> org.slf4j.helpers.MessageFormatter.basicArrayFormat((String) args[0], Arrays.copyOfRange(args, 1, args.length - 1));
            };

        }
        else {

            //  N params are expected
            //      args == expected params, expanded template is detail
            //      args != expected params, log warned AND expanded template is detail

            if (numOfArgs != numOfExpectedParams)
            {
                log.warn("Failure Type [{}] Was created with an incorrect number of parameters for the details. Expected {}, were given {}. We will still create the details from it in the hopes that something makes sense.");
            }

            this.detail = type.formatDetail(args);
        }
    }

    @Override
    public boolean isSuccess()
    {
        return false;
    }

    @Override
    public boolean isFailure()
    {
        return true;
    }

    public boolean hasCause()
    {
        return cause != null;
    }

    @Override
    public VT get() throws Throwable
    {
        if (cause != null)
        {
            throw new XResultException(cause, type);
        }

        throw new XResultException(type);
    }

    @Override
    public VT orElse(VT value) {
        return value;
    }

    @Override
    public <X extends Exception> VT orThrow(Supplier<X> supplier) throws X
    {
        throw supplier.get();
    }
}
