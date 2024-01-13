package org.saltations.mre.core;

import org.junit.jupiter.api.DisplayNameGenerator;

import java.lang.reflect.Method;

public class ReplaceBDDCamelCase extends DisplayNameGenerator.Standard
{
    @Override
    public String generateDisplayNameForClass(Class<?> testClass) {

        return splitCamelCase(testClass.getSimpleName().replaceAll("[Tt]est$",""));
    }

    @Override
    public String generateDisplayNameForNestedClass(Class<?> nestedClass) {

        return splitCamelCase(nestedClass.getSimpleName().replaceAll("[Tt]est$",""))
                .toLowerCase()
                .replaceAll("^and", "AND ")
                .replaceAll("^given", "GIVEN ")
                .replaceAll(" when ", "WHEN ")
                .replaceAll(" then ", " THEN ")
                .replaceAll("  ", " ")
                .trim();
    }

    @Override
    public String generateDisplayNameForMethod(Class<?> testClass, Method testMethod) {
        return splitCamelCase(testMethod.getName())
                .toLowerCase()
                .replaceAll("^given", "GIVEN ")
                .replaceAll("when ", "WHEN ")
                .replaceAll(" then ", " THEN ")
                .replaceAll("  ", " ")
                .trim();
    }

    private String splitCamelCase(String incoming)
    {
        return incoming.replaceAll("([A-Z][a-z]+)", " $1")
                .replaceAll("([A-Z][A-Z]+)", " $1")
                .replaceAll("([A-Z][a-z]+)", "$1 ")
                .trim();
    }
}
