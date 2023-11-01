package org.saltations.mre.persons.mapping;

import io.micronaut.core.beans.BeanProperty;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.validation.validator.Validator;
import jakarta.inject.Inject;

import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.function.Executable;
import org.saltations.mre.core.ReplaceBDDCamelCase;
import org.saltations.mre.persons.model.Person;
import org.saltations.mre.persons.model.PersonCore;
import org.saltations.mre.persons.model.PersonEntity;
import org.saltations.mre.persons.model.PersonOracle;

import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit test for the PersonMapper
 */

@Slf4j
@MicronautTest(transactional = false)
@DisplayNameGeneration(ReplaceBDDCamelCase.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonValidationTest extends ValidationTest<Person, PersonCore, PersonEntity>
{
    private final Validator validator;

    @Inject
    public PersonValidationTest(PersonOracle oracle, Validator validator)
    {
        super(Person.class, oracle);

        this.validator = validator;
    }

    String minNumericTestName(BeanProperty<Person, Object> beanProperty)
    {
        var annotation = beanProperty.getAnnotation(Min.class);
        var minimumValue = annotation.intValue().getAsInt();

        return beanProperty.getName() + " property has a value below " + minimumValue;
    }

    Person exemplarWithInvalidMin(BeanProperty<Person, Object> beanProperty)
    {
        var annotation = beanProperty.getAnnotation(Min.class);
        var minimumValue = annotation.intValue().getAsInt();

        var invalidValue = minimumValue-1;

        var exemplar = entityOracle.coreExemplar();
        beanProperty.set(exemplar, invalidValue);

        return exemplar;
    }

    Executable minNumericTestExecutable(Person invalidExemplar, BeanProperty<Person, Object> beanProperty)
    {

        var violations = validator.validateProperty(invalidExemplar, beanProperty.getName());

        return () ->  {
            assertEquals(1, violations.size(), () -> minNumericTestName(beanProperty));
        };
    }

    @Test
    void basicExample() throws Throwable
    {
        var constrainedProperties = entityOracle.extractCoreProperties().stream()
                .filter(ValidationType::hasConstraints)
                .collect(Collectors.toList());

        var property = constrainedProperties.stream()
                .filter(bp -> bp.getName().equals("age"))
                .findFirst().get();

        // Code to set the invalid value in(prop, proto,

        var annotation = property.getAnnotation(Min.class);

        // Create invalid value (depends on datatype) input [annotation or annotation class, datatype]

        var exemplar = exemplarWithInvalidMin(property);

        // Code to test the violation

        var executeable = minNumericTestExecutable(exemplar,property);
        executeable.execute();
    }

    /**
     * Create a set of tests to be executed. One for each constraint for each property with constraints.
     * <b>Note: The DynamicTests are executed differently than the standard @Tests and do not support lifecycle callbacks. Meaning, the @BeforeEach and the @AfterEach methods will not be called for the DynamicTests</b>
     */
//
//    @TestFactory
//    List<DynamicTest> dynamicValidationTestsFromStream()
//    {
//        List<DynamicTest> tests = new ArrayList<>();
//
//        // For each property
//
//        var properties = oracle.extractProperties().collect(Collectors.toList());
//
//        for (BeanProperty<Person, Object> property : properties)
//        {
//            // For each constraint
//
//            var annotationNames =  property.getDeclaredAnnotationNames();
////
////            annotationNames.stream()
////                    .filter(name -> name.contains("jakarta.validation.constraints"))
////                    .map(name -> property.getAnnotation(name))
////                    .
//
//            for (String annotationName : annotationNames)
//            {
//                if (annotationName.contains("jakarta.validation.constraints"))
//                {
//                    var annotationValue = property.getAnnotation(annotationName);
//
//                    switch (annotationName)
//                    {
//                        case "jakarta.validation.constraints.Min$List":
//
//                            var min = property.getAnnotation(Min.class).getValue(Integer.class).orElse(null);
//
//                            if (min == null)
//                            {
//                                log.warn("Property {} has an @Min annotation with no value");
//                                continue;
//                            }
//
//                            var prototype = oracle.corePrototype();
//
//                            property.set(prototype, min-1);
//
//                            var violations = validator.validate(prototype, Person.class);
//
//                            var test = DynamicTest.dynamicTest(property.getName() + " below minimum", () -> assertEquals(1, violations.size()));
//
//                            tests.add(test);
//                        default:
//                    }
//
//                    log.info("NAME {}", annotationValue.getAnnotationName());
//                }
//            }
//
//        }
//
//        return tests;
//    }
//


//
//    private DynamicTest toValidationTest(Triplet<PersonCore, BeanProperty<Person, Object>, String> triplet)
//    {
//        var prototype = triplet.getValue0();
//        var beanProperty = triplet.getValue1();
//        var annotationName = triplet.getValue2();
//
//        var metaData = beanProperty.getAnnotation("");
//        metaData.
//
//        var annotationSimpleName = beanProperty.getAnnotation(annotationName).getAnnotationName();
//
//        var parameters = nameToParams.get(annotationSimpleName);
//
//        beanProperty.set(prototype, parameters.getValueo);
//        DynamicTest
//    }

    // for each bean property with constraints -> bp
    //  for each constraint -> bp, constraint
    //      create and modify prototype (bp.set(prototype, badValue)
    //          validate prototype -> violations
    //              assert violations contain shouldContains mesg
//
//    @Data
//    @AllArgsConstructor
//    static class Testable<IC>
//    {
//        final IC objectToBeConstrained;
//        final BeanProperty<IC, Object> beanProperty;
//        final String annotSimpleName;
//    }
//
//    static record ValidationParam(String annotSimpleName,
//                                  Object valueThatViolatesConstraint,
//                                  String shouldContain)
//    {};

}
