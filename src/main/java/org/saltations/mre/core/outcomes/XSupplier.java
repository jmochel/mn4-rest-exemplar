package org.saltations.mre.core.outcomes;

/**
 * Represents a supplier of results that can throw an exception.
 *
 * <p>There is no requirement that a new or distinct result be returned each
 * time the supplier is invoked.
 *
 * <p>This is a functional interface whose functional method is {@link #get()}.
 *
 * @param <T> the type of results supplied by this supplier
 */
@FunctionalInterface
public interface XSupplier<T> {

    /**
     * Gets a result.
     *
     * @return a result
     */
    T get() throws Exception;
}
