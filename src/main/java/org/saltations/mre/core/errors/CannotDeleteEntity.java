package org.saltations.mre.core.errors;

import io.micronaut.http.HttpStatus;
import io.micronaut.problem.HttpStatusType;
import io.micronaut.serde.annotation.Serdeable;

import java.text.MessageFormat;

/**
 * Denotes the failure to delete an entity of a given type from a prototype
 */

@Serdeable
public class CannotDeleteEntity extends DomainProblemBase
{
    private static final String TITLE = "Cannot delete {0}";

    public CannotDeleteEntity(String resourceTypeName, Object id)
    {
        super(MessageFormat.format(TITLE, resourceTypeName),"Cannot delete a {0} with id {1}", resourceTypeName, id.toString());
        setStatusType(new HttpStatusType(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    public CannotDeleteEntity(Throwable e, String resourceTypeName, Object id)
    {
        super(e, MessageFormat.format(TITLE, resourceTypeName),"Cannot delete a {0} with id {1}", resourceTypeName, id.toString());
        setStatusType(new HttpStatusType(HttpStatus.INTERNAL_SERVER_ERROR));
    }

}