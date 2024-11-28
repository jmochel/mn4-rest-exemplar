package org.saltations.mre.fixtures;

import lombok.Getter;

/**
 * Foundation (provides some default functionality) for an Oracle, typically used for testing
 *
 * @param <T> Type Class of the exemplar
 */

@Getter
@SuppressWarnings("ClassHasNoToStringMethod")
public abstract class OracleFoundation<T> implements Oracle<T>
{
    private final Class<T> clazz;

    private final int initialSharedValue;

    public OracleFoundation(Class<T> clazz, int initialSharedValue)
    {
        this.clazz = clazz;
        this.initialSharedValue = initialSharedValue;
    }

}
