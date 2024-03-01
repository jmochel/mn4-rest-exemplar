package org.saltations.mre.core.control;

import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * OpResult is a class that represents a potential success or failure of an operation
 *
 * <p>Provides a concrete implementation of {@link Result}
 *
 * @param <VT> Type of the potential value to be returned
 * @param <FT> Enumerated type of the potential failure to be returned.
 */

@Getter
@AllArgsConstructor
class OpResult<VT, FT extends Enum<?>> implements Result<VT, FT>
{
    private VT value;
    private Failure<FT> failure;
}
