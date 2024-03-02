package org.saltations.mre.core.control;

import java.util.regex.Pattern;

/**
 *
 *
 * TODO Summary(ends with '.',third person[gets the X, not Get X],do not use @link) ${NAME} represents xxx OR ${NAME} does xxxx.
 *
 * <p>TODO Description(1 lines sentences,) References generic parameters with {@code <T>} and uses 'b','em', dl, ul, ol tags
 * org.slf4j.helpers.MessageFormatter
 *
 */
public interface FailureType
{
    String getTitle();

    String getDetailTemplate();

    default String formatDetail(Object...args)
    {
        return org.slf4j.helpers.MessageFormatter.basicArrayFormat(getDetailTemplate(), args);
    }

    default long detailTemplateParamCounts()
    {
        return Pattern.compile("\\{\\}") // Pattern
                                             .matcher(getDetailTemplate()) // Mather
                                             .results()
                                             .count();
    }

    default boolean detailTemplateTakesParams()
    {
        return getDetailTemplate().contains("{}");
    }
}
