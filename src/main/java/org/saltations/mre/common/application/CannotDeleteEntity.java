package org.saltations.mre.common.application;

import io.micronaut.http.HttpStatus;
import io.micronaut.problem.HttpStatusType;
import io.micronaut.serde.annotation.Serdeable;
import org.saltations.mre.common.core.errors.DomainProblemBase;

import java.text.MessageFormat;

/**
 * Denotes the failure to delete an entity of a given type from a prototype
 */

@Serdeable
public class CannotDeleteEntity extends DomainProblemBase
{
    private static final String PROBLEM_TYPE = "cannot-delete-entity";

    private static final String TITLE_TEMPLATE = "Cannot delete {0}";

    public CannotDeleteEntity(String resourceTypeName, Object id)
    {
        super(PROBLEM_TYPE, MessageFormat.format(TITLE_TEMPLATE, resourceTypeName),"Cannot delete a {0} with id {1}", resourceTypeName, id.toString());
        statusType(new HttpStatusType(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    public CannotDeleteEntity(Throwable e, String resourceTypeName, Object id)
    {
        super(e, PROBLEM_TYPE, MessageFormat.format(TITLE_TEMPLATE, resourceTypeName),"Cannot delete a {0} with id {1}", resourceTypeName, id.toString());
        statusType(new HttpStatusType(HttpStatus.INTERNAL_SERVER_ERROR));
    }

}
