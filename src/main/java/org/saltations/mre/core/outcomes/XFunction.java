package org.saltations.mre.core.outcomes;

@FunctionalInterface
public interface XFunction<T, R> {
    R apply(T t) throws Exception;
}
