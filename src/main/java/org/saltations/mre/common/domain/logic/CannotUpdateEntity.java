package org.saltations.mre.common.domain.logic;

import java.text.MessageFormat;

import io.micronaut.http.HttpStatus;
import io.micronaut.problem.HttpStatusType;
import io.micronaut.serde.annotation.Serdeable;
import org.saltations.mre.common.core.errors.DomainProblemBase;

/**
 * Denotes the failure to create an entity of a given type from a prototype
 */

@Serdeable
public class CannotUpdateEntity extends DomainProblemBase
{
    private static final String PROBLEM_TYPE = "cannot-update-entity";

    private static final String TITLE_TEMPLATE = "Cannot update {0}";

    public CannotUpdateEntity(String resourceTypeName, Object prototype)
    {
        super(PROBLEM_TYPE, MessageFormat.format(TITLE_TEMPLATE, resourceTypeName),"Cannot update a {0} with contents {1}", resourceTypeName, prototype.toString());
        statusType(new HttpStatusType(HttpStatus.BAD_REQUEST));
    }

    public CannotUpdateEntity(Throwable e, String resourceTypeName, Object prototype)
    {
        super(e, PROBLEM_TYPE, MessageFormat.format(TITLE_TEMPLATE, resourceTypeName),"Cannot update a {0} with contents {1}", resourceTypeName, prototype.toString());
        statusType(new HttpStatusType(HttpStatus.BAD_REQUEST));
    }

}
