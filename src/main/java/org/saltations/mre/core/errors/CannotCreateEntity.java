package org.saltations.mre.core.errors;

import io.micronaut.http.HttpStatus;
import io.micronaut.problem.HttpStatusType;
import io.micronaut.serde.annotation.Serdeable;

import java.text.MessageFormat;

/**
 * Denotes the failure to create an entity of a given type from a prototype
 */

@Serdeable
public class CannotCreateEntity extends DomainProblemBase
{
    private static final String TITLE = "Cannot create {0}";

    public CannotCreateEntity(String resourceTypeName, Object prototype)
    {
        super(MessageFormat.format(TITLE, resourceTypeName),"Cannot create a {0} with contents {1}", resourceTypeName, prototype.toString());
        setStatusType(new HttpStatusType(HttpStatus.BAD_REQUEST));
    }

    public CannotCreateEntity(Throwable e, String resourceTypeName, Object prototype)
    {
        super(e, MessageFormat.format(TITLE, resourceTypeName),"Cannot create a {0} with contents {1}", resourceTypeName, prototype.toString());
        setStatusType(new HttpStatusType(HttpStatus.BAD_REQUEST));
    }

}