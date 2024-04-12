package org.saltations.mre.core.outcomes;
import static java.util.Objects.requireNonNull;

public class Outcomes
{
    /**
     * Construct a successful result with a Boolean value of true
     *
     * @param <FV> class of the contained failure
     * @param <SV> class of the contained success value
     *
     * @return An XSuccess result of {@code XResult<XFail,Boolean>}
     *
     * <p>
     * <b>Example:</b>
     * <pre>{@code
     *   return XResults.succeed();
     * }
     * </pre>
     *
     */

    // TODO Make explicitly Boolean ?
    public static <FV extends XFail, SV> Outcome<FV,SV> succeed()
    {
        return (Outcome<FV, SV>) new XSuccess<>(Boolean.TRUE);
    }

    /**
     * Construct a successful result with given value
     *
     * @param <FV> class of the contained failure
     * @param <SV> class of the contained success value
     *
     * @return An XSuccess result of {@code XResult<XFail,SV>}
     *
     * <p>
     * <b>Example:</b>
     * <pre>{@code
     *   return XResults.succeed("Success!");
     * }
     * </pre>
     *
     */
    public static <FV extends XFail, SV> Outcome<FV,SV> succeed(SV value)
    {
        requireNonNull(value, "Result must have a non-null value to return");

        return new XSuccess(value);
    }

    /**
     * Construct a successful result with given value
     *
     * @param <FV> class of the contained failure
     * @param <SV> class of the contained success value
     *
     * @return An XSuccess result of {@code XResult<XFail,SV>}
     *
     * <p>
     * <b>Example:</b>
     * <pre>{@code
     *   return XResults.succeed("Success!");
     * }
     * </pre>
     *
     */
    public static <FV extends XFail, SV> Outcome<FV,SV> partialSucceed(FV failureValue , SV successValue)
    {
        requireNonNull(successValue, "Result must have a non-null success value to return");
        requireNonNull(failureValue, "Result must have a non-null failure value to return");

        return new XPartialSuccess<>(failureValue, successValue);
    }

    /**
     * Construct a failed generic result
     *
     * @param <FV> class of the contained failure
     * @param <SV> class of the contained success value
     *
     * @return An XFailure result of {@code XFailure<XFail,SV>}
     *
     * <p>
     * <b>Example:</b>
     * <pre>{@code
     *   return XResults.fail();
     * }
     * </pre>
     */
    public static <FV extends XFail, SV> Outcome<FV,SV> fail()
    {
        var type = XFail.GenericFail.GENERIC;

        return new XFailure(XFail.of()
                .type(type)
                .build());
    }


    /**
     * Construct a failed result with details from the given template and arguments
     *
     * @param template Message template composed using {@link org.slf4j.helpers.MessageFormatter} format strings
     * @param args arguments used to expand the given template
     *
     * @param <FV> class of the contained failure
     * @param <SV> class of the contained success value
     *
     * @return An XFailure result of {@code XFailure<XFail,SV>} with type {@code XFail.GenericFail.GENERIC} and a
     * detail message derived from the template and arguments
     *
     * <p>
     * <b>Example:</b>
     * <pre>{@code
     *   return XResults.failWithDetails("Ouch ! That {}", "hurt a lot.");
     * }
     * </pre>
     */

    public static <FV extends XFail, SV> Outcome<FV,SV> failWithDetails(String template, Object...args)
    {
        requireNonNull(template, "Failure needs a non-null template");

        var failureType = XFail.GenericFail.GENERIC;

        var fail = XFail.of()
                .type(failureType)
                .template(template)
                .args(args)
                .build();

        return new XFailure(fail);
    }


    /**
     * Construct a failed titled result.
     *
     * @param <FV> class of the contained failure
     * @param <SV> class of the contained success value
     *
     * @return An XFailure result of {@code XFailure<XFail,SV>}
     *
     * <p>
     * <b>Example:</b>
     * <pre>{@code
     *   return XResults.titledFail("just-plain-dumb");
     * }
     * </pre>
     */
    public static <FV extends XFail, SV> Outcome<FV,SV> titledFail(String title)
    {
        var failureType = XFail.GenericFail.GENERIC;

        var fail = XFail.of()
                .type(failureType)
                .title(title)
                .build();

        return new XFailure(fail);
    }


    /**
     * Construct a failed result with title and expanded details
     *
     * @param <FV> class of the contained failure
     * @param <SV> class of the contained success value
     *
     * @return An XFailure result of {@code XFailure<XFail,SV>}
     *
     * <p>
     * <b>Example:</b>
     * <pre>{@code
     *   return XResults.fail();
     * }
     * </pre>
     */
    public static <FV extends XFail, SV> Outcome<FV,SV> titledFailWithDetails(String title, String template, Object...args)
    {
        var failureType = XFail.GenericFail.GENERIC;

        var fail = XFail.of()
                .type(failureType)
                .title(title)
                .template(template)
                .args(args)
                .build();

        return new XFailure(fail);
    }


    public static <FV extends XFail, SV> Outcome<FV,SV> typedFail(XFailType failureType, Object...args)
    {
        requireNonNull(failureType, "Failure needs a non-null failure type");

        var builder = XFail.of().type(failureType);

        if (failureType.templateParameterCount() == 0 && args.length == 1)
        {
            builder.template((String)args[0]);
        }
        else {
            builder.args(args);
        }

        var fail = builder.build();

        return new XFailure(fail);
    }


    public static <FV extends XFail, SV> Outcome<FV,SV> typedFailWithDetails(XFailType failureType, String template, Object...args)
    {
        requireNonNull(failureType, "Failure needs a non-null failure type");

        var fail = XFail.of()
                .type(failureType)
                .template(template)
                .args(args)
                .build();

        return new XFailure(fail);
    }

    public static <FV extends XFail, SV> Outcome<FV,SV> causedFail(Exception cause)
    {
        var failureType = XFail.GenericFail.GENERIC;

        var fail = XFail.of()
                .type(failureType)
                .cause(cause)
                .build();

        return new XFailure(fail);
    }

    public static <FV extends XFail, SV> Outcome<FV,SV> causedFail(Exception cause, XFailType failureType, Object...args)
    {
        requireNonNull(failureType, "Failure needs a non-null failure type");

        var builder = XFail.of().type(failureType).cause(cause);

        if (failureType.templateParameterCount() == 0 && args.length == 1)
        {
            builder.template((String)args[0]);
        }
        else {
            builder.args(args);
        }

        var fail = builder.build();

        return new XFailure(fail);
    }

    public static <FV extends XFail, SV> Outcome<FV,SV> causedFailWithDetails(Exception cause, String template, Object...args)
    {
        var failureType = XFail.GenericFail.GENERIC;

        var fail = XFail.of()
                .type(failureType)
                .cause(cause)
                .template(template)
                .args(args)
                .build();

        return new XFailure(fail);
    }

}
