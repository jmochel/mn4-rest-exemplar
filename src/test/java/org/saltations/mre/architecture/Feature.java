package org.saltations.mre.architecture;

import java.util.List;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAPackage;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.simpleNameEndingWith;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Feature
{
    public static final String ROOT_PACKAGE = "org.saltations.mre";

    private final JavaClasses projectClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages(ROOT_PACKAGE + "..");

    static List<String> getDomainNames()
    {
        return List.of("people","places");
    }

    @Order(10)
    @ParameterizedTest(name ="{index} =>  {0} feature controllers depend on domain logic and domain model")
    @MethodSource("getDomainNames")
    void controllers_depend_on_feature_code_and_infra_layer_and_presentation_layer_and_below(String domainName) {

        var areFeatureLayer =  resideInAPackage(ROOT_PACKAGE + "." + domainName + "..");
        var areFeatureControllers = areFeatureLayer.and(simpleNameEndingWith("Controller"));

        classes()
             .that(areFeatureControllers)
             .should()
             .onlyDependOnClassesThat(areFeatureLayer.or(ProjectDomainLayer.areProjectDomainAndBelow).or(CommonPresentationLayer.areCommonPresentationAndBelow))
             .check(projectClasses);
    }

    @Order(20)
    @ParameterizedTest(name ="{index} =>  {0} application logic depends on domain logic and domain model")
    @MethodSource("getDomainNames")
    void services_depend_on_feature_code_and_infra_layer_and_presentation_layer_and_below(String domainName) {

        var areFeatureLayer =  resideInAPackage(ROOT_PACKAGE + "." + domainName + "..");
        var areFeatureApplicationLayer = areFeatureLayer.and(simpleNameEndingWith("Service").or(simpleNameEndingWith("UseCase")));

        classes()
                .that(areFeatureApplicationLayer)
                .should()
                .onlyDependOnClassesThat(areFeatureLayer.or(ProjectDomainLayer.areProjectDomainAndBelow).or(CommonApplicationLayer.areCommonApplicationAndBelow))
                .check(projectClasses);
    }


}

