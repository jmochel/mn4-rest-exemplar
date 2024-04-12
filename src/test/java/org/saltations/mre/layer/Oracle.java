package org.saltations.mre.layer;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Represents an Oracle that provides exemplar objects of class T created from an initial shared value and an offset.
 * <p>
 * Domain Glossary
 * <dl>
 *   <dt>Initial Shared Value</dt>
 *   <dd>common numeric value that all objects of the class use as a starting name space for its values. So if Person was
 *   a domain object, the PersonOracle would use the same starting Initial value for all generated Persons.
 *   </dd>
 *   <dt>Offset</dt>
 *   <dd>numeric offset from the initial shared value for that particular prototype being generated.</dd>
 *   <dt>Refurb</dt>
 *   <dd>A prototype that has had all of its attributes modified from the original prototype</dd>
 * </dl>
 */

public interface Oracle<T>
{
    /**
     * Returns a prototype object of class T based on a shared value with an offset.
     * <p>
     * The shared value is the start of the numeric name space of all the objects of class T. This allows us to
     * create related groups of domain objects that have a consistent results for each group of objects.
     * The created object should have the same data when it's created with the same initial value and offset
     * </p>
     * @param sharedInitialValue Initial shared value for all objects of the class
     * @param offset Offset used to compose data for this specific object.
     *
     * @return A populated object of type T.
     */

    T exemplar(int sharedInitialValue, int offset);

    /**
     * Provides the initial shared value for all objects of class T
     */

    int getInitialSharedValue();

    /**
     * Returns a prototype object of class T for the given offset
     */

    default T exemplar(int offset)
    {
        return exemplar(getInitialSharedValue(), offset);
    }

    /**
     * Returns a list of prototype objects with offsets from the given start to given end
     */

    default List<T> exemplars(int startOffset, int endOffset)
    {
        return IntStream.rangeClosed(startOffset, endOffset)
                .mapToObj(i -> exemplar(i))
                .collect(Collectors.toList());
    }

    /**
     * Returns a prototype object of class T with a default offset of 0.
     */

    default T exemplar()
    {
        return exemplar(0);
    }

    /**
     * Returns a copy of the original object with all content attributes (not ids) changed
     *
     * @param original Original object of class T
     *
     * @return An object of class T with all content attributes different from the original.
     */

    T refurbish(T original);

    /**
     * Returns a refurbished copy of the prototype object created from the given offset
     */

    default T refurbished(int offset)
    {
        return refurbish(exemplar(offset));
    }

    /**
     * Returns a refurbished copy of the default prototype object
     */

    default T refurbished()
    {
        return refurbish(exemplar());
    }

    /**
     * Confirms that the objects have the same core data
     *
     * @param expected Object containing the expected values
     * @param actual Object containing the actual values
     */

    void hasSameContent(T expected, T actual);

    default void hasSameContentAsDefaultExemplar(T actual)
    {
        hasSameContent(exemplar(), actual);
    }
}
