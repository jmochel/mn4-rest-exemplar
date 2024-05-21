package org.saltations.mre.core.outcomes;


import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import jakarta.validation.constraints.NotNull;

/**
 * A failure outcome contains a value of type {@code FV} and no success information.
 *
 * @param <FV> Failure payload class. Accessible in failures.
 * @param <SV> Success payload class. Accessible in successes
 */

public record Failure<FV extends FailureParticulars, SV>(FV fail) implements Outcome<FV, SV>
{
    @Override
    public boolean hasSuccessValue()
    {
        return false;
    }

    @Override
    public boolean hasFailureValue()
    {
        return true;
    }

    @Override
    public SV get()
    {
        throw new IllegalStateException(fail.getTotalMessage());
    }

    public FailureType getType()
    {
        return fail.getType();
    }

    public String getDetail()
    {
        return fail.getDetail();
    }

    public String getTitle()
    {
        return fail.getTitle();
    }

    public Exception getCause()
    {
        return fail.getCause();
    }

    @Override
    public Outcome<FV, SV> ifSuccess(Supplier<Outcome<FV, SV>> supplier)
    {
        return this;
    }

    @Override
    public Outcome<FV, SV> ifSuccess(Function<Success<FV, SV>, Outcome<FV, SV>> transform)
    {
        return this;
    }

    @Override
    public void onSuccess(@NotNull Consumer<Success<FV, SV>> action)
    {
        // Do Nothing
    }

    @Override
    public Outcome<FV, SV> ifFailure(Supplier<Outcome<FV, SV>> supplier)
    {
        return supplier.get();
    }

    @Override
    public Outcome<FV, SV> ifFailure(@NotNull Function<Failure<FV, SV>, Outcome<FV, SV>> transform)
    {
        return transform.apply(this);
    }

    @Override
    public void onFailure(@NotNull Consumer<Failure<FV, SV>> action)
    {
        action.accept(this);
    }

    @Override
    public void on(@NotNull Consumer<Success<FV, SV>> successAction, @NotNull Consumer<Failure<FV, SV>> failureAction)
    {
        failureAction.accept(this);
    }

    @Override
    public String toString()
    {
        return new StringBuffer("XFailure").append("[")
                                           .append(getType().toString())
                                           .append(":")
                                           .append(getTitle())
                                           .append(":")
                                           .append(getDetail())
                                           .append("]")
                                           .toString();
    }

}
