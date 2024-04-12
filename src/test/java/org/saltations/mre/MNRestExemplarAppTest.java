package org.saltations.mre;

import io.micronaut.http.HttpStatus;
import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.serde.annotation.Serdeable;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import jakarta.inject.Inject;
import lombok.Data;
import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;
import org.junit.jupiter.api.TestMethodOrder;
import org.saltations.mre.fixtures.ReplaceBDDCamelCase;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
@DisplayNameGeneration(ReplaceBDDCamelCase.class)
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MNRestExemplarAppTest
{
    @Inject
    EmbeddedApplication<?> app;

    @Test
    @Order(1)
    void isRunning()
    {
        assertTrue(app.isRunning());
    }

    @Test
    @Order(2)
    final void isHealthy(RequestSpecification spec)
    {
        spec.
                when().
                get("/health").
                then().
                statusCode(HttpStatus.OK.getCode()).
                body("status", is("UP"));
    }

    @Test
    @Order(3)
    final void suppliesInfo(RequestSpecification spec)
    {
        var result = spec.
                when().
                get("/info").
                then().
                statusCode(HttpStatus.OK.getCode()).
                extract().asString();
    }

    @Test
    @Order(6)
    final void suppliesRefresh(RequestSpecification spec)
    {
        Map<String, String> parms = new HashMap<>();
        parms.put("\"force\"","true");

        var result = spec.
            when().
                body(parms).
                post("/refresh").
            then().
                statusCode(HttpStatus.OK.getCode()).
                extract().asString();

        assertNotEquals("", result, "Response should be non-null");
    }

    @Test
    @Order(7)
    final void suppliesRoutes(RequestSpecification spec)
    {
        var result = spec.
                when().
                get("/routes").
                then().
                statusCode(HttpStatus.OK.getCode()).
                extract().asString();

        assertNotEquals("", result, "Response should be non-null");
    }

    @Test
    @Order(8)
    final void suppliesLoggers(RequestSpecification spec)
    {
        var result = spec.
                when().
                get("/loggers").
                then().
                statusCode(HttpStatus.OK.getCode()).
                extract().asString();

        assertNotEquals("", result, "Response should be non-null");
    }

    @Test
    @Order(10)
    final void canCheckSpecificLoggers(RequestSpecification spec)
    {
        spec.
                when().
                get("/loggers/io.micronaut.http").
                then().
                statusCode(HttpStatus.OK.getCode()).
                body("effectiveLevel", is("INFO"));
    }

    @Test
    @Order(12)
    final void canChangeLoggers(RequestSpecification spec)
    {
        spec.
                when().
                contentType(ContentType.JSON).
                body("{ \"configuredLevel\": \"ERROR\" }").
                post("/loggers/io.micronaut.http").
                then().
                statusCode(HttpStatus.OK.getCode());

        spec.
                when().
                get("/loggers/io.micronaut.http").
                then().
                statusCode(HttpStatus.OK.getCode()).
                body("configuredLevel", is("ERROR"));
    }


    @Test
    @Order(20)
    final void canCheckLiquibaseChangelog(RequestSpecification spec)
    {
        var result = spec.
                when().
                    get("/liquibase").
                then().
                    statusCode(HttpStatus.OK.getCode()).
                    extract().as(new TypeRef<List<LiquibaseReport>>() {}
                );

        assertNotNull(result);
    }

    @Data
    @Serdeable
    static class LiquibaseReport {

        private String name;

        private List<ChangeSet> changeSets;

    }

    @Data
    @Serdeable
    static class ChangeSet {

        private String author;
        private String changeLog;
        private String comments;
        private ZonedDateTime dateExecuted;
        private String deploymentId;
        private String description;
        private String execType;
        private String id;
        private String storedChangeLog;
        private String checksum;
        private Integer orderExecuted;
        private String tag;
        private List<String> labels;
        private List<String> contexts;

    }

}
