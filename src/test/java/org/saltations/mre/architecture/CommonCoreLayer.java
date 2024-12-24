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

@AnalyzeClasses(packages = "org.saltations.mre.common.core", importOptions = {ImportOption.DoNotIncludeTests.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CommonCoreLayer
{
    static final DescribedPredicate<JavaClass> areCommonCore = resideInAPackage("org.saltations.mre.common.core..");

    static final DescribedPredicate<JavaClass> areCrosscuttingDependencies = resideInAnyPackage("", "java..", "javax..", "jakarta..",                        // Jakarta annotations are across cutting concern that is easily managed and changed.
            "org.slf4j..",                      // Logging is crosscutting concern that is easily managed if it needs to be replaced
            "lombok..",                         // Bean annotations. Easily changed out if necessary.
            "io.micronaut.."                    // FIXIT Move out of core dependencies.
    );

    static final DescribedPredicate<JavaClass> areCommonCoreAndBelow = areCommonCore.or(areCrosscuttingDependencies);


    @ArchTest
    static final ArchRule common_core_should_only_depend_on_standard_libs_and_crosscutting_libs = classes().should()
                                                                                                           .onlyDependOnClassesThat(areCommonCoreAndBelow);

}

