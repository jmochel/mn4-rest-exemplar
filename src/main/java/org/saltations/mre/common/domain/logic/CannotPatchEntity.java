package org.saltations.mre.common.domain.logic;

import io.micronaut.http.HttpStatus;
import io.micronaut.problem.HttpStatusType;
import io.micronaut.serde.annotation.Serdeable;
import org.saltations.mre.common.core.errors.DomainProblemBase;

import java.text.MessageFormat;

/**
 * Denotes the failure to patch an entity of a given type from a patch
 */

@Serdeable
public class CannotPatchEntity extends DomainProblemBase
{
    private static final String PROBLEM_TYPE = "cannot-patch-entity";

    private static final String TITLE_TEMPLATE = "Cannot patch {0}";

    public CannotPatchEntity(String resourceTypeName, Object id)
    {
        super(PROBLEM_TYPE, MessageFormat.format(TITLE_TEMPLATE, resourceTypeName),"Cannot patch a {0} with id {1}", resourceTypeName, id);
        statusType(new HttpStatusType(HttpStatus.BAD_REQUEST));
    }

    public CannotPatchEntity(Throwable e, String resourceTypeName, Object id)
    {
        super(e, PROBLEM_TYPE, MessageFormat.format(TITLE_TEMPLATE, resourceTypeName),"Cannot patch a {0} with id {1}", resourceTypeName, id);
        statusType(new HttpStatusType(HttpStatus.BAD_REQUEST));
    }


}
