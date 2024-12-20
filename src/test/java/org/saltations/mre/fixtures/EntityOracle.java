package org.saltations.mre.fixtures;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import io.micronaut.core.beans.BeanProperty;

/**
 * Minimum contract for an Oracle that produces core objects and entities that implement the core interface (IC)
 *
 * @param <IC> Interface of the core domain item being represented
 * @param <C>  Class of the core domain item
 * @param <E>  Class of the persistable domain entity.
 *             Contains all the same data as C but supports additional entity specific meta-data.
 */

public interface EntityOracle<IC, C extends IC, E extends C>
{
    /**
     * Provides the initial shared value for all objects of class T
     */

    long getInitialSharedValue();

    /**
     * Returns a core exemplar of class C based on a shared value with an offset.
     * <p>
     * The shared value is the start of the numeric name space of all the objects of class C and E. This allows us to
     * create related groups of domain objects that have a consistent results for each group of objects.
     * The created object should have the same data when it's created with the same initial value and offset
     * </p>
     * @param sharedInitialValue Initial shared value for all objects of the class
     * @param offset Offset used to compose data for this specific object.
     *
     * @return A populated core object.
     */

    C coreExemplar(long sharedInitialValue, int offset);

    /**
     * Returns a prototype entity object of class E based on a shared value with an offset.
     * <p>
     * The shared value is the start of the numeric name space of all the objects of class E or E. This allows us to
     * create related groups of domain objects that have a consistent results for each group of objects.
     * The created object should have the same data when it's created with the same initial value and offset
     * </p>
     * @param sharedInitialValue Initial shared value for all objects of the class
     * @param offset Offset used to compose data for this specific object.
     *
     * @return A populated object of type T.
     */

    E entityExemplar(long sharedInitialValue, int offset);

    /**
     * Returns a copy of the original object with all content attributes (not ids) changed
     *
     * @param original Original object of class T
     *
     * @return An object of class T with all content attributes different from the original.
     */

    C refurbishCore(C original);

    /**
     * Confirms that the exemplars have the same core values
     */

    void hasSameCoreContent(IC expected, IC actual);

    /**
     * Returns a core object populated with all the data needed to create an entity
     */

    default C coreExemplar(int offset)
    {
        return coreExemplar(getInitialSharedValue(), offset);
    }

    /**
     * Returns a core prototype for the default offset of 0
     */

    default C coreExemplar()
    {
        return coreExemplar(0);
    }

    /**
     * Returns a list of core prototypes with offsets from the given start to given end
     */

    default List<C> coreExemplars(int startOffset, int endOffset)
    {
        return IntStream.rangeClosed(startOffset, endOffset)
                .mapToObj(i -> coreExemplar(i))
                .collect(Collectors.toList());
    }

    /**
     * Returns an entity exemplar
     */

    default E entityExemplar(int offset)
    {
        return entityExemplar(getInitialSharedValue(), offset);
    }

    /**
     * Returns an entity prototype for the default offset of 0
     */

    default E entityExemplar()
    {
        return entityExemplar(0);
    }

    /**
     * Returns a list of entity prototypes with offsets from the given start to given end
     */

    default List<E> entityExemplars(int startOffset, int endOffset)
    {
        return IntStream.rangeClosed(startOffset, endOffset)
                .mapToObj(i -> entityExemplar(i))
                .collect(Collectors.toList());
    }


    /**
     * Returns a refurbished copy of a core object created from the given offset
     */

    default C refurbishCore(int offset)
    {
        return refurbishCore(coreExemplar(offset));
    }

    /**
     * Returns a refurbished copy of the default core prototype
     */

    default C refurbishCore()
    {
        return refurbishCore(coreExemplar());
    }

    /**
     * Confirms that the provided exemplar has the same core content as a default core prototype 
     */
    default void hasSameCoreContentAsPrototype(IC actual)
    {
        hasSameCoreContent(coreExemplar(), actual);
    }

    /**
     * Extracts the list of core bean properties
     */

    List<BeanProperty<IC,Object>> extractCoreProperties();
}
