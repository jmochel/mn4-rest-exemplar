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

@AnalyzeClasses(packages = "org.saltations.mre.common.infra", importOptions = {ImportOption.DoNotIncludeTests.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CommonInfraLayer
{
    static final DescribedPredicate<JavaClass> areCommonInfra = resideInAPackage(
            "org.saltations.mre.common.infra.."
    );

    static final DescribedPredicate<JavaClass> areCommonInfraDependencies = resideInAnyPackage(
            ""
    );

    static final DescribedPredicate<JavaClass> areCommonInfraAndBelow = areCommonInfra
            .or(areCommonInfraDependencies)
            .or(areCommonDomainAndBelow);

    @ArchTest
    static final ArchRule should_only_depend_on_itself_and_common_application_and_below =
        classes()
            .should()
                .onlyDependOnClassesThat(areCommonInfraAndBelow);

}

