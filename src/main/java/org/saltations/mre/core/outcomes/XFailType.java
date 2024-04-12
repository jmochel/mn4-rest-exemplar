package org.saltations.mre.core.outcomes;

import java.util.regex.Pattern;

public interface XFailType
{
    String getTitle();
    String getTemplate();

    default long templateParameterCount()
    {
        return Pattern.compile("\\{\\}")
                      .matcher(getTemplate())
                      .results()
                      .count();
    }

}
