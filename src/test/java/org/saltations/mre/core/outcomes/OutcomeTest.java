package org.saltations.mre.core.outcomes;

import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;
import org.junit.jupiter.api.TestMethodOrder;
import org.saltations.mre.domain.core.outcomes.FailureParticulars;
import org.saltations.mre.domain.core.outcomes.Outcome;
import org.saltations.mre.domain.core.outcomes.Outcomes;
import org.saltations.mre.fixtures.ReplaceBDDCamelCase;

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

        private final Outcome<FailureParticulars, Long> success = Outcomes.success(1111L);

        @Test
        @Order(10)
        void whenGettingValueThenReturnsSuccessValue() throws Throwable
        {
            var value = success.rawSuccessValue();
            assertEquals(1111L, value, "Success Value");
        }


        @Test
        @Order(20)
        void whenSupplyingOutcomeOnSuccessThenReturnsSuppliedValue() throws Throwable
        {
            var outcome = success.ifSuccess(() -> Outcomes.success(2222L));
            assertEquals(2222L, outcome.rawSuccessValue(), "Success Value");
        }

        @Test
        @Order(30)
        void whenTransformingOutcomeOnSuccessThenReturnsTransformedOutcomeToNewSuccess() throws Throwable
        {
            var outcome = success.ifSuccess(x -> Outcomes.success(x.rawSuccessValue() * 3));
            assertEquals(3333L, outcome.rawSuccessValue(), "Transformed Outcome");
        }

        @Test
        @Order(32)
        void whenTransformingOutcomeOnSuccessThenReturnsTransformedOutcomeToNewFailure() throws Throwable
        {
            var outcome = success.ifSuccess(x -> Outcomes.fail());
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
            var outcome = success.ifFailure(() -> Outcomes.success(2222L));
            assertTrue(outcome == success, "Existing Success");
        }

        @Test
        @Order(60)
        void whenTransformingOutcomeOnFailureThenReturnsExistingSuccess() throws Throwable
        {
            var outcome = success.ifFailure(x -> Outcomes.success(x.rawSuccessValue() * 3));
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
    @Order(6)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class GivenFailure {

        private final Outcome<FailureParticulars, Long> failure = Outcomes.fail();

        @Test
        @Order(10)
        void whenGettingValueThenThrowsException() throws Throwable
        {
            assertThrows(Exception.class, () -> failure.potentialSuccessValue(), "Cannot get value from a failure");
        }

        @Test
        @Order(20)
        void whenSupplyingOutcomeOnSuccessThenReturnsTheExistingFailure() throws Throwable
        {
            var outcome = failure.ifSuccess(() -> Outcomes.success(2222L));
            assertTrue(outcome == failure, "Same failure");
        }

        @Test
        @Order(30)
        void whenTransformingOutcomeOnSuccessThenReturnsTheExistingFailure()
        {
            var outcome = failure.ifSuccess(x -> Outcomes.success(x.rawSuccessValue() * 3));
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
            var outcome = failure.ifFailure(() -> Outcomes.success(2222L));
            assertEquals(2222L, outcome.rawSuccessValue(),"New Outcome");
        }

        @Test
        @Order(60)
        void whenTransformingOutcomeOnFailureThenReturnsNewOutcome() throws Throwable
        {
            var outcome = failure.ifFailure(x -> Outcomes.fail());
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
