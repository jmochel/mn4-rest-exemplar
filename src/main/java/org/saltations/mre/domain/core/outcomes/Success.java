package org.saltations.mre.domain.core.outcomes;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import jakarta.validation.constraints.NotNull;

/**
 * A success outcome contains a value of type {@code SV} and no failure information.
 *
 * @param value the value of the success.
 *
 * @param <FV> Failure payload class. Accessible in failures.
 * @param <SV> Success payload class. Accessible in successes
 */

public record Success<FV extends FailureParticulars, SV>(SV value) implements Outcome<FV, SV>
{
    @Override
    public boolean hasSuccessValue()
    {
        return true;
    }

    @Override
    public boolean hasFailureValue()
    {
        return false;
    }

    @Override
    public Optional<SV> potentialSuccessValue()
    {
        return Optional.ofNullable(value);
    }

    @Override
    public SV rawSuccessValue()
    {
        return value;
    }

    @Override
    public Outcome<FV, SV> ifSuccess(Supplier<Outcome<FV, SV>> supplier)
    {
        return supplier.get();
    }

    @Override
    public Outcome<FV, SV> ifSuccess(@NotNull Function<Success<FV, SV>, Outcome<FV, SV>> transform)
    {
        return transform.apply(this);
    }

    @Override
    public void onSuccess(@NotNull Consumer<Success<FV, SV>> action)
    {
        action.accept(this);
    }

    @Override
    public Outcome<FV, SV> ifFailure(Supplier<Outcome<FV, SV>> supplier)
    {
        return this;
    }

    @Override
    public Outcome<FV, SV> ifFailure(@NotNull Function<Failure<FV, SV>, Outcome<FV, SV>> transform)
    {
        return this;
    }

    @Override
    public void onFailure(@NotNull Consumer<Failure<FV, SV>> action)
    {
        // Do Nothing
    }

    @Override
    public void on(@NotNull Consumer<Success<FV, SV>> successAction, @NotNull Consumer<Failure<FV, SV>> failureAction)
    {
        successAction.accept(this);
    }

    public String toString()
    {
        return new StringBuffer("XSuccess").append("[")
                                           .append(value)
                                           .append("]")
                                           .toString();
    }
}
