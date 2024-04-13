package org.saltations.mre.core.outcomes;
import javax.naming.OperationNotSupportedException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A generic interface for outcomes of operations.
 *
 * TODO Summary(ends with '.',third person[gets the X, not Get X],do not use @link) ${NAME} represents xxx OR ${NAME} does xxxx.
 *
 * <p>TODO Description(1 lines sentences,) References generic parameters with {@code <T>} and uses 'b','em', dl, ul, ol tags
 *
 * @param <FV> Failure payload class. Accessible in failures and partial successes
 * @param <SV> Success payload class. Accessible in successes and partial successes.  
 */

public sealed interface Outcome<FV extends Fail, SV> permits Failure, Success, PartialSuccess
{
    boolean hasSuccessValue();

    boolean hasFailureValue();

    /**
     * Return the success value or throw an exception if this does not have a success value
     *
     * @return value associated with the success. May be null
     *
     * @throws OperationNotSupportedException if there is no success value available
     *
     * <p>
     * <b>Example:</b>
     * <pre>{@code
     *      return outcome.get();
     * }
     * </pre>
     */

    SV get();

    /**
     * If this outcome is a success (or partial success) retunr the supplied outcome
     *
     * @param supplier function that supplies a new outcome. Not null
     *
     * @return populated XSuccess is success, XFailure if failure.
     *
     * <p><b>Example:</b>
     * {@snippet :
     *   var newOutcome = outcome.ifSuccess(() -> Outcomes.succeed(21));
     * }
     *
     */

    Outcome<FV,SV> ifSuccess(Supplier<Outcome<FV,SV>> supplier);

    /**
     * If this outcome is a success (or partial success) apply the transform
     *
     * @param transform function that supplies a new outcome from an existing outcome. Not null
     *
     * @return transformed XSuccess if success, XFailure if failure.
     *
     * <p><b>Example:</b>
     * {@snippet :
     *   var newOutcome = outcome.ifSuccessApply(this::outcomeTransform);
     * }
     *
     */

    Outcome<FV,SV> ifSuccessTransform(Function<Outcome<FV,SV>, Outcome<FV,SV>> transform);

    /**
     * If this outcome is a success (or partial success) apply the given action
     *
     * @param action function that takes action based on success. Not null
     *
     * <p><b>Example:</b>
     * {@snippet :
     *   var newOutcome = outcome.ifSuccess(x -> log.info("{}", x.get()));
     * }
     *
     */

    void onSuccess(Consumer<Outcome<FV,SV>> action);

    /**
     * If this outcome is a failure return the supplied outcome
     *
     * @param supplier function that supplies a new outcome. Not null
     *
     * @return existing XSuccess if success, new Outcome if failure.
     *
     * <p><b>Example:</b>
     * {@snippet :
     *   var newOutcome = outcome.ifFailure(() -> Outcomes.succeed(21));
     * }
     *
     */

    Outcome<FV,SV> ifFailure(Supplier<Outcome<FV,SV>> supplier);

    /**
     * If this outcome is a failure apply the transform
     *
     * @param transform function that supplies a new outcome from an existing outcome. Not null
     *
     * @return  existing XSuccess if success, new Outcome if failure.
     *
     * <p><b>Example:</b>
     * {@snippet :
     *   var newOutcome = outcome.ifFailureApply(this::outcomeTransform);
     * }
     *
     */

    Outcome<FV,SV> ifFailureTransform(Function<Outcome<FV,SV>, Outcome<FV,SV>> transform);

    /**
     * If this outcome is a failure apply the given action
     *
     * @param action function that takes action based on failure. Not null
     *
     * <p><b>Example:</b>
     * {@snippet :
     *   var newOutcome = outcome.ifFailure(x -> log.info("{}", x.get()));
     * }
     *
     */

    void onFailure(Consumer<Outcome<FV,SV>> action);

    /**
     *
     * If this outcome is a failure apply the failure action, if success apply the success action. If partial success apply both
     *
     * @param successAction action to execute if this is a success or partial success
     * @param failureAction action to execute if this is a failure or partial success
     *
     * <p><b>Example:</b>
     * {@snippet :
     *   var newOutcome = outcome.ifFailure(x -> log.info("{}", x.get()));
     * }
     */

    void on(Consumer<Outcome<FV,SV>> successAction, Consumer<Outcome<FV,SV>> failureAction);

}
