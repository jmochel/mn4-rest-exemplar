package org.saltations.mre.common.core.outcomes;

import static java.util.Objects.requireNonNull;

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
        requireNonNull(before);
        return (V v) -> apply(before.apply(v));
    }

    default <V> ThrowableFunction<T, V> andThen(ThrowableFunction<? super R, ? extends V> after) throws Throwable {
        requireNonNull(after);
        return (T t) -> after.apply(apply(t));
    }

    static <T> ThrowableFunction<T, T> identity() {
        return t -> t;
    }

}
