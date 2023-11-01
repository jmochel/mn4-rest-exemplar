package org.saltations.mre.persons.mapping;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import io.micronaut.core.annotation.AnnotationValue;
import io.micronaut.core.beans.BeanProperty;
import jakarta.validation.constraints.Min;
import org.javatuples.Pair;
import org.saltations.mre.core.EntityOracle;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Represents ValidationTest // TODO Document what ValidationTest represents
 * <p>
 * Responsible for   // TODO Document ValidationTest responsibilities, if any
 * <ol>
 *  <li></li>
 *  <li></li>
 *  <li></li>
 * </ol>
 * <p>
 * Collaborates with // TODO Document ValidationTest collaborators, if any
 */

public class ValidationTest<IC, C extends IC, E extends C>
{
    protected final Class<IC> coreInterfaceClass;

    protected final EntityOracle<IC, C, E> entityOracle;

    private final Map<String, Class<?>> constraintAnnotationByName;

    protected final Multimap<BeanProperty<IC, Object>, AnnotationValue<?>> annotationsByBeanProperty;

    public ValidationTest(Class<IC> coreInterfaceClass, EntityOracle<IC, C, E> entityOracle)
    {
        this.coreInterfaceClass = coreInterfaceClass;
        this.entityOracle = entityOracle;

        this.constraintAnnotationByName = Lists.newArrayList(Min.class)
                .stream()
                .collect(Collectors.toMap(x -> x.getName(), x -> x));

        var annotationAndBeanPropertyPairings =  entityOracle.extractCoreProperties().stream()
                .filter(this::hasAConstraintAnnotation)
                .flatMap(this::extractConstraintAnnotations)
                .collect(Collectors.toList());

        // Expand to BP, Constraint Pairings

//        TODO annotationAndBeanPropertyPairings.

        this.annotationsByBeanProperty = entityOracle.extractCoreProperties().stream()
                .filter(this::hasAConstraintAnnotation)
                .flatMap(this::extractConstraintAnnotations)
                .collect(Multimaps.toMultimap(pair -> pair.getValue0(), pair -> pair.getValue1(), HashMultimap::create));
    }

    boolean hasAConstraintAnnotation(BeanProperty<IC, Object> beanProperty)
    {
        return beanProperty.getAnnotationNames().stream().anyMatch(an -> constraintAnnotationByName.containsKey(an));
    }

    Stream<Pair<BeanProperty<IC, Object>, AnnotationValue<?>>> extractConstraintAnnotations(BeanProperty<IC, Object> beanProperty)
    {
        return beanProperty.getAnnotationNames().stream()
                .filter(an -> constraintAnnotationByName.containsKey(an) )
                .map(an -> beanProperty.getAnnotation(an))
                .map(av -> Pair.with(beanProperty, av));
    }
}
