package org.saltations.mre.shared.core.errors;

import java.net.URI;
import java.util.Map;

import io.micronaut.problem.HttpStatusType;

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

    HttpStatusType statusType();

    Map<String,Object> extensionPropertiesByName();
}
