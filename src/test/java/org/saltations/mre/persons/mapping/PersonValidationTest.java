package org.saltations.mre.persons.mapping;

import com.google.common.collect.Lists;
import io.micronaut.core.beans.BeanProperty;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import jakarta.validation.Validator;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.TestMethodOrder;
import org.saltations.mre.core.ReplaceBDDCamelCase;
import org.saltations.mre.persons.model.Person;
import org.saltations.mre.persons.model.PersonOracle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit test for the PersonMapper
 */

@Slf4j
@MicronautTest(transactional = false)
@DisplayNameGeneration(ReplaceBDDCamelCase.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonValidationTest
{
    @Inject
    private PersonOracle oracle;

    @Inject
    private Validator validator;

    private static Map<String, ValidationParam> nameToParams;

    static {

        List<ValidationParam> params = Lists.newArrayList(
                new ValidationParam("jakarta.validation.constraints.NotEmpty","", "not empty"),
                new ValidationParam("jakarta.validation.constraints.NotBlank"," ", "not blank"),
                new ValidationParam("jakarta.validation.constraints.NotNull",null, "not null")
        );

        nameToParams = params.stream().collect(Collectors.toMap(
                e -> e.annotSimpleName(),
                e -> e
        ));
    }

    @TestFactory
    List<DynamicTest> dynamicValidationTestsFromStream()
    {
        List<DynamicTest> tests = new ArrayList<>();

        var properties = oracle.extractProperties().collect(Collectors.toList());

        for (BeanProperty<Person, Object> property : properties)
        {
            var annotationNames =  property.getDeclaredAnnotationNames();

            for (String annotationName : annotationNames)
            {
                if (annotationName.contains("jakarta.validation.constraints"))
                {
                    var annotationValue = property.getAnnotation(annotationName);

                    switch (annotationName)
                    {
                        case "jakarta.validation.constraints.Min$List":

                            var min = property.getAnnotation(Min.class).getValue(Integer.class).orElse(null);

                            if (min == null)
                            {
                                log.warn("Property {} has an @Min annotation with no value");
                                continue;
                            }

                            var prototype = oracle.corePrototype();
                            property.set(prototype, min--);

                            var violations = validator.validate(prototype, Person.class);

                            var test = DynamicTest.dynamicTest(property.getName() + " below minimum", () -> assertEquals(1, violations.size()));

                            tests.add(test);
                        default:
                    }

                    log.info("NAME {}", annotationValue.getAnnotationName());
                }
            }

        }

        return tests;
    }
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

    @Data
    @AllArgsConstructor
    static class Testable<IC>
    {
        final IC objectToBeConstrained;
        final BeanProperty<IC, Object> beanProperty;
        final String annotSimpleName;
    }

    static record ValidationParam(String annotSimpleName,
                                  Object valueThatViolatesConstraint,
                                  String shouldContain)
    {};

}
