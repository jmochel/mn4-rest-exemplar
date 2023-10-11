package org.saltations.mre.core.annotations;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Meta-annotation
 */

@Email
@Size(max = 320)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface StdEmailAddress
{
}