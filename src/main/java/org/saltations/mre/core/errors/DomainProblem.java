package org.saltations.mre.core.errors;

import io.micronaut.problem.HttpStatusType;

import java.util.Map;

public interface DomainProblem
{
    String getTitle();

    String getDetail();

    HttpStatusType getStatusType();

    Map<String,Object> getProperties();
}
