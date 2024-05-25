package org.saltations.mre;

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
import org.junit.jupiter.params.provider.ValueSource;

import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAnyPackage;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ArchitectureTest
{
    private final JavaClasses classes = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("org.saltations.mre");

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

    private final DescribedPredicate<JavaClass> areCore = resideInAnyPackage("org.saltations.mre.core..");

    private final DescribedPredicate<JavaClass> areDomainScaffolding = resideInAnyPackage("org.saltations.mre.domain..");

    private final DescribedPredicate<JavaClass> areDomainModelScaffolding = resideInAnyPackage("org.saltations.mre.domain.model..");
    private final DescribedPredicate<JavaClass> areDomainServicesScaffolding = resideInAnyPackage("org.saltations.mre.domain.services..");

    private final DescribedPredicate<JavaClass> areDomainServiceScaffolding = resideInAnyPackage("org.saltations.mre.domain.services..");
    private final DescribedPredicate<JavaClass> areDomainControllerScaffolding = resideInAnyPackage("org.saltations.mre.application..");

    private final DescribedPredicate<JavaClass> areDomainModelScaffoldingOrBelow = areDomainModelScaffolding.or(areCore).or(areStandard);
    private final DescribedPredicate<JavaClass> areDomainServiceScaffoldingOrBelow = areDomainServiceScaffolding.or(areDomainModelScaffoldingOrBelow);
    private final DescribedPredicate<JavaClass> areDomainControllerScaffoldingOrBelow = areDomainControllerScaffolding.or(areDomainServiceScaffoldingOrBelow);

    private final DescribedPredicate<JavaClass> areFeatures = resideInAnyPackage("org.saltations.mre.feature..");


    @Test
    @Order(2)
    void core_should_only_depend_on_itself_and_standard_classes() {
        classes()
                .that(areCore)
                .should()
                .onlyDependOnClassesThat(areCore.or(areStandard))
                .check(classes);
    }

    @Test
    @Order(4)
    void core_should_not_depend_on_scaffolding() {
        noClasses()
                .that(areCore)
                .should()
                .dependOnClassesThat(areDomainModelScaffolding.or(areDomainServicesScaffolding))
                .check(classes);
    }

    @Test
    @Order(6)
    void core_should_not_depend_on_features() {
        noClasses()
                .that(areCore)
                .should()
                .dependOnClassesThat(areFeatures)
                .check(classes);
    }


    @Test
    @Order(8)
    void scaffolding_should_depend_on_core() {
        classes()
                .that(areDomainScaffolding)
                .should()
                .dependOnClassesThat(areDomainScaffolding.or(areCore).or(areStandard))
                .check(classes);
    }

    @Test
    @Order(10)
    void scaffolding_should_depend_from_controller_to_service_to_repo_to_model() {

        classes()
                .that(areDomainServiceScaffolding)
                .should()
                .dependOnClassesThat(areDomainServiceScaffoldingOrBelow)
                .check(classes);

        classes()
                .that(areDomainControllerScaffolding)
                .should()
                .dependOnClassesThat(areDomainControllerScaffoldingOrBelow)
                .check(classes);
    }

    @Test
    @Order(12)
    void feature_controllers_depend_on_controller_scaffolding_or_below() {
        classes()
                .that(resideInAnyPackage("org.saltations.mre.feature..")).and().haveSimpleNameEndingWith("Controller")
                .should()
                .dependOnClassesThat(areDomainControllerScaffoldingOrBelow)
                .check(classes);
    }

    @Test
    @Order(14)
    void feature_services_depend_on_service_scaffolding_or_below() {
        classes()
                .that(resideInAnyPackage("org.saltations.mre.feature..")).and().haveSimpleNameEndingWith("Service")
                .should()
                .dependOnClassesThat(areDomainServiceScaffoldingOrBelow)
                .check(classes);
    }

    @Test
    @Order(16)
    void feature_repos_depend_on_model_scaffolding_or_below() {
        classes()
                .that(resideInAnyPackage("org.saltations.mre.feature..")).and().haveSimpleNameEndingWith("Service")
                .should()
                .dependOnClassesThat(areDomainModelScaffoldingOrBelow)
                .check(classes);
    }

    @Order(20)
    @ParameterizedTest(name ="{index} =>  {0} feature depends on itself and its internal model")
    @ValueSource(strings = {"place","person"})
    void feature_depends_on_itself_and_its_model(String featureName) {

        var areFeature = resideInAnyPackage("org.saltations.mre.feature.persons");
        var areFeatureModel = resideInAnyPackage("org.saltations.mre.feature.persons.model");

        classes()
                .that(areFeature)
                .should()
                .dependOnClassesThat(areFeature.or(areFeatureModel))
                .check(classes);
    }

}

