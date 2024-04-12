package org.saltations.mre.core.outcomes;

import java.util.regex.Pattern;

/**
 * Represents an enumeration of a type of failure with some to support message templating
 * <p>
 * It has methods that take a set of variable arguments and produce a detailed message from those arguments and the built-in template
 *
 * <p><b>Sample</b>
 * {@snippet :
 *     @Getter
 *     @AllArgsConstructor
 *     public enum ExampleFailure implements FailureType {
 *
 *         GENERAL("Uncategorized error",""),
 *         NOT_FOUND("Just Not Found","Unable to find resource [{}] of type [{}]");
 *
 *         private final String title;
 *         private final String detailTemplate;
 *     }
 * }
 *
 *
 */

public interface FailureType
{
    /**
     * Return the top level title of the type of failure
     */
    String getTitle();

    /**
     * Teams returns the detail template for this type of error
     * <p>
     * Uses the format from {@link org.slf4j.helpers.MessageFormatter}
     */

    String getDetailTemplate();

    /**
     * Formats the detailed template using the provided arguments
     *
     * <p><b>Example:</b>
     * {@snippet :
     *      var formattedDetail = ExampleFailure.NOT_FOUND.formatDetail(rourcesId, resourceType);   // @highlight substring=
     *
     * }
     *
     * Adding an inline code element:
     * {@snippet file=MarkdownBuilderTest.java region=code-with-backticks}
     * {@snippet file=sample.properties}
     */
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
