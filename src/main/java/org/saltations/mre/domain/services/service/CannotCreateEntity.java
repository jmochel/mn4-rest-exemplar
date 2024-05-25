package org.saltations.mre.domain.services.service;

import java.text.MessageFormat;

import io.micronaut.http.HttpStatus;
import io.micronaut.problem.HttpStatusType;
import io.micronaut.serde.annotation.Serdeable;
import org.saltations.mre.domain.core.errors.DomainProblemBase;

/**
 * Denotes the failure to create an entity of a given type from a prototype
 */

@Serdeable
public class CannotCreateEntity extends DomainProblemBase
{
    private static final String PROBLEM_TYPE = "cannot-create-entity";

    private static final String TITLE_TEMPLATE = "Cannot create {0}";

    public CannotCreateEntity(String resourceTypeName, Object prototype)
    {
        super(PROBLEM_TYPE, MessageFormat.format(TITLE_TEMPLATE, resourceTypeName),"Cannot create a {0} with contents {1}", resourceTypeName, prototype.toString());
        statusType(new HttpStatusType(HttpStatus.BAD_REQUEST));
    }

    public CannotCreateEntity(Throwable e, String resourceTypeName, Object prototype)
    {
        super(e, PROBLEM_TYPE, MessageFormat.format(TITLE_TEMPLATE, resourceTypeName),"Cannot create a {0} with contents {1}", resourceTypeName, prototype.toString());
        statusType(new HttpStatusType(HttpStatus.BAD_REQUEST));
    }

}
