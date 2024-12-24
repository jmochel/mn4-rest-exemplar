package org.saltations.mre.architecture;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;

import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAPackage;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAnyPackage;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static org.saltations.mre.architecture.CommonCoreLayer.areCommonCoreAndBelow;

@AnalyzeClasses(packages = "org.saltations.mre.common.domain", importOptions = {ImportOption.DoNotIncludeTests.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CommonDomainLayer
{
    static final DescribedPredicate<JavaClass> areCommonDomain = resideInAPackage(
            "org.saltations.mre.common.domain.."
    );

    static final DescribedPredicate<JavaClass> areCommonDomainDependencies = resideInAnyPackage(
            "org.mapstruct..",
            "com.fasterxml.jackson..", // FIXIT Do we really need these dependencies at this level ?
            "io.swagger..", // FIXIT Do we really need swagger annotations here ?
            "io.micronaut.context.." // FIXIT Do we really need these dependencies at this level ?
    );

    static final DescribedPredicate<JavaClass> areCommonDomainAndBelow = areCommonDomain
            .or(areCommonDomainDependencies)
            .or(areCommonCoreAndBelow);

    @ArchTest
    static final ArchRule should_only_depend_on_itself_and_common_core_and_below =
        classes()
            .should()
                .onlyDependOnClassesThat(areCommonDomainAndBelow);

}

