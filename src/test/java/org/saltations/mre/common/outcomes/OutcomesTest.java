package org.saltations.mre.common.outcomes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;
import org.junit.jupiter.api.TestMethodOrder;
import org.saltations.mre.fixtures.ReplaceBDDCamelCase;
import org.saltations.mre.common.core.outcomes.BasicFailureType;
import org.saltations.mre.common.core.outcomes.Failure;
import org.saltations.mre.common.core.outcomes.FailureParticulars;
import org.saltations.mre.common.core.outcomes.FailureType;
import org.saltations.mre.common.core.outcomes.Outcome;
import org.saltations.mre.common.core.outcomes.Outcomes;
import org.saltations.mre.common.core.outcomes.Success;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@DisplayNameGeneration(ReplaceBDDCamelCase.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
public class OutcomesTest
{
    @Getter
    @Accessors(fluent = true)
    @AllArgsConstructor
    enum ExemplarFailure implements FailureType
    {
        GENERAL("Uncategorized error",""),
        POTENTIALLY_FATAL("Potentially Fatal error of some sort", ""),
        NOT_REALLY_SO_BAD("Not so bad problem", "Happened in widget [{}]");

        private final String title;
        private final String template;
    }

    @Nested
    @Order(1)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class GoldenPathSuccesses {
        @Test
        @Order(1)
        void canCreateSuccessWithValue()
        {
            var result = Outcomes.success("OK");
            assertOutcomeIsSuccess(result);
            assertAll("Success",
                    () -> assertEquals("OK",result.rawSuccessValue(), "Value")
            );
        }

        @Test
        @Order(2)
        void canCreateSuccessWithoutValue()
        {
            var result = Outcomes.success();

            assertOutcomeIsSuccess(result);

            assertAll("Success",
                    () -> assertEquals(Boolean.TRUE, result.rawSuccessValue(), "Value")
            );
        }
    }


    @Nested
    @Order(2)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class SimpleUntypedFailures
    {
        @Test
        @Order(2)
        void canCreateSimplestFailure()
        {
            var result = Outcomes.fail();

            var failure = assertOutcomeIsFailure(result);

            assertAll("Failure", () -> assertEquals(BasicFailureType.GENERIC, failure.getType(), "Type"), () -> assertNull(failure.getCause(), "Cause"));

            assertEquals("", failure.getDetail(), "Detail");
        }

        @Test
        @Order(4)
        void canCreateFailureWithDetails()
        {
            var result = Outcomes.failureWith("{} did it", "Bozo");

            var failure = assertOutcomeIsFailure(result);

            assertAll("Failure",
                    () -> assertEquals(BasicFailureType.GENERIC, failure.getType(), "Type"),
                    () -> assertNull(failure.getCause(), "Cause"),
                    () -> assertEquals("Bozo did it", failure.getDetail(), "Detail")
            );
        }
    }


    @Nested
    @Order(4)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class TitledFailures
    {
        @Test
        @Order(2)
        void canCreateTitledFailure()
        {
            var result = Outcomes.titledFailure("Strange Category");

            var failure = assertOutcomeIsFailure(result);

            assertAll("Failure",
                    () -> assertEquals(BasicFailureType.GENERIC, failure.getType(), "Type"),
                    () -> assertNull(failure.getCause(), "Cause")
            );

            assertEquals("Strange Category", failure.getTitle(), "Title");
        }

        @Test
        @Order(4)
        void canCreateTitledFailureWithDetail()
        {
            var detail = "This went really bad";
            var result = Outcomes.titledFailureWith("Really Bad", "Details: {} Bad", "Really Really");

            var failure = assertOutcomeIsFailure(result);

            assertAll("Failure",
                    () -> assertEquals(BasicFailureType.GENERIC, failure.getType(), "Type"),
                    () -> assertNull(failure.getCause(), "Cause")
            );

            assertEquals("Really Bad", failure.getTitle(), "Title");
            assertEquals("Details: Really Really Bad", failure.getDetail(), "Detail");
        }

    }

    @Nested
    @Order(6)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class TypedFailures
    {
        @Test
        @Order(2)
        void canCreateTypedFailure()
        {
            var result = Outcomes.typedFailure(ExemplarFailure.POTENTIALLY_FATAL);

            var failure = assertOutcomeIsFailure(result);

            assertAll("Failure", () -> assertEquals(ExemplarFailure.POTENTIALLY_FATAL, failure.getType(), "Type"), () -> assertNull(failure.getCause(), "Cause"));

            assertEquals("", failure.getDetail(), "Detail");
        }

        @Test
        @Order(6)
        void canCreateTypedFailureWithDetail()
        {
            var detail = "This went really bad";
            var result = Outcomes.typedFailure(ExemplarFailure.POTENTIALLY_FATAL, detail);

            var failure = assertOutcomeIsFailure(result);

            assertAll("Failure", () -> assertEquals(ExemplarFailure.POTENTIALLY_FATAL, failure.getType(), "Type"), () -> assertNull(failure.getCause(), "Cause"));

            assertEquals(detail, failure.getDetail(), "Detail");
        }

        @Test
        @Order(8)
        void canCreateTypedFailureWithDetails()
        {
            var result = Outcomes.typedFailWith(ExemplarFailure.NOT_REALLY_SO_BAD, "Details: {} Bad", "Really Really");
            var failure = assertOutcomeIsFailure(result);

            assertAll("Failure",
                    () -> assertEquals(ExemplarFailure.NOT_REALLY_SO_BAD, failure.getType(), "Type"),
                    () -> assertTrue(failure.getDetail().contains("Details: Really Really Bad"), "Detail contains expanded arg"),
                    () -> assertNull(failure.getCause(), "Cause"));
        }
    }

    @Nested
    @Order(4)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class CausedFailures
    {
        @Test
        @Order(20)
        void canCreateCausedFailure()
        {
            var cause = new Exception();
            var result = Outcomes.causedFailure(cause);

            var failure = assertOutcomeIsFailure(result);

            assertAll("Failure",
                    () -> assertEquals(BasicFailureType.GENERIC, failure.getType(), "Type"),
                    () -> assertEquals("", failure.getDetail(), "Detail"),
                    () -> assertNotNull(failure.getCause(), "Cause")
            );
        }

        @Test
        @Order(22)
        void canCreateCausedFailureWithExceptionAndType()
        {
            var cause = new Exception();
            var result = Outcomes.causedFailure(cause, ExemplarFailure.POTENTIALLY_FATAL);

            var failure = assertOutcomeIsFailure(result);

            assertAll("Failure",
                    () -> assertEquals(ExemplarFailure.POTENTIALLY_FATAL, failure.getType(), "Type"),
                    () -> assertEquals("", failure.getDetail(), "Detail"),
                    () -> assertNotNull(failure.getCause(), "Cause")
            );
        }

        @Test
        @Order(24)
        void canCreateCausedFailureWithExceptionAndTypeAndDetail()
        {
            var cause = new Exception();
            var detail = "This went really bad";
            var result = Outcomes.causedFailure(cause, ExemplarFailure.POTENTIALLY_FATAL, detail);

            var failure = assertOutcomeIsFailure(result);

            assertAll("Failure",
                    () -> assertEquals(ExemplarFailure.POTENTIALLY_FATAL, failure.getType(), "Type"),
                    () -> assertEquals(detail, failure.getDetail(), "Detail"),
                    () -> assertNotNull(failure.getCause(), "Cause")
            );
        }

        @Test
        @Order(26)
        void canCreateCausedFailureWithExceptionAndDetails()
        {
            var cause = new Exception();
            var detail = "This went really bad";
            var result = Outcomes.causedFailureWith(cause, detail);

            var failure = assertOutcomeIsFailure(result);

            assertAll("Failure",
                    () -> assertEquals(BasicFailureType.GENERIC, failure.getType(), "Type"),
                    () -> assertEquals(detail, failure.getDetail(), "Detail"),
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
        void whenProvidedValueThenAbleToRetrieve()
        {
            var result = simpleReturnOfOKString();
            var value = result.potentialSuccessValue();
        }

        @Test
        @Order(31)
        void whenProvidedValueThenAbleToRetrieveRatherThanThrowException()
        {
            var result = simpleReturnOfOKString();
            var value = result.potentialSuccessValue();
        }

        Outcome<FailureParticulars, String> simpleReturnOfOKString()
        {
            return Outcomes.success("OK");
        }
    }

    @Nested
    @Order(4)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class GivenFailedOperation {


        @Test
        @Order(41)
        void whenRetrievingValueThenThrowsException()
        {
            var result = simpleReturnOfFailure();

            assertThrows(Exception.class, () -> result.potentialSuccessValue(), "Cannot get value from a failure");
        }

        Outcome<FailureParticulars, Object> simpleReturnOfFailure()
        {
            return Outcomes.typedFailure(ExemplarFailure.NOT_REALLY_SO_BAD);
        }
    }

    @Nested
    @Order(20)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class ToString {


        @Test
        @Order(2)
        void canShowSuccess()
        {
            assertEquals("XSuccess[Jake]", Outcomes.success("Jake").toString());
        }

        @Test
        @Order(4)
        void canShowFailure()
        {
            assertEquals("Failure[GENERIC:generic-failure:]", Outcomes.fail().toString());
        }
    }

    private Failure<?,?> assertOutcomeIsFailure(Outcome<?,?> result)
    {
        if (result instanceof Success<?,?>)
        {
            fail("Result should be a Failure");
        }

        assertAll("Result",
                () -> assertFalse(result.hasSuccessValue(),"Is Not Success"),
                () -> assertTrue(result.hasFailureValue(),"Is Failure")
        );

        return (Failure<?,?>) result;
    }


    private void assertOutcomeIsSuccess(Outcome<?, ?> result)
    {
        if (result instanceof Failure<?,?>)
        {
            fail("Result should be a Success");
        }

        assertAll("Result",
                () -> assertTrue(result.hasSuccessValue(),"Has Success value"),
                () -> assertFalse(result.hasFailureValue(),"Does not have Failure value")
        );

    }


}
