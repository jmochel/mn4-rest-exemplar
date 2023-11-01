package org.saltations.mre.persons.mapping;

import io.micronaut.core.beans.BeanProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.saltations.mre.persons.model.Person;
import org.saltations.mre.persons.model.PersonOracle;

import java.util.function.Predicate;

/**
 * Represents ValidationFixture // TODO Document what ValidationFixture represents
 * <p>
 * Responsible for   // TODO Document ValidationFixture responsibilities, if any
 * <ol>
 *  <li></li>
 * </ol>
 * <p>
 * Collaborates with // TODO Document ValidationFixture collaborators, if any
 */

public class PersonValidationFixture
{
    static boolean hasMinConstraint(BeanProperty<Person, Object> beanProperty)
    {
        return beanProperty.hasAnnotation(Min.class);
    }

//    static boolean hasNotBlankConstraint(BeanProperty<Person, Object> beanProperty)
//    {
//        return beanProperty.hasAnnotation(NotBlank.class);
//    }
//
//    static boolean hasNotNullConstraint(BeanProperty<Person, Object> beanProperty)
//    {
//        return beanProperty.hasAnnotation(NotNull.class);
//    }
//
//    static boolean hasNotEmptyAnnotation(BeanProperty<Person, Object> beanProperty)
//    {
//        return beanProperty.hasAnnotation(NotEmpty.class);
//    }

}


