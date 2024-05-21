package org.saltations.mre.core.outcomes;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The default basic failure type that is used when no other failure type is specified..
 */

@Getter
@AllArgsConstructor
public enum BasicFailureType implements FailureType
{
    GENERIC("generic-failure", ""),
    CATASTROPHIC("catastrophic-failure", "");

    private final String title;
    private final String template;
}
