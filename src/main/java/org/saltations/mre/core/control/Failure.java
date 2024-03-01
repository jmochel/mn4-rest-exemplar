package org.saltations.mre.core.control;

/**
 * Represents a failure composed of an enumerated failure type FT and a possible exception
 *
 * @param <FT> The class of the failure code type (Typically Enum, String, or Integer)
 */

public interface Failure<FT extends  FailureType>
{
    FT getType();

    Exception  getCause();

    String getDetail();

    default boolean hasCause()
    {
        return getCause() != null;
    }
}
