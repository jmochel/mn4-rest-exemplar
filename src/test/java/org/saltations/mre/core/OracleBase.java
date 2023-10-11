package org.saltations.mre.core;

import lombok.Getter;

/**
 * Base class for an Oracle, typically used for testing
 *
 * @param <T> Type Class of the exemplar
 */

@SuppressWarnings("ClassHasNoToStringMethod")
public abstract class OracleBase<T> implements Oracle<T>
{
    @Getter
    private final Class<T> clazz;

    public OracleBase(Class<T> clazz)
    {
        this.clazz = clazz;
    }

}