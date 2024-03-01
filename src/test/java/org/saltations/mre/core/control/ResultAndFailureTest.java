package org.saltations.mre.core.control;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;
import org.junit.jupiter.api.TestMethodOrder;
import org.saltations.mre.core.ReplaceBDDCamelCase;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@MicronautTest(transactional = false)
@DisplayNameGeneration(ReplaceBDDCamelCase.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
public class ResultAndFailureTest
{
    @Getter
    @AllArgsConstructor
    enum ExemplarFailure implements FailureType {

        GENERAL("Uncategorized error",""),
        POTENTIALLY_FATAL("Potentially Fatal error of some sort", ""),
        NOT_REALLY_SO_BAD("Not so bad problem", "Happened in widget [{}]");

        private final String title;
        private final String detailTemplate;
    }

    @Nested
    @Order(1)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class GoldenPathSuccesses {
        @Test
        @Order(1)
        void canCreateSuccessWithValue()
        {
            var result = Results.success("OK");

            assertAll("Result",
                    () -> assertTrue(result.isSuccess(),"Is Success"),
                    () -> assertFalse(result.isFailure(),"Is not Failure")
            );

            assertAll("Success",
                    () -> assertEquals("OK",result.getValue(), "Value")
            );
        }

        @Test
        @Order(2)
        void canCreateSuccessWithoutValue()
        {
            var result = Results.success();

            assertAll("Result",
                    () -> assertTrue(result.isSuccess(),"Is Success"),
                    () -> assertFalse(result.isFailure(),"Is not Failure")
            );

            assertAll("Success",
                    () -> assertNull(result.getValue(), "Value")
            );
        }
    }

    @Nested
    @Order(2)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class GoldenPathFailures {

        @Test
        @Order(1)
        void canCreateFailureWithType()
        {
            var result = Results.failure(ExemplarFailure.POTENTIALLY_FATAL);

            assertAll("Result",
                    () -> assertFalse(result.isSuccess(),"Is Not Success"),
                    () -> assertTrue(result.isFailure(),"Is Failure"),
                    () -> assertNull(result.getValue(), "Value")
            );

            assertAll("Failure",
                    () -> assertEquals(ExemplarFailure.POTENTIALLY_FATAL, result.getFailure().getType(), "Type"),
                    () -> assertFalse(result.getFailure().hasCause(), "Has Cause"),
                    () -> assertNull(result.getFailure().getCause(), "Cause")
            );
        }

        @Test
        @Order(2)
        void canCreateFailureWithTypeAndDetail()
        {
            var detail = "This went really bad";
            var result = Results.failure(ExemplarFailure.POTENTIALLY_FATAL, detail);

            assertAll("Result",
                    () -> assertFalse(result.isSuccess(),"Is Not Success"),
                    () -> assertTrue(result.isFailure(),"Is Failure"),
                    () -> assertNull(result.getValue(), "Value")
            );

            assertAll("Failure",
                    () -> assertEquals(ExemplarFailure.POTENTIALLY_FATAL, result.getFailure().getType(), "Type"),
                    () -> assertEquals(detail, result.getFailure().getDetail(), "Detail"),
                    () -> assertFalse(result.getFailure().hasCause(), "Has Cause"),
                    () -> assertNull(result.getFailure().getCause(), "Cause")
            );
        }

        @Test
        @Order(3)
        void canCreateFailureWithTypeAndFormattedDetail()
        {
            var detail = "Sinister";
            var result = Results.formattedFailure(ExemplarFailure.NOT_REALLY_SO_BAD, detail);

            assertAll("Result",
                    () -> assertFalse(result.isSuccess(),"Is Not Success"),
                    () -> assertTrue(result.isFailure(),"Is Failure"),
                    () -> assertNull(result.getValue(), "Value")
            );

            assertAll("Failure",
                    () -> assertEquals(ExemplarFailure.NOT_REALLY_SO_BAD, result.getFailure().getType(), "Type"),
                    () -> assertTrue(result.getFailure().getDetail().contains("[Sinister]"), "Detail contains expanded arg"),
                    () -> assertFalse(result.getFailure().hasCause(), "Has Cause"),
                    () -> assertNull(result.getFailure().getCause(), "Cause")
            );
        }

        @Test
        @Order(20)
        void canCreateFailureWithException()
        {
            var cause = new Exception();
            var result = Results.failure(cause);

            assertAll("Result",
                    () -> assertFalse(result.isSuccess(),"Is Not Success"),
                    () -> assertTrue(result.isFailure(),"Is Failure"),
                    () -> assertNull(result.getValue(), "Value")
            );

            assertAll("Failure",
                    () -> assertNull(result.getFailure().getType(), "Type"),
                    () -> assertEquals("", result.getFailure().getDetail(), "Detail"),
                    () -> assertTrue(result.getFailure().hasCause(), "Has Cause"),
                    () -> assertNotNull(result.getFailure().getCause(), "Cause")
            );
        }

        @Test
        @Order(22)
        void canCreateFailureWithExceptionAndType()
        {
            var cause = new Exception();
            var result = Results.failure(cause, ExemplarFailure.POTENTIALLY_FATAL);

            assertAll("Result",
                    () -> assertFalse(result.isSuccess(),"Is Not Success"),
                    () -> assertTrue(result.isFailure(),"Is Failure"),
                    () -> assertNull(result.getValue(), "Value")
            );

            assertAll("Failure",
                    () -> assertEquals(ExemplarFailure.POTENTIALLY_FATAL, result.getFailure().getType(), "Type"),
                    () -> assertEquals("", result.getFailure().getDetail(), "Detail"),
                    () -> assertTrue(result.getFailure().hasCause(), "Has Cause"),
                    () -> assertNotNull(result.getFailure().getCause(), "Cause")
            );
        }

        @Test
        @Order(24)
        void canCreateFailureWithExceptionAndTypeAndDetail()
        {
            var cause = new Exception();
            var detail = "This went really bad";
            var result = Results.failure(cause, ExemplarFailure.POTENTIALLY_FATAL, detail);

            assertAll("Result",
                    () -> assertFalse(result.isSuccess(),"Is Not Success"),
                    () -> assertTrue(result.isFailure(),"Is Failure"),
                    () -> assertNull(result.getValue(), "Value")
            );

            assertAll("Failure",
                    () -> assertEquals(ExemplarFailure.POTENTIALLY_FATAL, result.getFailure().getType(), "Type"),
                    () -> assertEquals(detail, result.getFailure().getDetail(), "Detail"),
                    () -> assertTrue(result.getFailure().hasCause(), "Has Cause"),
                    () -> assertNotNull(result.getFailure().getCause(), "Cause")
            );
        }

        @Test
        @Order(26)
        void canCreateFailureWithExceptionAndDetail()
        {
            var cause = new Exception();
            var detail = "This went really bad";
            var result = Results.failure(cause, detail);

            assertAll("Result",
                    () -> assertFalse(result.isSuccess(),"Is Not Success"),
                    () -> assertTrue(result.isFailure(),"Is Failure"),
                    () -> assertNull(result.getValue(), "Value")
            );

            assertAll("Failure",
                    () -> assertNull(result.getFailure().getType(), "Type"),
                    () -> assertEquals(detail, result.getFailure().getDetail(), "Detail"),
                    () -> assertTrue(result.getFailure().hasCause(), "Has Cause"),
                    () -> assertNotNull(result.getFailure().getCause(), "Cause")
            );
        }

        @Test
        @Order(28)
        void canCreateFailureWithExceptionAndTypeAndFormattedDetail()
        {
            var cause = new Exception();
            var aDetail = "Sinister";
            var result = Results.formattedFailure(cause, ExemplarFailure.NOT_REALLY_SO_BAD, aDetail);

            assertAll("Result",
                    () -> assertFalse(result.isSuccess(),"Is Not Success"),
                    () -> assertTrue(result.isFailure(),"Is Failure"),
                    () -> assertNull(result.getValue(), "Value")
            );

            assertAll("Failure",
                    () -> assertEquals(ExemplarFailure.NOT_REALLY_SO_BAD, result.getFailure().getType(), "Type"),
                    () -> assertTrue(result.getFailure().getDetail().contains("[Sinister]"), "Detail has expanded arg"),
                    () -> assertTrue(result.getFailure().hasCause(), "Has Cause"),
                    () -> assertNotNull(result.getFailure().getCause(), "Cause")
            );
        }

    }

}
