package org.saltations.mre;

import java.util.List;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAPackage;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAnyPackage;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ArchitectureTest
{
    public static final String ROOT_PACKAGE = "org.saltations.mre";
    private final JavaClasses classes = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages(ROOT_PACKAGE + "..");

    private final DescribedPredicate<JavaClass> areStandard = resideInAnyPackage(
            "",
            "java..",
            "javax..",
            "org.slf4j..",
            "lombok..",
            "io.micronaut..",
            "jakarta..",
            "com.fasterxml.jackson..",
            "io.swagger.."
    );

    private final DescribedPredicate<JavaClass> areApp  = resideInAnyPackage(ROOT_PACKAGE + ".app..");
    private final DescribedPredicate<JavaClass> areDomains = resideInAnyPackage(ROOT_PACKAGE + ".domain..");

    private final DescribedPredicate<JavaClass> areCommonCore = resideInAnyPackage(ROOT_PACKAGE + ".common.core..");
    private final DescribedPredicate<JavaClass> areCommonDomain = resideInAnyPackage(ROOT_PACKAGE + ".common.domain..");

    private final DescribedPredicate<JavaClass> areCommon = areCommonCore.or(areCommonDomain);
    private final DescribedPredicate<JavaClass> areCommonOrStandard = areCommonCore.or(areCommonDomain).or(areStandard);


    @Test
    @Order(1)
    void common_core_depends_on_standard_libs_and_itself() {
        classes()
                .that(areCommonCore)
                .should()
                .dependOnClassesThat(areStandard.or(areCommonCore))
                .check(classes);
    }

    @Test
    @Order(2)
    void common_core_does_not_depend_on_common_domain() {
        noClasses()
                .that(areCommonCore)
                .should()
                .dependOnClassesThat(areCommonDomain)
                .check(classes);
    }

    @Test
    @Order(3)
    void common_core_should_not_depend_on_domains_or_app_code() {
        noClasses()
                .that(areCommonCore)
                .should()
                .dependOnClassesThat(areDomains.or(areApp).or(areCommonDomain))
                .check(classes);
    }

    @Test
    @Order(6)
    void common_domain_depends_on_common_core_and_standard_libs_and_itself() {
        classes()
                .that(areCommonDomain)
                .should()
                .dependOnClassesThat(areStandard.or(areCommonCore).or(areCommonCore))
                .check(classes);
    }

    static List<String> getDomainNames()
    {
        return List.of("people");
    }

    @Order(10)
    @ParameterizedTest(name ="{index} =>  {0} domain controllers depend on domain logic and domain model")
    @MethodSource("getDomainNames")
    void domain_controllers_depend_on_domain_logic_and_model(String domainName) {

        var areDomainControllers = resideInAPackage(ROOT_PACKAGE + ".domain." + domainName);
        var areDomainLogic = resideInAnyPackage(ROOT_PACKAGE + ".domain." + domainName + ".logic");
        var areDomainModel = resideInAnyPackage(ROOT_PACKAGE + ".domain." + domainName + ".model");

        classes()
                .that(areDomainControllers)
                .should()
                .dependOnClassesThat(areDomainControllers.or(areDomainLogic).or(areDomainModel).or(areCommonDomain).or(areCommonOrStandard))
                .check(classes);
    }


    @Order(12)
    @ParameterizedTest(name ="{index} =>  {0} domain logic depends on itself and its internal model")
    @MethodSource("getDomainNames")
    void domain_logic_depends_on_itself_and_its_model_and_outports(String domainName) {

        var areDomainLogic = resideInAnyPackage(ROOT_PACKAGE + ".domain." + domainName + ".logic" );
        var areDomainModel = resideInAnyPackage(ROOT_PACKAGE + ".domain." + domainName + ".model");
        var areDomainOutputs = resideInAnyPackage(ROOT_PACKAGE + ".domain." + domainName + ".outport");

        classes()
                .that(areDomainLogic)
                .should()
                .dependOnClassesThat(areDomainLogic.or(areDomainModel).or(areCommonOrStandard))
                .check(classes);
    }

}

