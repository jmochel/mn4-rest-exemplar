package org.saltations.mre.persons.mapping;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import io.micronaut.core.beans.BeanProperty;
import jakarta.validation.constraints.Min;
import org.saltations.mre.persons.model.Person;

import java.util.function.Predicate;

/**
 * Represents ValidationType // TODO Document what ValidationType represents
 * <p>
 * Responsible for   // TODO Document ValidationType responsibilities, if any
 * <ol>
 *  <li></li>
 * </ol>
 * <p>
 * Collaborates with // TODO Document ValidationType collaborators, if any
 */

public enum ValidationType
{
//    STRING_NOT_BLANK(NotBlank.class, PersonValidationFixture::hasNotBlankConstraint),
//    NOT_NULL(NotNull.class, PersonValidationFixture::hasNotNullConstraint),
//    STRING_NOT_EMPTY(NotEmpty.class, PersonValidationFixture::hasNotEmptyAnnotation),
    NUMERIC_MIN(Min.class, PersonValidationFixture::hasMinConstraint);

    public final Class<?> annotationClass;
    public final Predicate<BeanProperty<Person, Object>> hasConstraint;

    ValidationType(Class<?> annotationClass, Predicate<BeanProperty<Person, Object>> hasConstraint)
    {
        this.annotationClass = annotationClass;
        this.hasConstraint = hasConstraint;
    }

    public Class<?> getAnnotationClass()
    {
        return annotationClass;
    }

    public static Multimap<Class<?>,ValidationType> mapOfAnnotationToValidationType()
    {
        Multimap<Class<?>,ValidationType> mapOfAnnotationToValidationType = ArrayListMultimap.create();

        var enums = Lists.newArrayList(ValidationType.values());

        for (ValidationType vt : enums)
        {
                mapOfAnnotationToValidationType.put(vt.getAnnotationClass(), vt);
        }

        return mapOfAnnotationToValidationType;
    }

    static boolean hasConstraints(BeanProperty<Person, Object> bp)
    {
        return bp.getAnnotationNames().stream().anyMatch(an -> an.contains("jakarta.validation.constraints"));
    }


}
