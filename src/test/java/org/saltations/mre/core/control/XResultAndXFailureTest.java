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
import static org.junit.jupiter.api.Assertions.fail;
import static org.saltations.mre.core.control.XResults.failure;
import static org.saltations.mre.core.control.XResults.ofThrowable;


@Testcontainers
@MicronautTest(transactional = false)
@DisplayNameGeneration(ReplaceBDDCamelCase.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
public class XResultAndXFailureTest
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
            var result = XResults.success("OK");

            assertResultIsSuccess(result);

            assertAll("Success",
                    () -> assertEquals("OK",result.get(), "Value")
            );
        }

        @Test
        @Order(2)
        void canCreateSuccessWithoutValue()
        {
            var result = XResults.success();

            assertResultIsSuccess(result);

            assertAll("Success",
                    () -> assertNull(result.get(), "Value")
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

            var failure  = assertResultIsFailure(result);

            assertAll("Failure",
                    () -> assertEquals(ExemplarFailure.POTENTIALLY_FATAL, failure.getType(), "Type"),
                    () -> assertFalse(failure.hasCause(), "Has Cause"),
                    () -> assertNull(failure.getCause(), "Cause")
            );

            assertEquals("", failure.getDetail(), "Detail");
        }

        @Test
        @Order(2)
        void canCreateFailureWithTypeAndDetail()
        {
            var detail = "This went really bad";
            var result = failure(ExemplarFailure.POTENTIALLY_FATAL, detail);

            var failure  = assertResultIsFailure(result);

            assertAll("Failure",
                    () -> assertEquals(ExemplarFailure.POTENTIALLY_FATAL, failure.getType(), "Type"),
                    () -> assertFalse(failure.hasCause(), "Has Cause"),
                    () -> assertNull(failure.getCause(), "Cause")
            );

            assertEquals(detail, failure.getDetail(), "Detail");
        }

        @Test
        @Order(3)
        void canCreateFailureWithTypeAndFormattedDetail()
        {
            var detail = "Sinister";
            var result = XResults.failure(ExemplarFailure.NOT_REALLY_SO_BAD, detail);

            var failure = assertResultIsFailure(result);

            assertAll("Failure",
                    () -> assertEquals(ExemplarFailure.NOT_REALLY_SO_BAD, failure.getType(), "Type"),
                    () -> assertTrue(failure.getDetail().contains("[Sinister]"), "Detail contains expanded arg"),
                    () -> assertFalse(failure.hasCause(), "Has Cause"),
                    () -> assertNull(failure.getCause(), "Cause")
            );
        }

        @Test
        @Order(20)
        void canCreateFailureWithException()
        {
            var cause = new Exception();
            var result = failure(cause);

            var failure = assertResultIsFailure(result);

            assertAll("Failure",
                    () -> assertEquals(XResults.GenericFailure.GENERAL, failure.getType(), "Type"),
                    () -> assertEquals("", failure.getDetail(), "Detail"),
                    () -> assertTrue(failure.hasCause(), "Has Cause"),
                    () -> assertNotNull(failure.getCause(), "Cause")
            );
        }

        @Test
        @Order(22)
        void canCreateFailureWithExceptionAndType()
        {
            var cause = new Exception();
            var result = failure(cause, ExemplarFailure.POTENTIALLY_FATAL);

            var failure = assertResultIsFailure(result);

            assertAll("Failure",
                    () -> assertEquals(ExemplarFailure.POTENTIALLY_FATAL, failure.getType(), "Type"),
                    () -> assertEquals("", failure.getDetail(), "Detail"),
                    () -> assertTrue(failure.hasCause(), "Has Cause"),
                    () -> assertNotNull(failure.getCause(), "Cause")
            );
        }

        @Test
        @Order(24)
        void canCreateFailureWithExceptionAndTypeAndDetail()
        {
            var cause = new Exception();
            var detail = "This went really bad";
            var result = failure(cause, ExemplarFailure.POTENTIALLY_FATAL, detail);

            var failure = assertResultIsFailure(result);

            assertAll("Failure",
                    () -> assertEquals(ExemplarFailure.POTENTIALLY_FATAL, failure.getType(), "Type"),
                    () -> assertEquals(detail, failure.getDetail(), "Detail"),
                    () -> assertTrue(failure.hasCause(), "Has Cause"),
                    () -> assertNotNull(failure.getCause(), "Cause")
            );
        }

        @Test
        @Order(26)
        void canCreateFailureWithExceptionAndDetail()
        {
            var cause = new Exception();
            var detail = "This went really bad";
            var result = failure(cause, detail);

            var failure = assertResultIsFailure(result);

            assertAll("Failure",
                    () -> assertEquals(XResults.GenericFailure.GENERAL, failure.getType(), "Type"),
                    () -> assertEquals(detail, failure.getDetail(), "Detail"),
                    () -> assertTrue(failure.hasCause(), "Has Cause"),
                    () -> assertNotNull(failure.getCause(), "Cause")
            );
        }

        @Test
        @Order(28)
        void canCreateFailureWithExceptionAndTypeAndFormattedDetail()
        {
            var cause = new Exception();
            var aDetail = "Sinister";
            var result = XResults.failure(cause, ExemplarFailure.NOT_REALLY_SO_BAD, aDetail);

            var failure = assertResultIsFailure(result);

            assertAll("Failure",
                    () -> assertEquals(ExemplarFailure.NOT_REALLY_SO_BAD, failure.getType(), "Type"),
                    () -> assertTrue(failure.getDetail().contains("[Sinister]"), "Detail has expanded arg"),
                    () -> assertTrue(failure.hasCause(), "Has Cause"),
                    () -> assertNotNull(failure.getCause(), "Cause")
            );
        }

    }

    @Nested
    @Order(3)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class GivenSuccessfulOperation {

        @Test
        @Order(30)
        void whenProvidedValueThenAbleToRetrieve() throws Throwable
        {
            var result = simpleReturnOfOKString();
            var value = result.get();
        }

        @Test
        @Order(31)
        void whenProvidedValueThenAbleToRetrieveRatherThanThrowException() throws Throwable
        {
            var result = simpleReturnOfOKString();
            var value = result.get();
        }

        XResult<String,ExemplarFailure> simpleReturnOfOKString()
        {
            return XResults.success("OK");
        }
    }

    @Nested
    @Order(4)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class GivenFailedOperation {


        @Test
        @Order(41)
        void whenRetrievingValueThenThrowsException() throws Throwable
        {
            var result = simpleReturnOfFailure();

            assertThrows(Exception.class, () -> result.get(), "Cannot get value from a failure");
        }

        XResult<Object,ExemplarFailure> simpleReturnOfFailure()
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
            var result = ofThrowable(this::methodThatCouldThrowIllegalArgumentException);

            assertResultIsSuccess(result);
        }

        @Test
        @Order(51)
        void whenUsingMethodThatThrowsExceptionThenTransformItToSimpleFailureWithCause()
        {
            var result = ofThrowable(this::methodThatThrowsIllegalArgumentException);

            var failure = assertResultIsFailure(result);

            assertAll("Failure",
                    () -> assertEquals(XResults.GenericFailure.GENERAL, failure.getType(), "Type"),
                    () -> assertTrue(failure.hasCause(), "Has Cause"),
                    () -> assertNotNull(failure.getCause(), "Cause")
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
//
//    @Nested
//    @Order(6)
//    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//    class GivenSuccessResult
//    {
//        @Test
//        @Order(60)
//        void thenTransformItToSuccessWithNewValue()
//        {
//            var result = methodThatReturnsSuccessfulResult();
//
//            var transformedResult = result.ifSuccess(new Supplier<Result<String, Results.GenericFailure>>()
//            {
//                @Override
//                public Result<String, Results.GenericFailure> get()
//                {
//                    return success("VERY OK");
//                }
//            });
//
//            assertResultIsSuccess(transformedResult);
//
//            assertEquals("VERY OK", transformedResult.getValue(), "Value");
//        }
//
//        @Test
//        @Order(61)
//        void thenTransformItToFailure()
//        {
//            var result = methodThatReturnsSuccessfulResult();
//
//            var transformedResult = result.ifSuccess(new Supplier<Result<String, Results.GenericFailure>>()
//            {
//                @Override
//                public Result<String, Results.GenericFailure> get()
//                {
//                    return failure(Results.GenericFailure.GENERAL);
//                }
//            });
//
//            assertResultIsFailure(transformedResult);
//
//            assertEquals(Results.GenericFailure.GENERAL, transformedResult.getFailure().getType(), "Failure Type");
//        }
//
//        private Result<String, Results.GenericFailure> methodThatReturnsSuccessfulResult()
//        {
//            return success("OK");
//        }
//    }


    private XFailure<?,?> assertResultIsFailure(XResult<?,?> result)
    {
        switch (result) {
            case XSuccess f -> fail("Result should be a Failure");
            case XFailure f -> assertAll("Result",
                    () -> assertFalse(result.isSuccess(),"Is Not Success"),
                    () -> assertTrue(result.isFailure(),"Is Failure")
            );
        };

        return (XFailure) result;
    }


    private void assertResultIsSuccess(XResult<?, ?> result)
    {
        switch (result) {
            case XFailure f -> fail("Result should be a Success");
            case XSuccess s -> assertAll("Result",
                    () -> assertTrue(result.isSuccess(),"Is Success"),
                    () -> assertFalse(result.isFailure(),"Is not Failure")
            );
        };


    }
}
