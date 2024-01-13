package org.saltations.mre.core.control;

import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.saltations.mre.core.ReplaceBDDCamelCase;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

// Embracing Failure as Expected Outcomes
@DisplayNameGeneration(ReplaceBDDCamelCase.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OperationResultExampleTest
{
    @Nested
    @Order(10)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class WhenFulfilledWithValue
    {
        OperationResult<String, Integer> opResult = OperationResult.fulfilled("Successful!");

        @Test
        @Order(2)
        public void hasAppropriateValue()
        {
            assertAll(
                    () -> assertTrue(opResult.isSuccess(),"Success"),
                    () -> assertFalse(opResult.isError(),"Error"),
                    () -> assertEquals("Successful!", opResult.value, "Value"),
                    () -> assertNull(opResult.errorCode, "Error Code"),
                    () -> assertNull(opResult.thrown, "Thrown")
            );
        }

        @Test
        @Order(4)
        public void canTransformToAnotherFulfillmentOfSameValueType()
        {
            var newOpResult = opResult.mapFulfillment(val -> "Banzai");

            assertAll(
                    () -> assertTrue(newOpResult.isSuccess(),"Success"),
                    () -> assertFalse(newOpResult.isError(),"Error"),
                    () -> assertEquals("Banzai", newOpResult.value, "Value"),
                    () -> assertNull(newOpResult.errorCode, "Error Code"),
                    () -> assertNull(newOpResult.thrown, "Thrown")
            );
        }

        @Test
        @Order(6)
        public void canTransformToFulfillmentOfDifferentValueType()
        {
            var newOpResult = opResult.mapFulfillment(val -> Integer.valueOf(21));

            assertAll(
                    () -> assertTrue(newOpResult.isSuccess(),"Success"),
                    () -> assertFalse(newOpResult.isError(),"Error"),
                    () -> assertEquals(21, newOpResult.value, "Value"),
                    () -> assertNull(newOpResult.errorCode, "Error Code"),
                    () -> assertNull(newOpResult.thrown, "Thrown")
            );
        }

        @Test
        @Order(8)
        public void canUseExistingValueWhenProvidedAnAlternate()
        {
            // Should ignore the supplied alternate value
            var value = opResult.elseValue("So not bogus!");
            assertEquals("Successful!", value, "Value");
        }

        @Test
        @Order(10)
        public void canUseExistingFulfillmentWhenProvidedFromSupplier()
        {
            // Should ignore the supplied alternate value
            var newOpResult = opResult.orSuppliedResult(() -> "New Banzai");

            assertAll(
                    () -> assertTrue(newOpResult.isSuccess(),"Success"),
                    () -> assertFalse(newOpResult.isError(),"Error"),
                    () -> assertEquals("Successful!", newOpResult.value, "Value"),
                    () -> assertNull(newOpResult.errorCode, "Error Code"),
                    () -> assertNull(newOpResult.thrown, "Thrown")
            );
        }

        @Test
        @Order(12)
        public void canUseExistingFulfillmentWhenAskedToThrowIfAnError()
        {
            // Should ignore the supplied exception
            assertDoesNotThrow(() -> opResult.orThrow(supplyIllegalArgumentException()));
        }

        @Test
        @Order(14)
        public void canCreateNewFulfillmentFromProvidedTransforms()
        {
            var newOpResult = opResult.map(val -> Integer.valueOf(1211), code -> 888);

            assertAll(
                    () -> assertTrue(newOpResult.isSuccess(),"Success"),
                    () -> assertFalse(newOpResult.isError(),"Error"),
                    () -> assertEquals(1211, newOpResult.value, "Value"),
                    () -> assertNull(newOpResult.errorCode, "Error Code"),
                    () -> assertNull(newOpResult.thrown, "Thrown")
            );
        }

        @Test
        @Order(16)
        public void canCreateNewFulfillmentFromProvidedTransform()
        {
            var newOpResult = opResult.mapFulfillment(val -> Integer.valueOf(1211));

            assertAll(
                    () -> assertTrue(newOpResult.isSuccess(),"Success"),
                    () -> assertFalse(newOpResult.isError(),"Error"),
                    () -> assertEquals(1211, newOpResult.value, "Value"),
                    () -> assertNull(newOpResult.errorCode, "Error Code"),
                    () -> assertNull(newOpResult.thrown, "Thrown")
            );
        }

        @Test
        @Order(18)
        public void canUseExistingFulfillmentFromProvidedErrorTransform()
        {
            var newOpResult = opResult.mapError(code -> Integer.valueOf(999));

            assertAll(
                    () -> assertTrue(newOpResult.isSuccess(),"Success"),
                    () -> assertFalse(newOpResult.isError(),"Error"),
                    () -> assertEquals("Successful!", newOpResult.value, "Value"),
                    () -> assertNull(newOpResult.errorCode, "Error Code"),
                    () -> assertNull(newOpResult.thrown, "Thrown")
            );
        }

    }

    @Nested
    @Order(20)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class WhenFulfilledWithNoValue
    {
        OperationResult<String, Integer> opResult = OperationResult.fulfilled(null);


        @Test
        @Order(2)
        public void hasAppropriateValue()
        {
            assertAll(
                    () -> assertTrue(opResult.isSuccess(),"Success"),
                    () -> assertFalse(opResult.isError(),"Error"),
                    () -> assertNull(opResult.value, "Value"),
                    () -> assertNull(opResult.errorCode, "Error Code"),
                    () -> assertNull(opResult.thrown, "Thrown")
            );
        }

        @Test
        @Order(4)
        public void canTransformToAnotherFulfillmentOfSameValueType()
        {
            var newOpResult = opResult.mapFulfillment(val -> "Banzai");

            assertAll(
                    () -> assertTrue(newOpResult.isSuccess(),"Success"),
                    () -> assertFalse(newOpResult.isError(),"Error"),
                    () -> assertEquals("Banzai", newOpResult.value, "Value"),
                    () -> assertNull(newOpResult.errorCode, "Error Code"),
                    () -> assertNull(newOpResult.thrown, "Thrown")
            );
        }

        @Test
        @Order(6)
        public void canTransformToFulfillmentOfDifferentValueType()
        {
            var newOpResult = opResult.mapFulfillment(val -> Integer.valueOf(21));

            assertAll(
                    () -> assertTrue(newOpResult.isSuccess(),"Success"),
                    () -> assertFalse(newOpResult.isError(),"Error"),
                    () -> assertEquals(21, newOpResult.value, "Value"),
                    () -> assertNull(newOpResult.errorCode, "Error Code"),
                    () -> assertNull(newOpResult.thrown, "Thrown")
            );
        }

        @Test
        @Order(8)
        public void canUseExistingValue()
        {
            // Should ignore the supplied alternate value
            var value = opResult.elseValue("So not bogus!");
            assertNull(value, "Value");
        }

        @Test
        @Order(10)
        public void canUseExistingFulfillmentWhenProvidedFromSupplier()
        {
//            assertTrue(false);
            // Should ignore the supplied alternate value
            var newOpResult = opResult.orSuppliedResult(() -> "New Banzai");

            assertAll(
                    () -> assertTrue(newOpResult.isSuccess(),"Success"),
                    () -> assertFalse(newOpResult.isError(),"Error"),
                    () -> assertNull(newOpResult.value, "Value"),
                    () -> assertNull(newOpResult.errorCode, "Error Code"),
                    () -> assertNull(newOpResult.thrown, "Thrown")
            );
        }

        @Test
        @Order(12)
        public void canUseExistingFulfillmentWhenAskedToThrowIfAnError()
        {
            // Should ignore the supplied exception
            assertDoesNotThrow(() -> opResult.orThrow(supplyIllegalArgumentException()));
        }

        @Test
        @Order(14)
        public void canCreateNewFulfillmentFromProvidedTransforms()
        {
            var newOpResult = opResult.map(val -> Integer.valueOf(1211), code -> 888);

            assertAll(
                    () -> assertTrue(newOpResult.isSuccess(),"Success"),
                    () -> assertFalse(newOpResult.isError(),"Error"),
                    () -> assertEquals(1211, newOpResult.value, "Value"),
                    () -> assertNull(newOpResult.errorCode, "Error Code"),
                    () -> assertNull(newOpResult.thrown, "Thrown")
            );
        }

        @Test
        @Order(16)
        public void canCreateNewFulfillmentFromProvidedTransform()
        {
            var newOpResult = opResult.mapFulfillment(val -> Integer.valueOf(1211));

            assertAll(
                    () -> assertTrue(newOpResult.isSuccess(),"Success"),
                    () -> assertFalse(newOpResult.isError(),"Error"),
                    () -> assertEquals(1211, newOpResult.value, "Value"),
                    () -> assertNull(newOpResult.errorCode, "Error Code"),
                    () -> assertNull(newOpResult.thrown, "Thrown")
            );
        }

        @Test
        @Order(18)
        public void canUseExistingFulfillmentFromProvidedErrorTransform()
        {
            var newOpResult = opResult.mapError(code -> Integer.valueOf(999));

            assertAll(
                    () -> assertTrue(newOpResult.isSuccess(),"Success"),
                    () -> assertFalse(newOpResult.isError(),"Error"),
                    () -> assertEquals(null, newOpResult.value, "Value"),
                    () -> assertNull(newOpResult.errorCode, "Error Code"),
                    () -> assertNull(newOpResult.thrown, "Thrown")
            );
        }
    }



    @Nested
    @Order(20)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class WhenFulfilledWithVoidValue
    {
        OperationResult<Void, Integer> opResult = OperationResult.fulfilled();

        @Test
        @Order(2)
        public void hasAppropriateValue()
        {
            assertAll(
                    () -> assertTrue(opResult.isSuccess(),"Success"),
                    () -> assertFalse(opResult.isError(),"Error"),
                    () -> assertNull(opResult.value, "Value"),
                    () -> assertNull(opResult.errorCode, "Error Code"),
                    () -> assertNull(opResult.thrown, "Thrown")
            );
        }

        @Test
        @Order(4)
        public void canTransformToAnotherFulfillmentOfSameValueType()
        {
            var newOpResult = opResult.mapFulfillment(val -> "Banzai");

            assertAll(
                    () -> assertTrue(newOpResult.isSuccess(),"Success"),
                    () -> assertFalse(newOpResult.isError(),"Error"),
                    () -> assertEquals("Banzai", newOpResult.value, "Value"),
                    () -> assertNull(newOpResult.errorCode, "Error Code"),
                    () -> assertNull(newOpResult.thrown, "Thrown")
            );
        }

        @Test
        @Order(6)
        public void canTransformToFulfillmentOfDifferentValueType()
        {
            var newOpResult = opResult.mapFulfillment(val -> Integer.valueOf(21));

            assertAll(
                    () -> assertTrue(newOpResult.isSuccess(),"Success"),
                    () -> assertFalse(newOpResult.isError(),"Error"),
                    () -> assertEquals(21, newOpResult.value, "Value"),
                    () -> assertNull(newOpResult.errorCode, "Error Code"),
                    () -> assertNull(newOpResult.thrown, "Thrown")
            );
        }

        @Test
        @Order(8)
        public void canUseExistingValueWhenProvidedAnAlternate()
        {
//          var value = opResult.elseValue("So not bogus!");
//          Cannot be done since Void does not allow for an alternate
        }

        @Test
        @Order(10)
        public void canUseExistingFulfillmentWhenProvidedFromSupplier()
        {
//           var newOpResult = opResult.orSupplied(() -> "New Banzai");
//          Cannot be done since Void does not allow for an alternate
        }

        @Test
        @Order(12)
        public void canUseExistingFulfillmentWhenAskedToThrowIfAnError()
        {
            // Should ignore the supplied exception
            assertDoesNotThrow(() -> opResult.orThrow(supplyIllegalArgumentException()));
        }

        @Test
        @Order(14)
        public void canCreateNewFulfillmentFromProvidedTransforms()
        {
            var newOpResult = opResult.map(val -> Integer.valueOf(1211), code -> 888);

            assertAll(
                    () -> assertTrue(newOpResult.isSuccess(),"Success"),
                    () -> assertFalse(newOpResult.isError(),"Error"),
                    () -> assertEquals(1211, newOpResult.value, "Value"),
                    () -> assertNull(newOpResult.errorCode, "Error Code"),
                    () -> assertNull(newOpResult.thrown, "Thrown")
            );
        }

        @Test
        @Order(16)
        public void canCreateNewFulfillmentFromProvidedTransform()
        {
            var newOpResult = opResult.mapFulfillment(val -> Integer.valueOf(1211));

            assertAll(
                    () -> assertTrue(newOpResult.isSuccess(),"Success"),
                    () -> assertFalse(newOpResult.isError(),"Error"),
                    () -> assertEquals(1211, newOpResult.value, "Value"),
                    () -> assertNull(newOpResult.errorCode, "Error Code"),
                    () -> assertNull(newOpResult.thrown, "Thrown")
            );
        }

        @Test
        @Order(18)
        public void canUseExistingFulfillmentFromProvidedErrorTransform()
        {
            var newOpResult = opResult.mapError(code -> Integer.valueOf(999));

            assertAll(
                    () -> assertTrue(newOpResult.isSuccess(),"Success"),
                    () -> assertFalse(newOpResult.isError(),"Error"),
                    () -> assertEquals(null, newOpResult.value, "Value"),
                    () -> assertNull(newOpResult.errorCode, "Error Code"),
                    () -> assertNull(newOpResult.thrown, "Thrown")
            );
        }
    }


    @Nested
    @Order(30)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class WhenErrorFromCode
    {
        OperationResult<String, Integer> opResult = OperationResult.error(666, null);


        @Test
        @Order(2)
        public void hasRequiredProperties()
        {

            assertAll(
                    () -> assertTrue(opResult.isError(),"Is Error"),
                    () -> assertFalse(opResult.isSuccess(), "Success"),
                    () -> assertNull(opResult.value, "Value"),
                    () -> assertEquals(666, opResult.errorCode, "Error Code"),
                    () -> assertEquals(null, opResult.thrown, "Thrown")
            );
        }

        @Test
        @Order(4)
        public void canThrowOnError() throws Throwable
        {
            var ex = assertThrows(IllegalArgumentException.class,
                    () -> opResult.orThrow(supplyIllegalArgumentException()));
        }

        @Test
        @Order(6)
        public void canProvideAlternateValue()
        {
            var value = opResult.elseValue("bogus");

            assertEquals("bogus", value, "Value");
        }

        @Test
        @Order(8)
        public void canProvideAlternateValueFromSupplier()
        {
            var value = opResult.elseValue(() -> "bogus");

            assertEquals("bogus", value, "Value");
        }


        @Test
        @Order(10)
        public void canUseFulfillmentWhenProvidedFromSupplier()
        {
//            assertTrue(false);
            var newOpResult = opResult.orSuppliedResult(() -> "New Banzai");

            assertAll(
                    () -> assertTrue(newOpResult.isSuccess(),"Success"),
                    () -> assertFalse(newOpResult.isError(),"Error"),
                    () -> assertEquals("New Banzai", newOpResult.value, "Value"),
                    () -> assertNull(newOpResult.errorCode, "Error Code"),
                    () -> assertNull(newOpResult.thrown, "Thrown")
            );
        }


        @Test
        @Order(12)
        public void willThrowProvidedExceptionWhenAskedToThrowIfAnError()
        {
            // Should ignore the supplied exception
            assertThrows(IllegalArgumentException.class, () -> opResult.orThrow(supplyIllegalArgumentException()));
        }

        @Test
        @Order(14)
        public void canCreateNewErrorFromProvidedTransforms()
        {
            var newOpResult = opResult.map(val -> Integer.valueOf(1211), code -> 888);

            assertAll(
                    () -> assertFalse(newOpResult.isSuccess(),"Success"),
                    () -> assertTrue(newOpResult.isError(),"Error"),
                    () -> assertEquals(null, newOpResult.value, "Value"),
                    () -> assertEquals(888, newOpResult.errorCode, "Error Code"),
                    () -> assertNull(newOpResult.thrown, "Thrown")
            );
        }

        @Test
        @Order(16)
        public void canUseExistingErrorWhenProvidedFulfillmentTransform()
        {
            var newOpResult = opResult.mapFulfillment(val -> Integer.valueOf(1211));

            assertAll(
                    () -> assertFalse(newOpResult.isSuccess(),"Success"),
                    () -> assertTrue(newOpResult.isError(),"Error"),
                    () -> assertEquals(null, newOpResult.value, "Value"),
                    () -> assertEquals(666, newOpResult.errorCode, "Error Code"),
                    () -> assertNull(newOpResult.thrown, "Thrown")
            );
        }

        @Test
        @Order(18)
        public void canCreateNewErrorFromProvidedErrorTransform()
        {
            var newOpResult = opResult.mapError(code -> Integer.valueOf(999));

            assertAll(
                    () -> assertFalse(newOpResult.isSuccess(),"Success"),
                    () -> assertTrue(newOpResult.isError(),"Error"),
                    () -> assertEquals(null, newOpResult.value, "Value"),
                    () -> assertEquals(999, newOpResult.errorCode, "Error Code"),
                    () -> assertNull(newOpResult.thrown, "Thrown")
            );
        }
    }

    @Nested
    @Order(40)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class WhenErrorFromException
    {
        OperationResult<String, Integer> opResult = OperationResult.error(null, new IllegalArgumentException("Whoops"));


        @Test
        @Order(2)
        public void hasRequiredProperties()
        {
            assertAll(
                    () -> assertTrue(opResult.isError(),"Is Error"),
                    () -> assertFalse(opResult.isSuccess(), "Success"),
                    () -> assertNull(opResult.value, "Value"),
                    () -> assertNull(opResult.errorCode, "Error Code"),
                    () -> assertNotNull(opResult.thrown, "Thrown")
            );
        }

        @Test
        @Order(4)
        public void canThrowOnError() throws Throwable
        {
            var ex = assertThrows(IllegalArgumentException.class,
                    () -> opResult.orThrow(supplyIllegalArgumentException()));
        }

        @Test
        @Order(6)
        public void canProvideAlternateValue()
        {
            var value = opResult.elseValue("bogus");

            assertEquals("bogus", value, "Value");
        }

        @Test
        @Order(8)
        public void canProvideAlternateValueFromSupplier()
        {
            var value = opResult.elseValue(() -> "bogus");

            assertEquals("bogus", value, "Value");
        }

        @Test
        @Order(10)
        public void canUseFulfillmentWhenProvidedFromSupplier()
        {
//            assertTrue(false);
            var newOpResult = opResult.orSuppliedResult(() -> "New Banzai");

            assertAll(
                    () -> assertTrue(newOpResult.isSuccess(),"Success"),
                    () -> assertFalse(newOpResult.isError(),"Error"),
                    () -> assertEquals("New Banzai", newOpResult.value, "Value"),
                    () -> assertNull(newOpResult.errorCode, "Error Code"),
                    () -> assertNull(newOpResult.thrown, "Thrown")
            );
        }

        @Test
        @Order(12)
        public void willThrowProvidedExceptionWhenAskedToThrowIfAnError()
        {
            // Should ignore the supplied exception
            assertThrows(IllegalArgumentException.class, () -> opResult.orThrow(supplyIllegalArgumentException()));
        }

        @Test
        @Order(14)
        public void canCreateNewErrorFromProvidedTransforms()
        {
            var newOpResult = opResult.map(val -> Integer.valueOf(1211), code -> 888);

            assertAll(
                    () -> assertFalse(newOpResult.isSuccess(),"Success"),
                    () -> assertTrue(newOpResult.isError(),"Error"),
                    () -> assertEquals(null, newOpResult.value, "Value"),
                    () -> assertEquals(888, newOpResult.errorCode, "Error Code"),
                    () -> assertNotNull(newOpResult.thrown, "Thrown")
            );
        }

        @Test
        @Order(16)
        public void canUseExistingErrorWhenProvidedFulfillmentTransform()
        {
            var newOpResult = opResult.mapFulfillment(val -> Integer.valueOf(1211));

            assertAll(
                    () -> assertFalse(newOpResult.isSuccess(),"Success"),
                    () -> assertTrue(newOpResult.isError(),"Error"),
                    () -> assertEquals(null, newOpResult.value, "Value"),
                    () -> assertEquals(null, newOpResult.errorCode, "Error Code"),
                    () -> assertNotNull(newOpResult.thrown, "Thrown")
            );
        }

        @Test
        @Order(18)
        public void canCreateNewErrorFromProvidedErrorTransform()
        {
            var newOpResult = opResult.mapError(code -> Integer.valueOf(999));

            assertAll(
                    () -> assertFalse(newOpResult.isSuccess(),"Success"),
                    () -> assertTrue(newOpResult.isError(),"Error"),
                    () -> assertEquals(null, newOpResult.value, "Value"),
                    () -> assertEquals(999, newOpResult.errorCode, "Error Code"),
                    () -> assertNotNull(newOpResult.thrown, "Thrown")
            );
        }
    }


    @Nested
    @Order(50)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class WhenErrorFromExceptionAndCode
    {
        OperationResult<String, Integer> opResult = OperationResult.error(666, new IllegalArgumentException("Whoops"));


        @Test
        @Order(2)
        public void hasRequiredProperties()
        {
            assertAll(
                    () -> assertTrue(opResult.isError(),"Is Error"),
                    () -> assertFalse(opResult.isSuccess(), "Success"),
                    () -> assertNull(opResult.value, "Value"),
                    () -> assertEquals(666, opResult.errorCode, "Error Code"),
                    () -> assertNotNull(opResult.thrown, "Thrown")
            );
        }

        @Test
        @Order(4)
        public void canThrowOnError() throws Throwable
        {
            var ex = assertThrows(IllegalArgumentException.class,
                    () -> opResult.orThrow(supplyIllegalArgumentException()));
        }

        @Test
        @Order(6)
        public void canProvideAlternateValue()
        {
            var value = opResult.elseValue("bogus");

            assertEquals("bogus", value, "Value");
        }

        @Test
        @Order(8)
        public void canProvideAlternateValueFromSupplier()
        {
            var value = opResult.elseValue(() -> "bogus");

            assertEquals("bogus", value, "Value");
        }

        @Test
        @Order(10)
        public void canUseFulfillmentWhenProvidedFromSupplier()
        {
//            assertTrue(false);
            var newOpResult = opResult.orSuppliedResult(() -> "Banzai");

            assertAll(
                    () -> assertTrue(newOpResult.isSuccess(),"Success"),
                    () -> assertFalse(newOpResult.isError(),"Error"),
                    () -> assertEquals("Banzai", newOpResult.value, "Value"),
                    () -> assertNull(newOpResult.errorCode, "Error Code"),
                    () -> assertNull(newOpResult.thrown, "Thrown")
            );
        }

        @Test
        @Order(12)
        public void willThrowProvidedExceptionWhenAskedToThrowIfAnError()
        {
            // Should ignore the supplied exception
            assertThrows(IllegalArgumentException.class, () -> opResult.orThrow(supplyIllegalArgumentException()));
        }

        @Test
        @Order(14)
        public void canCreateNewErrorFromProvidedTransforms()
        {
            var newOpResult = opResult.map(val -> Integer.valueOf(1211), code -> 888);

            assertAll(
                    () -> assertFalse(newOpResult.isSuccess(),"Success"),
                    () -> assertTrue(newOpResult.isError(),"Error"),
                    () -> assertEquals(null, newOpResult.value, "Value"),
                    () -> assertEquals(888, newOpResult.errorCode, "Error Code"),
                    () -> assertNotNull(newOpResult.thrown, "Thrown")
            );
        }

        @Test
        @Order(16)
        public void canUseExistingErrorWhenProvidedFulfillmentTransform()
        {
            var newOpResult = opResult.mapFulfillment(val -> Integer.valueOf(1211));

            assertAll(
                    () -> assertFalse(newOpResult.isSuccess(),"Success"),
                    () -> assertTrue(newOpResult.isError(),"Error"),
                    () -> assertEquals(null, newOpResult.value, "Value"),
                    () -> assertEquals(666, newOpResult.errorCode, "Error Code"),
                    () -> assertNotNull(newOpResult.thrown, "Thrown")
            );
        }

        @Test
        @Order(18)
        public void canCreateNewErrorFromProvidedErrorTransform()
        {
            var newOpResult = opResult.mapError(code -> Integer.valueOf(999));

            assertAll(
                    () -> assertFalse(newOpResult.isSuccess(),"Success"),
                    () -> assertTrue(newOpResult.isError(),"Error"),
                    () -> assertEquals(null, newOpResult.value, "Value"),
                    () -> assertEquals(999, newOpResult.errorCode, "Error Code"),
                    () -> assertNotNull(newOpResult.thrown, "Thrown")
            );
        }
    }


    @Nested
    @Order(50)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class WhenErrorFromThrowable
    {
        OperationResult<Integer, Integer> opResult = OperationResult.ofThrowable(() -> Integer.valueOf(1)/Integer.valueOf(0));


        @Test
        @Order(2)
        public void hasRequiredProperties()
        {
            assertAll(
                    () -> assertTrue(opResult.isError(),"Is Error"),
                    () -> assertFalse(opResult.isSuccess(),"Is Success"),
                    () -> assertNull(opResult.value, "Value"),
                    () -> assertNull(opResult.errorCode, "Error Code"),
                    () -> assertNotNull(opResult.thrown, "Thrown")
            );
        }

        @Test
        @Order(4)
        public void canThrowOnError() throws Throwable
        {
            var ex = assertThrows(IllegalArgumentException.class,
                    () -> opResult.orThrow(supplyIllegalArgumentException()));
        }

        @Test
        @Order(6)
        public void canProvideAlternateValue()
        {
            var value = opResult.elseValue(666);

            assertEquals(666, value, "Value");
        }

        @Test
        @Order(8)
        public void canProvideAlternateValueFromSupplier()
        {
            var value = opResult.elseValue(() -> 666);

            assertEquals(666, value, "Value");
        }

        @Test
        @Order(10)
        public void canUseFulfillmentWhenProvidedFromSupplier()
        {
//            assertTrue(false);
            var newOpResult = opResult.orSuppliedResult(() -> 121);

            assertAll(
                    () -> assertTrue(newOpResult.isSuccess(),"Success"),
                    () -> assertFalse(newOpResult.isError(),"Error"),
                    () -> assertEquals(121, newOpResult.value, "Value"),
                    () -> assertNull(newOpResult.errorCode, "Error Code"),
                    () -> assertNull(newOpResult.thrown, "Thrown")
            );
        }

        @Test
        @Order(12)
        public void willThrowProvidedExceptionWhenAskedToThrowIfAnError()
        {
            // Should ignore the supplied exception
            assertThrows(IllegalArgumentException.class, () -> opResult.orThrow(supplyIllegalArgumentException()));
        }

        @Test
        @Order(14)
        public void canCreateNewErrorFromProvidedTransforms()
        {
            var newOpResult = opResult.map(val -> Integer.valueOf(1211), code -> 888);

            assertAll(
                    () -> assertFalse(newOpResult.isSuccess(),"Success"),
                    () -> assertTrue(newOpResult.isError(),"Error"),
                    () -> assertEquals(null, newOpResult.value, "Value"),
                    () -> assertEquals(888, newOpResult.errorCode, "Error Code"),
                    () -> assertNotNull(newOpResult.thrown, "Thrown")
            );
        }

        @Test
        @Order(16)
        public void canUseExistingErrorWhenProvidedFulfillmentTransform()
        {
            var newOpResult = opResult.mapFulfillment(val -> Integer.valueOf(1211));

            assertAll(
                    () -> assertFalse(newOpResult.isSuccess(),"Success"),
                    () -> assertTrue(newOpResult.isError(),"Error"),
                    () -> assertEquals(null, newOpResult.value, "Value"),
                    () -> assertEquals(null, newOpResult.errorCode, "Error Code"),
                    () -> assertNotNull(newOpResult.thrown, "Thrown")
            );
        }

        @Test
        @Order(18)
        public void canCreateNewErrorFromProvidedErrorTransform()
        {
            var newOpResult = opResult.mapError(code -> Integer.valueOf(999));

            assertAll(
                    () -> assertFalse(newOpResult.isSuccess(),"Success"),
                    () -> assertTrue(newOpResult.isError(),"Error"),
                    () -> assertEquals(null, newOpResult.value, "Value"),
                    () -> assertEquals(999, newOpResult.errorCode, "Error Code"),
                    () -> assertNotNull(newOpResult.thrown, "Thrown")
            );
        }

    }

    @Nested
    @Order(60)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class WhenErrorFromCodeForVoidValue
    {
        OperationResult<Void, Integer> opResult = OperationResult.error(666, null);


        @Test
        @Order(2)
        public void hasRequiredProperties()
        {
            assertAll(
                    () -> assertTrue(opResult.isError(),"Is Error"),
                    () -> assertFalse(opResult.isSuccess(), "Success"),
                    () -> assertNull(opResult.value, "Value"),
                    () -> assertEquals(666, opResult.errorCode, "Error Code"),
                    () -> assertEquals(null, opResult.thrown, "Thrown")
            );
        }

        @Test
        @Order(4)
        public void canThrowOnError() throws Throwable
        {
            var ex = assertThrows(IllegalArgumentException.class,
                    () -> opResult.orThrow(supplyIllegalArgumentException()));
        }

        @Test
        @Order(6)
        public void canProvideAlternateValue()
        {
            var value = opResult.mapFulfillment(val -> "").elseValue("bogus");

            assertEquals("bogus", value, "Value");
        }

        @Test
        @Order(8)
        public void canProvideAlternateValueFromSupplier()
        {
            var value = opResult.mapFulfillment(val -> "").elseValue(() -> "bogus");

            assertEquals("bogus", value, "Value");
        }

        @Test
        @Order(10)
        public void canUseFulfillmentWhenProvidedFromSupplier()
        {
//           var newOpResult = opResult.orSuppliedResult(() -> "New Banzai");
//          Cannot be done since Void does not allow for an alternate
        }

        @Test
        @Order(12)
        public void willThrowProvidedExceptionWhenAskedToThrowIfAnError()
        {
            // Should ignore the supplied exception
            assertThrows(IllegalArgumentException.class, () -> opResult.orThrow(supplyIllegalArgumentException()));
        }

        @Test
        @Order(14)
        public void canCreateNewErrorFromProvidedTransforms()
        {
            var newOpResult = opResult.map(val -> Integer.valueOf(1211), code -> 888);

            assertAll(
                    () -> assertFalse(newOpResult.isSuccess(),"Success"),
                    () -> assertTrue(newOpResult.isError(),"Error"),
                    () -> assertEquals(null, newOpResult.value, "Value"),
                    () -> assertEquals(888, newOpResult.errorCode, "Error Code"),
                    () -> assertNull(newOpResult.thrown, "Thrown")
            );
        }

        @Test
        @Order(16)
        public void canUseExistingErrorWhenProvidedFulfillmentTransform()
        {
            var newOpResult = opResult.mapFulfillment(val -> Integer.valueOf(1211));

            assertAll(
                    () -> assertFalse(newOpResult.isSuccess(),"Success"),
                    () -> assertTrue(newOpResult.isError(),"Error"),
                    () -> assertEquals(null, newOpResult.value, "Value"),
                    () -> assertEquals(666, newOpResult.errorCode, "Error Code"),
                    () -> assertNull(newOpResult.thrown, "Thrown")
            );
        }

        @Test
        @Order(18)
        public void canCreateNewErrorFromProvidedErrorTransform()
        {
            var newOpResult = opResult.mapError(code -> Integer.valueOf(999));

            assertAll(
                    () -> assertFalse(newOpResult.isSuccess(),"Success"),
                    () -> assertTrue(newOpResult.isError(),"Error"),
                    () -> assertEquals(null, newOpResult.value, "Value"),
                    () -> assertEquals(999, newOpResult.errorCode, "Error Code"),
                    () -> assertNull(newOpResult.thrown, "Thrown")
            );
        }
    }

    @Nested
    @Order(70)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class WhenErrorFromExceptionForVoidValue
    {
        OperationResult<Void, Integer> opResult = OperationResult.error(null, new IllegalArgumentException("Whoops"));


        @Test
        @Order(2)
        public void hasRequiredProperties()
        {
            assertAll(
                    () -> assertTrue(opResult.isError(),"Is Error"),
                    () -> assertFalse(opResult.isSuccess(), "Success"),
                    () -> assertNull(opResult.value, "Value"),
                    () -> assertNull(opResult.errorCode, "Error Code"),
                    () -> assertNotNull(opResult.thrown, "Thrown")
            );
        }

        @Test
        @Order(4)
        public void canThrowOnError() throws Throwable
        {
            var ex = assertThrows(IllegalArgumentException.class,
                    () -> opResult.orThrow(supplyIllegalArgumentException()));
        }

        @Test
        @Order(6)
        public void canProvideAlternateValue()
        {
            var value = opResult.mapFulfillment(val -> "").elseValue("bogus");

            assertEquals("bogus", value, "Value");
        }

        @Test
        @Order(8)
        public void canProvideAlternateValueFromSupplier()
        {
            var value = opResult.mapFulfillment(val -> "").elseValue(() -> "bogus");

            assertEquals("bogus", value, "Value");
        }

        @Test
        @Order(10)
        public void canUseFulfillmentWhenProvidedFromSupplier()
        {
//           var newOpResult = opResult.orSuppliedResult(() -> "New Banzai");
//          Cannot be done since Void does not allow for an alternate
        }

        @Test
        @Order(12)
        public void willThrowProvidedExceptionWhenAskedToThrowIfAnError()
        {
            // Should ignore the supplied exception
            assertThrows(IllegalArgumentException.class, () -> opResult.orThrow(supplyIllegalArgumentException()));
        }

        @Test
        @Order(14)
        public void canCreateNewErrorFromProvidedTransforms()
        {
            var newOpResult = opResult.map(val -> Integer.valueOf(1211), code -> 888);

            assertAll(
                    () -> assertFalse(newOpResult.isSuccess(),"Success"),
                    () -> assertTrue(newOpResult.isError(),"Error"),
                    () -> assertEquals(null, newOpResult.value, "Value"),
                    () -> assertEquals(888, newOpResult.errorCode, "Error Code"),
                    () -> assertNotNull(newOpResult.thrown, "Thrown")
            );
        }


        @Test
        @Order(16)
        public void canUseExistingErrorWhenProvidedFulfillmentTransform()
        {
            var newOpResult = opResult.mapFulfillment(val -> Integer.valueOf(1211));

            assertAll(
                    () -> assertFalse(newOpResult.isSuccess(),"Success"),
                    () -> assertTrue(newOpResult.isError(),"Error"),
                    () -> assertEquals(null, newOpResult.value, "Value"),
                    () -> assertEquals(null, newOpResult.errorCode, "Error Code"),
                    () -> assertNotNull(newOpResult.thrown, "Thrown")
            );
        }

        @Test
        @Order(18)
        public void canCreateNewErrorFromProvidedErrorTransform()
        {
            var newOpResult = opResult.mapError(code -> Integer.valueOf(999));

            assertAll(
                    () -> assertFalse(newOpResult.isSuccess(),"Success"),
                    () -> assertTrue(newOpResult.isError(),"Error"),
                    () -> assertEquals(null, newOpResult.value, "Value"),
                    () -> assertEquals(999, newOpResult.errorCode, "Error Code"),
                    () -> assertNotNull(newOpResult.thrown, "Thrown")
            );
        }
    }

    OperationResult<Void, Integer> OR_Void_Integer_returnsErrorIfNull_EmptyIfBlank_EmptyOtherwise(String id)
    {
        if (id == null)
        {
            return OperationResult.error(666, null);
        }

         return OperationResult.fulfilled(null);
    }

    /**
     * If the value cannot be found it is an error
     */
    OperationResult<String, Integer> OR_String_Integer_returnsIdOrErrorIfBlank(String id)
    {
        if (id.isBlank())
        {
            return OperationResult.error(666, null);
        }

        return OperationResult.fulfilled(id);
    }


    /**
     * If the value cannot be found it is an error
     */
    OperationResult<Void, Integer> OR_Void_Integer_returnsIdOrErrorIfBlank(String id)
    {
        if (id.isBlank())
        {
            return OperationResult.error(666, null);
        }

        return OperationResult.fulfilled(null);
    }

    /**
     * If the value cannot be found it is an error
     */
    OperationResult<String, Integer> findResultThatMightBeThere(String id)
    {
        if (id == null)
        {
            return OperationResult.error(666, null);
        }

        if (id.isBlank())
        {
            return OperationResult.fulfilled(null);
        }

        return OperationResult.fulfilled(id);
    }


    Supplier<Exception> supplyIllegalArgumentException()
    {
        return new Supplier<Exception>() {
            @Override
            public Exception get()
            {
                return new IllegalArgumentException("Whoopsie!");
            }
        };
    }

}
