package org.saltations.mre.common.core.errors;

import java.net.URI;
import java.util.Map;

/**
 * Standard exception interface. This exists so that Domain specific exceptions thrown from the services where the
 * controller can be mapped to the standard {@link org.zalando.problem.ThrowableProblem} without using the
 * {@code ThrowableProblem} class as a base for all domain specific errors.
 */

public interface DomainProblem
{
    URI expandType(URI problemTypeRootURI);

    String title();

    String detail();

    Map<String,Object> extensionPropertiesByName();
}
