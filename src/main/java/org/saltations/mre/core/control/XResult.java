package org.saltations.mre.core.control;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;


public sealed interface XResult<VT,FT> permits XFailure, XSuccess
{
    boolean isSuccess();

    boolean isFailure();

    public VT get() throws Throwable;

    public VT orElse(VT value);

    public <X extends Exception> VT orThrow(Supplier<X> supplier) throws X;


}


