package org.saltations.mre.core.control;

import lombok.AllArgsConstructor;
import lombok.Getter;


public sealed interface XResult<VT,FT> permits XFailure, XSuccess
{
    boolean isSuccess();

    boolean isFailure();

    public VT get() throws Throwable;
}


