package org.saltations.mre.common.application;

import lombok.AllArgsConstructor;
import lombok.experimental.Accessors;
import org.saltations.endeavour.FailureType;


@Accessors(fluent = true)
@AllArgsConstructor
public enum CrudFailure implements FailureType
{
    GENERAL("Uncategorized error",""),
    CANNOT_CREATE("Unable to create an entity", "{} entity could not be created from [{}]"),;

    private final String title;
    private final String template;

    @Override
    public String getTitle()
    {
        return title;
    }

    @Override
    public String getTemplate()
    {
        return template;
    }
}
