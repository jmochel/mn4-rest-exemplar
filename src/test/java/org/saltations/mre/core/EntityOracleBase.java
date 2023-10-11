package org.saltations.mre.core;

import io.micronaut.core.annotation.AnnotationValue;
import io.micronaut.core.beans.BeanIntrospection;
import io.micronaut.core.beans.BeanProperty;
import lombok.Getter;
import org.javatuples.Pair;
import org.junit.jupiter.api.function.Executable;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Generates example core objects and entities that implement the core interface (IC)
 *
 * @param <IC> Interface of the business item being represented
 * @param <C> Class of the business item
 * @param <E> Class of the persistable business item entity. Contains all the same data as C but Supports additional
 *           entity specific meta-data.
 */

@SuppressWarnings("ClassHasNoToStringMethod")
public abstract class EntityOracleBase<IC, C extends IC, E extends IC>
        implements EntityOracle<IC, C, E>
{
    @Getter
    private final Class<C> coreClass;

    @Getter
    private final Class<E> entityClass;

    @Getter
    private final Class<IC> coreInterfaceClass;

    private final BeanIntrospection<IC> introspection;
    private final Collection<BeanProperty<IC, Object>> beanProperties;

    public EntityOracleBase(Class<C> coreClass, Class<E> entityClass, Class<IC> coreInterfaceClass)
    {
        this.coreClass = coreClass;
        this.entityClass = entityClass;
        this.coreInterfaceClass = coreInterfaceClass;

        introspection = BeanIntrospection.getIntrospection(this.coreInterfaceClass);
        beanProperties = introspection.getBeanProperties();
    }

    @Override
    public void hasSameCoreContent(IC expected, IC actual)
    {
        List<Executable> assertions = new ArrayList<>();

        for (BeanProperty<IC,Object> property : beanProperties)
        {
            assertions.add(() -> assertEquals(property.get(expected),
                    property.get(actual), property.getName()));
        }

        assertAll(coreInterfaceClass.getSimpleName(), assertions);
    }

    public Stream<BeanProperty<IC,Object>> extractProperties()
    {
        return beanProperties.stream();
    }

    private Pair<AnnotationValue<Annotation>,BeanProperty<IC,Object>> pairUp(String annotationName, BeanProperty<IC,Object> beanProperty)
    {
        return Pair.with(beanProperty.getAnnotation(annotationName), beanProperty);
    }


    private boolean hasValidationConstraints(BeanProperty<IC,Object> property)
    {
        return property.getDeclaredAnnotationNames().stream().anyMatch(name -> name.contains("constraint"));
    }

}