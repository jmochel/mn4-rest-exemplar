package org.saltations.mre.domain.core.outcomes;

/**
 * This is similar to the Java Supplier function type. It has a checked exception on it to allow it to
 * be used in lambda expressions on the outcome methods.
 *
 * @param <T> Type of the supplied value
 */

public interface ThrowableSupplier<T>
{
    T get() throws Throwable;
}
