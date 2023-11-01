package org.saltations.mre.core.errors;

import io.micronaut.problem.HttpStatusType;

import java.net.URI;
import java.net.URL;
import java.util.Map;

/**
 * Standard exception interface. This exists so that Domain specific exceptions thrown from the services where the
 * controller can be mapped to the standard {@link org.zalando.problem.ThrowableProblem} without using the
 * {@code ThrowableProblem} class as a base for all domain specific errors.
 */

public interface DomainProblem
{
    URI expandType(URI problemTypeRootURI);

    String getTitle();

    String getDetail();

    HttpStatusType getStatusType();

    Map<String,Object> getExtensionPropertiesByName();
}
