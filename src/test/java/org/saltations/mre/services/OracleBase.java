package org.saltations.mre.services;

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

    @Getter
    private final int initialSharedValue;

    public OracleBase(Class<T> clazz, int initialSharedValue)
    {
        this.clazz = clazz;
        this.initialSharedValue = initialSharedValue;
    }

}
