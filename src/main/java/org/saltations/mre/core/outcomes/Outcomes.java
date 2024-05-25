package org.saltations.mre.core.outcomes;

import jakarta.validation.constraints.NotNull;

import static java.util.Objects.requireNonNull;

/**
 * Factory for constructing outcomes
 * <p>
 * Provides methods to construct successful and failed outcomes from various actions and data.
 */

public class Outcomes
{
    /**
     * Provides a success outcome with a success value of {@code Boolean.TRUE}
     *
     * @return A Success result of {@code Outcome<FV,Boolean>}
     *
     * <p>
     * <b>Example:</b>
     * <pre>{@snippet :
     *   var success = XResults.success();
     * }
     * </pre>
     */

    public static <FV extends FailureParticulars, SV> Outcome<FV,SV> success()
    {
        return (Outcome<FV, SV>) new Success<>(Boolean.TRUE);
    }

    /**
     * Provides a success outcome with a given success value
     *
     * @param <FV> class of the contained failure
     * @param <SV> class of the contained success value
     *
     * @return A Success result of {@code Outcome<FV,SV>}
     *
     * <p>
     * <b>Example:</b>
     * <pre>{@code
     *   var success = Outcomes.success("Success!");
     * }
     * </pre>
     */

    public static <FV extends FailureParticulars, SV> Outcome<FV,SV> success(SV value)
    {
        return new Success(value);
    }

    /**
     * Provides a failure outcome with a basic failure type and generic failure title and details
     *
     * @return A Failure result of {@code Failure<FailureParticulars, SV>}
     *
     * <p>
     * <b>Example:</b>
     * <pre>{@snippet :
     *   var failure = Outcomes.failure();
     * }
     * </pre>
     */
    public static <FV extends FailureParticulars, SV> Outcome<FV,SV> fail()
    {
        var type = BasicFailureType.GENERIC;

        return new Failure(FailureParticulars.of()
                .type(type)
                .build());
    }

    /**
     * Provides a basic failure outcome with details derived from the given template and detail arguments
     *
     * @param template Message template composed using {@link org.slf4j.helpers.MessageFormatter} format strings
     * @param args     arguments used to expand the given template
     *
     * @param <FV>     class of the contained failure
     * @param <SV>     class of the contained success value
     *
     * @return A failure result of {@code Failure<FV,SV>} with failure type {@code BasicFailureType.GENERIC} and a
     * detail message derived from the template and arguments
     *
     * <p>
     * <b>Example:</b>
     * <pre> {@snippet :
     *   return Outcomes.failureWith("Ouch ! That {}", "hurt a lot.");
     *}
     * </pre>
     */

    public static <FV extends FailureParticulars, SV> Failure<FV, SV> failureWith(@NotNull String template, Object...args)
    {
        requireNonNull(template, "Failure needs a non-null template");

        var failureType = BasicFailureType.GENERIC;

        var fail = FailureParticulars.of()
                .type(failureType)
                .template(template)
                .args(args)
                .build();

        return new Failure(fail);
    }


    /**
     * Provides a basic failure outcome with the given title
     *
     * @param title    Title for the failure
     *
     * @param <FV>     class of the contained failure
     * @param <SV>     class of the contained success value
     *
     * @return A failure result of {@code Failure<FV,SV>} with failure type {@code BasicFailureType.GENERIC} and the given title
     *
     * <p>
     * <b>Example:</b>
     * <pre> {@snippet :
     *   return Outcomes.titledFailure("Completely avoidable failure.");
     *}
     * </pre>
     */

    public static <FV extends FailureParticulars, SV> Outcome<FV,SV> titledFailure(String title)
    {
        requireNonNull(title, "Failure needs a non-null title");

        var failureType = BasicFailureType.GENERIC;

        var fail = FailureParticulars.of()
                .type(failureType)
                .title(title)
                .build();

        return new Failure(fail);
    }

    /**
     * Provides a basic failure outcome with details derived from the given template and detail arguments
     *
     * @param title    Title for the failure. Not null.
     * @param template Message template composed using {@link org.slf4j.helpers.MessageFormatter} format strings. Not null.
     * @param args     arguments used to expand the given template
     *
     * @param <FV>     class of the contained failure
     * @param <SV>     class of the contained success value
     *
     * @return A failure result of {@code Failure<FV,SV>} with failure type {@code BasicFailureType.GENERIC} and a
     * detail message derived from the template and arguments
     *
     * <p>
     * <b>Example:</b>
     * <pre> {@snippet :
     *   return Outcomes.titledFailureWith("pretty-bad-error", "Ouch ! That {}", "hurt a lot.");
     *}
     * </pre>
     */

    public static <FV extends FailureParticulars, SV> Outcome<FV,SV> titledFailureWith(String title, String template, Object...args)
    {
        requireNonNull(title, "Failure needs a non-null title");
        requireNonNull(template, "Failure needs a non-null template");

        var failureType = BasicFailureType.GENERIC;

        var fail = FailureParticulars.of()
                .type(failureType)
                .title(title)
                .template(template)
                .args(args)
                .build();

        return new Failure(fail);
    }


    /**
     * Provides a failure with title and template from the FailureType details from the template and given arguments
     *
     * @param failureType  Type of the failure to be created
     * @param args     arguments used to expand the failureType's template
     *
     * @param <FV>     class of the contained failure
     * @param <SV>     class of the contained success value
     *
     * @return A failure result of {@code Failure<FV,SV>} with given failure type, derived title and template and expanded
     * detail message
     *
     * <p>
     * <b>Example:</b>
     * <pre> {@snippet :
     *   return Outcomes.typedFailure(SpecializedFailureType.SPECIAL_ERROR_1, "argument 1", "argument 2");
     *}
     * </pre>
     */


    public static <FV extends FailureParticulars, SV> Outcome<FV,SV> typedFailure(FailureType failureType, Object...args)
    {
        requireNonNull(failureType, "Failure needs a non-null failure type");

        var builder = FailureParticulars.of().type(failureType);

        if (failureType.templateParameterCount() == 0 && args.length == 1)
        {
            builder.template((String)args[0]);
        }
        else {
            builder.args(args);
        }

        var fail = builder.build();

        return new Failure(fail);
    }

    /**
     * Provides a failure with title from the FailureType, using the given template and arguments for the detail message
     *
     * @param failureType  Type of the failure to be created
     * @param args     arguments used to expand the failureType's template
     *
     * @param <FV>     class of the contained failure
     * @param <SV>     class of the contained success value
     *
     * @return A failure result of {@code Failure<FV,SV>} with given failure type, derived title and template and expanded
     * detail message
     *
     * <p>
     * <b>Example:</b>
     * <pre> {@snippet :
     *   return Outcomes.typedFailWith(SpecializedFailureType.SPECIAL_ERROR_1, "Special details : {} happened {}", "This Oopsie", "once");
     *}
     * </pre>
     */

    public static <FV extends FailureParticulars, SV> Outcome<FV,SV> typedFailWith(FailureType failureType, String template, Object...args)
    {
        requireNonNull(failureType, "Failure needs a non-null failure type");
        requireNonNull(template, "Failure needs a non-null template");


        var fail = FailureParticulars.of()
                .type(failureType)
                .template(template)
                .args(args)
                .build();

        return new Failure(fail);
    }

    /**
     * Provides a basic `caused` failure with the given cause and generic title and message and
     *
     * @param cause    the cause of this failure
     *
     * @param <FV>     class of the contained failure
     * @param <SV>     class of the contained success value
     *
     * @return A failure result of {@code Failure<FV,SV>} with a cause and generic failure type amd title
     *
     * <p>
     * <b>Example:</b>
     * <pre> {@snippet :
     *   return Outcomes.causedFailure(e);
     *}
     * </pre>
     */

    public static <FV extends FailureParticulars, SV> Outcome<FV,SV> causedFailure(Exception cause)
    {
        var failureType = BasicFailureType.GENERIC;

        var fail = FailureParticulars.of()
                .type(failureType)
                .cause(cause)
                .build();

        return new Failure(fail);
    }

    /**
     * Provides a `caused` failure with using the given template and arguments for the detail message
     *
     * @param cause    the cause of this failure
     * @param template Message template composed using {@link org.slf4j.helpers.MessageFormatter} format strings
     * @param args     arguments used to expand the given template
     *
     * @param <FV>     class of the contained failure
     * @param <SV>     class of the contained success value
     *
     * @return A failure result of {@code Failure<FV,SV>} with given failure type, derived title and template and expanded
     * detail message
     *
     * <p>
     * <b>Example:</b>
     * <pre> {@snippet :
     *   return Outcomes.causedFailureWith(SpecializedFailureType.SPECIAL_ERROR_1, "Special details : {} happened {}", "This Oopsie", "once");
     *}
     * </pre>
     */

    public static <FV extends FailureParticulars, SV> Outcome<FV,SV> causedFailureWith(Exception cause, String template, Object...args)
    {
        requireNonNull(template, "Failure needs a non-null template");

        var failureType = BasicFailureType.GENERIC;

        var fail = FailureParticulars.of()
                .type(failureType)
                .cause(cause)
                .template(template)
                .args(args)
                .build();

        return new Failure(fail);
    }

    /**
     * Provides typed `caused` failure with the given cause and given failure type
     *
     * @param cause    the cause of this failure
     * @param failureType  Type of the failure to be created
     * @param args     arguments used to expand the failureType's template
     *
     * @param <FV>     class of the contained failure
     * @param <SV>     class of the contained success value
     *
     * @return A failure result of {@code Failure<FV,SV>} with a cause and generic failure type amd title
     *
     * <p>
     * <b>Example:</b>
     * <pre> {@snippet :
     *   return Outcomes.causedFailure(e,SpecializedFailureType.SPECIAL_ERROR_1, "argument 1", "argument 2");
     *}
     * </pre>
     */

    public static <FV extends FailureParticulars, SV> Outcome<FV,SV> causedFailure(Exception cause, FailureType failureType, Object...args)
    {
        requireNonNull(failureType, "Failure needs a non-null failure type");

        var builder = FailureParticulars.of().type(failureType).cause(cause);

        if (failureType.templateParameterCount() == 0 && args.length == 1)
        {
            builder.template((String)args[0]);
        }
        else {
            builder.args(args);
        }

        var fail = builder.build();

        return new Failure(fail);
    }

}
