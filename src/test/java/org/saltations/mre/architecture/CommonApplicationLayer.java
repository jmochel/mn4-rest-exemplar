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
import static org.saltations.mre.architecture.CommonDomainLayer.areCommonDomainAndBelow;

@AnalyzeClasses(packages = "org.saltations.mre.common.application", importOptions = {ImportOption.DoNotIncludeTests.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CommonApplicationLayer
{
    static final DescribedPredicate<JavaClass> areCommonApplication = resideInAPackage(
            "org.saltations.mre.common.application.."
    );

    static final DescribedPredicate<JavaClass> areCommonApplicationDependencies = resideInAnyPackage(
            "com.fasterxml.jackson.."
    );

    static final DescribedPredicate<JavaClass> areCommonApplicationAndBelow = areCommonApplication
            .or(areCommonApplicationDependencies)
            .or(areCommonDomainAndBelow);

    @ArchTest
    static final ArchRule should_only_depend_on_itself_and_common_domain_and_below =
        classes()
            .should()
                .onlyDependOnClassesThat(areCommonApplicationAndBelow);

}

