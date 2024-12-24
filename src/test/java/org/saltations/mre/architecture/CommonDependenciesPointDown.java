package org.saltations.mre.architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.library.DependencyRules.NO_CLASSES_SHOULD_DEPEND_UPPER_PACKAGES;

@AnalyzeClasses(packages = "org.saltations.mre.common..", importOptions = {ImportOption.DoNotIncludeTests.class})
public class CommonDependenciesPointDown
{
    @ArchTest
    static final ArchRule no_access_to_upper_package = NO_CLASSES_SHOULD_DEPEND_UPPER_PACKAGES;

}

