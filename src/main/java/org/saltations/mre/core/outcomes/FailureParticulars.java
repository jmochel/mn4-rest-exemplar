package org.saltations.mre.core.outcomes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.helpers.MessageFormatter;

/**
 * A record of the particulars of a failure. This includes the type of failure, a title, a detail message and an
 * optional cause.
 */

@Slf4j
@Data
@Getter
@Setter
@AllArgsConstructor
public class FailureParticulars
{
    private FailureType type;
    private String title;
    private String detail;
    private Exception cause;

    public boolean hasCause()
    {
        return cause != null;
    }

    public String getTotalMessage()
    {
        return title + "-" + detail;
    }

    public static Builder of()
    {
        return new Builder();
    }

    public static final class Builder
    {
        // Final values that get passed into the XFail.

        private FailureType type = BasicFailureType.GENERIC;
        private Exception cause;

        // Additional fields input for building

        private Object[] args = new Object[]{};
        private String title;
        private String template;

        private Builder()
        {
        }

        private Builder(FailureParticulars initialData)
        {
            this.type = initialData.type;
            this.title = initialData.title;
            this.cause = initialData.cause;
        }

        public Builder type(FailureType type)
        {
            this.type = type;

            if (this.title == null)
            {
                this.title = type.title();
            }

            if (this.template == null)
            {
                this.template = type.template();
            }

            return this;
        }

        public Builder title(String title)
        {
            this.title = title;
            return this;
        }

        public Builder template(String template)
        {
            this.template = template;
            return this;
        }

        public Builder cause(Exception cause)
        {
            this.cause = cause;
            return this;
        }

        public Builder args(Object...args)
        {
            this.args = args;
            return this;
        }

        public FailureParticulars build()
        {
            return new FailureParticulars(type, title, MessageFormatter.basicArrayFormat(template, args), cause);
        }
    }
}
