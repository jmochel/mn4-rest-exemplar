package org.saltations.mre.errors;

import java.io.IOException;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.saltations.mre.common.core.errors.FormattedUncheckedException;
import org.saltations.mre.fixtures.ReplaceBDDCamelCase;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayNameGeneration(ReplaceBDDCamelCase.class)
class FormattedUncheckedExceptionTest
{
    @Test
    void exceptionMessageIsFormattedCorrectly() {
        String message = "Error: {} occurred at {}";
        String param1 = "NullPointerException";
        String param2 = "line 42";

        FormattedUncheckedException exception = new FormattedUncheckedException(message, param1, param2);

        assertEquals("Error: NullPointerException occurred at line 42", exception.getMessage());
    }

    @Test
    void exceptionWithCauseHasFormattedMessage()
    {
        String message = "Error: {} occurred at {}";
        String param1 = "IOException";
        String param2 = "line 24";
        Throwable cause = new IOException("File not found");
        FormattedUncheckedException exception = new FormattedUncheckedException(cause, message, param1, param2);

        assertEquals("Error: IOException occurred at line 24", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

}
