package org.saltations.mre.core.errors;

import io.micronaut.http.HttpStatus;
import io.micronaut.problem.HttpStatusType;
import io.micronaut.serde.annotation.Serdeable;

import java.text.MessageFormat;

/**
 * Denotes the failure to create an entity of a given type from a prototype
 */

@Serdeable
public class CannotFindEntity extends DomainProblemBase
{
    private static final String PROBLEM_TYPE = "cannot-find-entity";

    private static final String TITLE_TEMPLATE = "Cannot find {0}";

    public CannotFindEntity(String resourceTypeName, Object id)
    {
        super(PROBLEM_TYPE, MessageFormat.format(TITLE_TEMPLATE, resourceTypeName),"Cannot find a {0} with id {1}", resourceTypeName, id.toString());
        setStatusType(new HttpStatusType(HttpStatus.NOT_FOUND));
    }

    public CannotFindEntity(Throwable e, String resourceTypeName, Object id)
    {
        super(e, PROBLEM_TYPE, MessageFormat.format(TITLE_TEMPLATE, resourceTypeName),"Cannot find a {0} with id {1}", resourceTypeName, id.toString());
        setStatusType(new HttpStatusType(HttpStatus.NOT_FOUND));
    }

}