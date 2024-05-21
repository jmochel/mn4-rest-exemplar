package org.saltations.mre.core.outcomes;

import java.util.regex.Pattern;

/**
 * Represents a type of failure. A failure type has a title and a template for the failure details message.
 */

public interface FailureType
{
    String getTitle();
    String getTemplate();

    /**
     * Count the number of template parameters needed by the template.
     */

    default long templateParameterCount()
    {
        return Pattern.compile("\\{\\}")
                      .matcher(getTemplate())
                      .results()
                      .count();
    }

}
