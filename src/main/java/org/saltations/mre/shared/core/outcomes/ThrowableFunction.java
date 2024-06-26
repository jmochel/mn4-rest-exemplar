package org.saltations.mre.shared.core.outcomes;

import java.util.Objects;

/**
 * This is similar to the Java Function type excepts that it allows for throwing exceptions and errors which allows it to be used in Result monads
 *
 * @param <T> Type of the input
 * @param <R> Type of the output
 */

public interface ThrowableFunction<T,R>
{

    R apply(T t) throws Throwable;

    default <V> ThrowableFunction<V, R> compose(ThrowableFunction<? super V, ? extends T> before) throws Throwable {
        Objects.requireNonNull(before);
        return (V v) -> apply(before.apply(v));
    }

    default <V> ThrowableFunction<T, V> andThen(ThrowableFunction<? super R, ? extends V> after) throws Throwable {
        Objects.requireNonNull(after);
        return (T t) -> after.apply(apply(t));
    }

    static <T> ThrowableFunction<T, T> identity() {
        return t -> t;
    }

}
