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

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.saltations.mre.core.control.Results.failure;
import static org.saltations.mre.core.control.Results.fromThrowable;
import static org.saltations.mre.core.control.Results.success;

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
            var result = success("OK");

            assertResultIsSuccess(result);

            assertAll("Success",
                    () -> assertEquals("OK",result.getValue(), "Value")
            );
        }

        @Test
        @Order(2)
        void canCreateSuccessWithoutValue()
        {
            var result = success();

            assertResultIsSuccess(result);

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
            var result = failure(ExemplarFailure.POTENTIALLY_FATAL);

            assertResultIsFailure(result);

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
            var result = failure(ExemplarFailure.POTENTIALLY_FATAL, detail);

            assertResultIsFailure(result);

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

            assertResultIsFailure(result);

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
            var result = failure(cause);

            assertResultIsFailure(result);

            assertAll("Failure",
                    () -> assertEquals(Results.GenericFailure.GENERAL, result.getFailure().getType(), "Type"),
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
            var result = failure(cause, ExemplarFailure.POTENTIALLY_FATAL);

            assertResultIsFailure(result);

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
            var result = failure(cause, ExemplarFailure.POTENTIALLY_FATAL, detail);

            assertResultIsFailure(result);

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
            var result = failure(cause, detail);

            assertResultIsFailure(result);

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

            assertResultIsFailure(result);

            assertAll("Failure",
                    () -> assertEquals(ExemplarFailure.NOT_REALLY_SO_BAD, result.getFailure().getType(), "Type"),
                    () -> assertTrue(result.getFailure().getDetail().contains("[Sinister]"), "Detail has expanded arg"),
                    () -> assertTrue(result.getFailure().hasCause(), "Has Cause"),
                    () -> assertNotNull(result.getFailure().getCause(), "Cause")
            );
        }

    }

    @Nested
    @Order(3)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class GivenSuccessfulOperation {

        @Test
        @Order(30)
        void whenProvidedValueThenAbleToRetrieve()
        {
            var result = simpleReturnOfOKString();
            var value = result.getValue();
        }

        @Test
        @Order(31)
        void whenProvidedValueThenAbleToRetrieveRatherThanThrowException() throws Exception
        {
            var result = simpleReturnOfOKString();
            var value = result.orThrow();
        }

        Result<String,ExemplarFailure> simpleReturnOfOKString()
        {
            return success("OK");
        }
    }

    @Nested
    @Order(4)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class GivenFailedOperation {

        @Test
        @Order(40)
        void whenRetrievingValueThenGetsNullValue()
        {
            var result = simpleReturnOfFailure();

            assertNull(result.getValue(),"Value");
        }

        @Test
        @Order(41)
        void whenRetrievingValueThenThrowsException()
        {
            var result = simpleReturnOfFailure();

            assertThrows(Exception.class, () -> result.orThrow(), "Cannot get value from a failure");
        }

        Result<String,ExemplarFailure> simpleReturnOfFailure()
        {
            return failure(ExemplarFailure.NOT_REALLY_SO_BAD);
        }
    }

    @Nested
    @Order(5)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class GivenThrowableSupplier {

        @Test
        @Order(50)
        void whenUsingMethodThatCouldThrowExceptionThatReturnsValueThenTransformItToSimpleFailureWithValue()
        {
            var result = fromThrowable(this::methodThatCouldThrowIllegalArgumentException);

            assertResultIsSuccess(result);
        }

        @Test
        @Order(51)
        void whenUsingMethodThatThrowsExceptionThenTransformItToSimpleFailureWithCause()
        {
            var result = fromThrowable(this::methodThatThrowsIllegalArgumentException);

            assertResultIsFailure(result);

            assertAll("Failure",
                    () -> assertEquals(Results.GenericFailure.GENERAL, result.getFailure().getType(), "Type"),
                    () -> assertTrue(result.getFailure().hasCause(), "Has Cause"),
                    () -> assertNotNull(result.getFailure().getCause(), "Cause")
            );
        }

        String methodThatThrowsIllegalArgumentException() throws IllegalArgumentException
        {
            throw new IllegalArgumentException("Nope, you can't use that");
        }

        String methodThatCouldThrowIllegalArgumentException() throws IllegalArgumentException
        {
            return  "OK";
        }

    }

    @Nested
    @Order(6)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class GivenSuccessResult
    {
        @Test
        @Order(60)
        void thenTransformItToSuccessWithNewValue()
        {
            var result = methodThatReturnsSuccessfulResult();

            var transformedResult = result.ifSuccess(new Supplier<Result<String, Results.GenericFailure>>()
            {
                @Override
                public Result<String, Results.GenericFailure> get()
                {
                    return success("VERY OK");
                }
            });

            assertResultIsSuccess(transformedResult);

            assertEquals("VERY OK", transformedResult.getValue(), "Value");
        }

        @Test
        @Order(61)
        void thenTransformItToFailure()
        {
            var result = methodThatReturnsSuccessfulResult();

            var transformedResult = result.ifSuccess(new Supplier<Result<String, Results.GenericFailure>>()
            {
                @Override
                public Result<String, Results.GenericFailure> get()
                {
                    return failure(Results.GenericFailure.GENERAL);
                }
            });

            assertResultIsFailure(transformedResult);

            assertEquals(Results.GenericFailure.GENERAL, transformedResult.getFailure().getType(), "Failure Type");
        }

        private Result<String, Results.GenericFailure> methodThatReturnsSuccessfulResult()
        {
            return success("OK");
        }
    }


    private void assertResultIsFailure(Result<?,?> result)
    {
        assertAll("Result",
                () -> assertFalse(result.isSuccess(),"Is Not Success"),
                () -> assertTrue(result.isFailure(),"Is Failure"),
                () -> assertNull(result.getValue(), "Value")
        );
    }


    private void assertResultIsSuccess(Result<?, ?> result)
    {
        assertAll("Result",
                () -> assertTrue(result.isSuccess(),"Is Success"),
                () -> assertFalse(result.isFailure(),"Is not Failure")
        );
    }
}
