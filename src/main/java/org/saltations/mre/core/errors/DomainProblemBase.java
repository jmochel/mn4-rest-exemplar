package org.saltations.mre.core.errors;

import io.micronaut.problem.HttpStatusType;
import io.micronaut.serde.annotation.Serdeable;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static java.text.MessageFormat.format;

/**
 * Basic internal error that can be mapped to RFC 7807 Problem (without HTTP status)
 */

@Getter
@Setter
@Serdeable
public class DomainProblemBase extends Exception implements DomainProblem
{
    private final String title;

    private final String detail;

    private HttpStatusType statusType;

    private final Map<String,Object> properties = new HashMap<>();

    public DomainProblemBase(String title, String detailTemplate, Object...args)
    {
        super(title + ":" + format(detailTemplate, args));
        this.title = title;
        this.detail = format(detailTemplate, args);

        properties.put("trace-id", UUID.randomUUID().toString());
    }

    public DomainProblemBase(Throwable cause, String title, String detailTemplate, Object...args)
    {
        super(title + ":" + format(detailTemplate, args), cause);
        this.title = title;
        this.detail = format(detailTemplate, args);

        properties.put("trace-id", UUID.randomUUID().toString());
    }

}