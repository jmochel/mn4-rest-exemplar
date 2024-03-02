package org.saltations.mre.core.control;

public class XResultException extends Exception
{
    public XResultException(Throwable cause, FailureType failureType)
    {
        super(failureType.getTitle(), cause);
    }

    public XResultException(FailureType failureType)
    {
        super(failureType.getTitle());
    }

}
