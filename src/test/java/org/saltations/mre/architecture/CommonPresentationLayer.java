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
import static org.saltations.mre.architecture.CommonApplicationLayer.areCommonApplicationAndBelow;

@AnalyzeClasses(packages = "org.saltations.mre.common.presentation", importOptions = {ImportOption.DoNotIncludeTests.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CommonPresentationLayer
{
    static final DescribedPredicate<JavaClass> areCommonPresentation = resideInAPackage(
            "org.saltations.mre.common.presentation.."
    );

    static final DescribedPredicate<JavaClass> areCommonPresentationDependencies = resideInAnyPackage(
            "io.swagger..",     // TODO review of this needs to be in the _common_ presentation layer
            "org.zalando.problem..",     // TODO review of this needs to be in the _common_ presentation layer
            "com.github.fge..",         // TODO review of this needs to be in the _common_ presentation layer
            "reactor.core.."            // TODO review of this needs to be in the _common_ presentation layer
    );

    static final DescribedPredicate<JavaClass> areCommonPresentationAndBelow = areCommonPresentation
            .or(areCommonPresentationDependencies)
            .or(areCommonApplicationAndBelow);

    @ArchTest
    static final ArchRule should_only_depend_on_itself_and_common_application_and_below =
        classes()
            .should()
                .onlyDependOnClassesThat(areCommonPresentationAndBelow);

}

