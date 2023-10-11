package org.saltations.mre.core;

/**
 * Represents an Oracle that provides exemplar objects of class T
 */

public interface Oracle<T>
{
    /**
     * Returns a prototype object of class T
     */

    T prototype();

    /**
     * Returns an object of class T with all attributes changed from the prototype
     */

    T revamp();

    /**
     * Confirms that the objects have the same core data
     *
     * @param expected Object containing the expected values
     * @param actual Object containing the actual values
     */

    void hasSameContent(T expected, T actual);

    default void hasSameContentAsPrototype(T actual)
    {
        hasSameContent(prototype(), actual);
    }
}
