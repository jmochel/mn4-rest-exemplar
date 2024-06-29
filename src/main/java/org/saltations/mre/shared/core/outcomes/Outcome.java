package org.saltations.mre.shared.core.outcomes;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import javax.naming.OperationNotSupportedException;

import jakarta.validation.constraints.NotNull;

/**
 * A generic interface for outcomes of an operation. Outcomes can be success or failure
 * <p>
 * A success outcome contains a value of type {@code SV} and no failure information
 * A failure outcome contains a value of type {@code FV} and failure information
 *
 * @param <FV> Failure payload class. Accessible in failures.
 * @param <SV> Success payload class. Accessible in successes
 *
 * @implSpec A null success value held by a success my never be retrieved. If you can return null, use a failure.
 * .Description of what it means to be a valid default implementation (or an overridable implementation in a class), such as "throws UOE." Similarly this is where we'd describe what the default for putIfAbsent does. It is from this box that the would-be-implementer gets enough information to make a sensible decision as to whether or not to override.
 * @ImplNote This interface is sealed and permits only the {@link Failure} and {@link Success} classes to implement it.
 * The only methods that should be added to thii interface are those that are common to both success and failure
 * outcomes and can be formed with the data contained in success or failure.
 */

public sealed interface Outcome<FV extends FailureParticulars, SV> permits Failure, Success
{
    boolean hasSuccessValue();
    boolean hasFailureValue();

    /**
     * Return the contained success value or throw an exception if this is a failure outcome
     *
     * @return value associated with the success. May be null
     *
     * @throws OperationNotSupportedException if this is a failure outcome
     *
     *                                        <p>
     *                                        <b>Example:</b>
     *                                        <pre>{@snippet :
     *                                             return outcome.get();
     *}
     *                                        </pre>
     * @apiNote - Commentary, rationale, or examples pertaining to the API.
     * @implSpec - Description of what it means to be a valid default implementation (or an overridable implementation in a class), such as "throws UOE." Similarly this is where we'd describe what the default for putIfAbsent does. It is from this box that the would-be-implementer gets enough information to make a sensible decision as to whether or not to override.
     * @implNote - Informative notes about the implementation, such as performance characteristics that are specific to the implementation in this class in this JDK in this version, and might change. These things are allowed to vary across platforms, vendors and versions.
     */

    Optional<SV> potentialSuccessValue();

    /**
     * Return the contained success value or throw an exception if this is a failure outcome
     *
     * @return raw value associated with the success. May be null
     *
     * @throws OperationNotSupportedException if this is a failure outcome
     *
     *                                        <p>
     *                                        <b>Example:</b>
     *                                        <pre>{@snippet :
     *                                             return outcome.get();
     *}
     *                                        </pre>
     * @apiNote - Commentary, rationale, or examples pertaining to the API.
     * @implSpec - Description of what it means to be a valid default implementation (or an overridable implementation in a class), such as "throws UOE." Similarly this is where we'd describe what the default for putIfAbsent does. It is from this box that the would-be-implementer gets enough information to make a sensible decision as to whether or not to override.
     * @implNote - Informative notes about the implementation, such as performance characteristics that are specific to the implementation in this class in this JDK in this version, and might change. These things are allowed to vary across platforms, vendors and versions.
     */

    SV rawSuccessValue();

    /**
     * Provide a new outcome if there is an existing success.
     *
     * @param supplier function that supplies a new outcome. Not null
     * @return new Outcome or the existing outcome (on success).
     *
     * <p><b>Example:</b>
     * {@snippet :
     *   var newOutcome = outcome.ifSuccess(() -> Outcomes.success(21));
     *}
     */

    Outcome<FV,SV> ifSuccess(@NotNull Supplier<Outcome<FV,SV>> supplier);

    /**
     * Provide a new outcome if there is an existing failure
     *
     * @param supplier function that supplies a new outcome. Not null
     * @return new Outcome or the existing outcome (on failure).
     *
     * <p><b>Example:</b>
     * {@snippet :
     *   var newOutcome = outcome.ifFailure(() -> Outcomes.success(21));
     *}
     */

    Outcome<FV,SV> ifFailure(@NotNull Supplier<Outcome<FV,SV>> supplier);

    /**
     * Provide an outcome based on an existing success.
     *
     * @param transform function that turns an existing success into a new outcome. Not null
     * @return new outcome based on existing success or existing outcome if failure.
     *
     * <p><b>Example:</b>
     * {@snippet :
     *   var newOutcome = outcome.ifSuccess(this::outcomeTransform);
     * }
     */

    Outcome<FV,SV> ifSuccess(@NotNull Function<Success<FV, SV>, Outcome<FV, SV>> transform);

    /**
     * Provide an outcome based on an existing failure.
     *
     * @param transform function that turns an existing failure into a new outcome. Not null
     * @return new outcome based on existing failure or existing outcome if success.
     *
     * <p><b>Example:</b>
     * {@snippet :
     *   var newOutcome = outcome.ifFailure(this::outcomeTransform);
     * }
     */

    Outcome<FV,SV> ifFailure(@NotNull Function<Failure<FV, SV>, Outcome<FV, SV>> transform);

    /**
     * Takes action based on an existing success, nothing if a failure
     *
     * @param action function that takes action based on success. Not null
     *
     * <p><b>Example:</b>
     * {@snippet :
     *   var newOutcome = outcome.onSuccess(x -> log.info("{}", x.get()));
     * }
     */

    void onSuccess(@NotNull Consumer<Success<FV, SV>> action);

    /**
     * Takes action based on an existing failure, nothing if a success
     *
     * @param action function that takes action based on failure. Not null
     *
     * <p><b>Example:</b>
     * {@snippet :
     *   var newOutcome = outcome.onFailure(x -> log.info("{}", x.get()));
     * }
     */

    void onFailure(@NotNull Consumer<Failure<FV, SV>> action);

    /**
     * Takes action based on an existing success or failure
     *
     * @param successAction function that takes action based on success. Not null
     * @param failureAction function that takes action based on failure. Not null
     *
     * <p><b>Example:</b>
     * {@snippet :
     *   var newOutcome = outcome.on(x -> log.info("{}", x.get()), x -> log.error("{}", x.getDetails()));
     * }
     */

    void on(@NotNull Consumer<Success<FV, SV>> successAction, @NotNull Consumer<Failure<FV, SV>> failureAction);

}
