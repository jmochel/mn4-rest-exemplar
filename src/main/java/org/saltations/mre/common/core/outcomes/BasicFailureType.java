package org.saltations.mre.common.core.outcomes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * The default basic failure type that is used when no other failure type is specified..
 */

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public enum BasicFailureType implements FailureType
{
    GENERIC("generic-failure", ""),
    CATASTROPHIC("catastrophic-failure", "");

    private final String title;
    private final String template;
}
