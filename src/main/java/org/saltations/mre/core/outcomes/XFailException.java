package org.saltations.mre.core.outcomes;

import static org.slf4j.helpers.MessageFormatter.basicArrayFormat;

public class XFailException extends Exception
{
    public XFailException()
    {
    }

    /**
     * Creates an exception from a message template and arguments in the style of Slf4j
     *
     * @param message Message template in the format of {@link org.slf4j.helpers.MessageFormatter}
     * @param args Collected arguments matching the template
     */
    public XFailException(String message, Object...args)
    {
        super(basicArrayFormat(message, args));
    }

    /**
     * Creates an exception from an exception and a message template and arguments in the style of Slf4j
     *
     * @param cause root cause exception
     * @param message Message template in the format of {@link org.slf4j.helpers.MessageFormatter}
     * @param args Collected arguments matching the template
     */

    public XFailException(Throwable cause, String message, Object...args)
    {
        super(basicArrayFormat(message, args), cause);
    }

    public XFailException(Throwable cause)
    {
        super(cause);
    }


}
