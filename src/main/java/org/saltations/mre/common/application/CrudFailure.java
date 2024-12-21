package org.saltations.mre.common.application;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.saltations.mre.common.core.outcomes.FailureType;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public enum CrudFailure implements FailureType
{
    GENERAL("Uncategorized error",""),
    CANNOT_CREATE("Unable to create an entity", "{} entity could not be created from [{}]"),;

    private final String title;
    private final String template;
}
