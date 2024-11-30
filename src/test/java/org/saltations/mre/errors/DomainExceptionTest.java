package org.saltations.mre.errors;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.saltations.mre.common.core.errors.DomainException;
import org.saltations.mre.fixtures.ReplaceBDDCamelCase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayNameGeneration(ReplaceBDDCamelCase.class)
class DomainExceptionTest {

    @Test
    void domainExceptionWithMessage() {
        var exception = new DomainException("Error occurred");
        assertEquals("Error occurred", exception.getMessage());
    }

    @Test
    void domainExceptionWithMessageAndArgs() {
        var exception = new DomainException("Error: {}", "details");
        assertEquals("Error: details", exception.getMessage());
    }

    @Test
    void domainExceptionWithThrowableAndMessage() {
        var cause = new RuntimeException("Cause");
        DomainException exception = new DomainException(cause, "Error occurred");
        assertEquals("Error occurred", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void domainExceptionWithThrowableMessageAndArgs() {
        var cause = new RuntimeException("Cause");
        var exception = new DomainException(cause, "Error: {}", "details");
        assertEquals("Error: details", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void domainExceptionTraceIdIsNotNull() {
        var exception = new DomainException("Error occurred");
        assertNotNull(exception.getTraceId());
    }
}
