package org.saltations.mre.core.outcomes.v4;

import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;
import org.junit.jupiter.api.TestMethodOrder;
import org.saltations.mre.core.outcomes.Outcome;
import org.saltations.mre.core.outcomes.Outcomes;
import org.saltations.mre.core.outcomes.XFail;
import org.saltations.mre.fixtures.ReplaceBDDCamelCase;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Validates the functionality of the individual outcome classes and how they are used
 */

@DisplayNameGeneration(ReplaceBDDCamelCase.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
public class OutcomeTest
{
    @Nested
    @Order(2)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class GivenSuccess {

        private final Outcome<XFail, Long> success = Outcomes.succeed(1111L);

        @Test
        @Order(10)
        void whenGettingValueThenReturnsSuccessValue() throws Throwable
        {
            var value = success.get();
            assertEquals(1111L, value, "Success Value");
        }


        @Test
        @Order(20)
        void whenSupplyingOutcomeOnSuccessThenReturnsSuppliedValue() throws Throwable
        {
            var outcome = success.ifSuccess(() -> Outcomes.succeed(2222L));
            assertEquals(2222L, outcome.get(), "Success Value");
        }

        @Test
        @Order(30)
        void whenTransformingOutcomeOnSuccessThenReturnsTransformedOutcomeToNewSuccess() throws Throwable
        {
            var outcome = success.ifSuccessTransform(x -> Outcomes.succeed(x.get() * 3));
            assertEquals(3333L, outcome.get(), "Transformed Outcome");
        }

        @Test
        @Order(32)
        void whenTransformingOutcomeOnSuccessThenReturnsTransformedOutcomeToNewFailure() throws Throwable
        {
            var outcome = success.ifSuccessTransform(x -> Outcomes.fail());
            assertTrue(outcome.hasFailureValue(), "Now a Failure");
        }

        @Test
        @Order(40)
        void whenTakingActionOnSuccessThenTakesAction() throws Throwable
        {
            final AtomicBoolean applied = new AtomicBoolean(false);

            success.onSuccess(x -> applied.getAndSet(true));
            assertTrue(applied.get(), "Action taken");
        }


        @Test
        @Order(50)
        void whenSupplyingOutcomeOnFailureThenReturnsExistingSuccess() throws Throwable
        {
            var outcome = success.ifFailure(() -> Outcomes.succeed(2222L));
            assertTrue(outcome == success, "Existing Success");
        }

        @Test
        @Order(60)
        void whenTransformingOutcomeOnFailureThenReturnsExistingSuccess() throws Throwable
        {
            var outcome = success.ifFailureTransform(x -> Outcomes.succeed(x.get() * 3));
            assertTrue(outcome == success, "Existing Success");
        }


        @Test
        @Order(70)
        void whenTakingActionOnFailureThenTakesNoAction()
        {
            final AtomicBoolean applied = new AtomicBoolean(false);

            success.onFailure(x -> applied.getAndSet(true));
            assertFalse(applied.get(), "Action taken");
        }

        @Test
        @Order(70)
        void whenTakingActionOnBothThenTakesSuccessAction()
        {
            final AtomicBoolean appliedForFailure = new AtomicBoolean(false);
            final AtomicBoolean appliedForSuccess = new AtomicBoolean(false);

            success.on(x -> appliedForSuccess.getAndSet(true), x -> appliedForFailure.getAndSet(true));

            assertTrue(appliedForSuccess.get(), "Success Action taken");
            assertFalse(appliedForFailure.get(), "Failure Action taken");
        }
    }

    @Nested
    @Order(4)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class GivenPartialSuccess {

        private final Outcome<XFail, Long> success = Outcomes.partialSucceed(XFail.of().build(),1111L);

        @Test
        @Order(10)
        void whenGettingValueThenReturnsSuccessValue() throws Throwable
        {
            var value = success.get();
            assertEquals(1111L, value, "Success Value");
        }

        @Test
        @Order(20)
        void whenSupplyingOutcomeOnSuccessThenReturnsSuppliedValue() throws Throwable
        {
            var outcome = success.ifSuccess(() -> Outcomes.succeed(2222L));
            assertEquals(2222L, outcome.get(), "Success Value");
        }

        @Test
        @Order(30)
        void whenTransformingOutcomeOnSuccessThenReturnsTransformedOutcomeToNewSuccess() throws Throwable
        {
            var outcome = success.ifSuccessTransform(x -> Outcomes.succeed(x.get() * 3));
            assertEquals(3333L, outcome.get(), "Transformed Outcome");
        }

        @Test
        @Order(32)
        void whenTransformingOutcomeOnSuccessThenReturnsTransformedOutcomeToNewFailure() throws Throwable
        {
            var outcome = success.ifSuccessTransform(x -> Outcomes.fail());
            assertTrue(outcome.hasFailureValue(), "Now a Failure");
        }

        @Test
        @Order(40)
        void whenTakingActionOnSuccessThenTakesAction() throws Throwable
        {
            final AtomicBoolean applied = new AtomicBoolean(false);
            success.onSuccess(x -> applied.getAndSet(true));
            assertTrue(applied.get(), "Action taken");
        }

        @Test
        @Order(50)
        void whenSupplyingOutcomeOnFailureThenReturnsExistingSuccess() throws Throwable
        {
            var value = success.ifFailure(() -> Outcomes.succeed(2222L));
            assertTrue(value == success, "Existing Success");
        }

        @Test
        @Order(60)
        void whenTransformingOutcomeOnFailureThenReturnsExistingSuccess() throws Throwable
        {
            var value = success.ifFailureTransform(x -> Outcomes.succeed(x.get() * 3));
            assertTrue(value == success, "Existing Success");
        }


        @Test
        @Order(70)
        void whenTakingActionOnFailureThenTakesNoAction()
        {
            final AtomicBoolean applied = new AtomicBoolean(false);
            success.onFailure(x -> applied.getAndSet(true));
            assertFalse(applied.get(), "Action taken");
        }

        @Test
        @Order(80)
        void whenTakingActionOnBothThenTakesBothActions()
        {
            final AtomicBoolean appliedForFailure = new AtomicBoolean(false);
            final AtomicBoolean appliedForSuccess = new AtomicBoolean(false);

            success.on(x -> appliedForSuccess.getAndSet(true), x -> appliedForFailure.getAndSet(true));

            assertTrue(appliedForSuccess.get(), "Success Action taken");
            assertTrue(appliedForFailure.get(), "Failure Action taken");
        }
    }

    @Nested
    @Order(6)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class GivenFailure {

        private final Outcome<XFail, Long> failure = Outcomes.fail();

        @Test
        @Order(10)
        void whenGettingValueThenThrowsException() throws Throwable
        {
            assertThrows(Exception.class, () -> failure.get(), "Cannot get value from a failure");
        }

        @Test
        @Order(20)
        void whenSupplyingOutcomeOnSuccessThenReturnsTheExistingFailure() throws Throwable
        {
            var outcome = failure.ifSuccess(() -> Outcomes.succeed(2222L));
            assertTrue(outcome == failure, "Same failure");
        }

        @Test
        @Order(30)
        void whenTransformingOutcomeOnSuccessThenReturnsTheExistingFailure()
        {
            var outcome = failure.ifSuccessTransform(x -> Outcomes.succeed(x.get() * 3));
            assertTrue(outcome == failure, "Same failure");
        }

        @Test
        @Order(40)
        void whenTakingActionOnSuccessThenDoesNotTakeAction()
        {
            final AtomicBoolean applied = new AtomicBoolean(false);
            failure.onSuccess(x -> applied.getAndSet(true));
            assertFalse(applied.get(), "Action taken");
        }


        @Test
        @Order(50)
        void whenSupplyingValueOnFailureThenReturnsNewOutcome() throws Throwable
        {
            var outcome = failure.ifFailure(() -> Outcomes.succeed(2222L));
            assertEquals(2222L, outcome.get(),"New Outcome");
        }

        @Test
        @Order(60)
        void whenTransformingOutcomeOnFailureThenReturnsNewOutcome() throws Throwable
        {
            var outcome = failure.ifFailureTransform(x -> Outcomes.fail());
            assertFalse(outcome == failure,"New Outcome");
        }


        @Test
        @Order(70)
        void whenTakingActionOnFailureThenTakesAction()
        {
            final AtomicBoolean applied = new AtomicBoolean(false);
            failure.onFailure(x -> applied.getAndSet(true));
            assertTrue(applied.get(), "Action taken");
        }

        @Test
        @Order(80)
        void whenTakingActionOnBothThenTakesFailureAction()
        {
            final AtomicBoolean appliedForFailure = new AtomicBoolean(false);
            final AtomicBoolean appliedForSuccess = new AtomicBoolean(false);

            failure.on(x -> appliedForSuccess.getAndSet(true), x -> appliedForFailure.getAndSet(true));

            assertFalse(appliedForSuccess.get(), "Success Action taken");
            assertTrue(appliedForFailure.get(), "Failure Action taken");
        }
    }

}
