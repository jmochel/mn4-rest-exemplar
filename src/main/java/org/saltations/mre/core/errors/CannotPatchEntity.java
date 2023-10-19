package org.saltations.mre.core.errors;

import io.micronaut.http.HttpStatus;
import io.micronaut.problem.HttpStatusType;
import io.micronaut.serde.annotation.Serdeable;

import java.text.MessageFormat;

/**
 * Denotes the failure to patch an entity of a given type from a patch
 */

@Serdeable
public class CannotPatchEntity extends DomainProblemBase
{
    private static final String TITLE = "Cannot patch {0}";

    public CannotPatchEntity(String resourceTypeName, Long id)
    {
        super(MessageFormat.format(TITLE, resourceTypeName),"Cannot patch a {0} with id {1}", resourceTypeName, id);
        setStatusType(new HttpStatusType(HttpStatus.BAD_REQUEST));
    }

    public CannotPatchEntity(Throwable e, String resourceTypeName, Long id)
    {
        super(e, MessageFormat.format(TITLE, resourceTypeName),"Cannot patch a {0} with id {1}", resourceTypeName, id);
        setStatusType(new HttpStatusType(HttpStatus.BAD_REQUEST));
    }

}