package org.saltations.mre.persons.controller;

import io.micronaut.http.HttpStatus;
import io.micronaut.serde.ObjectMapper;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import jakarta.inject.Inject;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.saltations.mre.core.ReplaceBDDCamelCase;
import org.saltations.mre.persons.model.PersonEntity;
import org.saltations.mre.persons.model.PersonOracle;
import org.saltations.mre.persons.repo.PersonRepo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
@DisplayNameGeneration(ReplaceBDDCamelCase.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PersonControllerTest
{
    @Inject
    private PersonOracle oracle;

    @Inject
    private PersonRepo repo;

    @Test
    @Order(2)
    void canCreateReadReplaceAndDelete(RequestSpecification spec, ObjectMapper objMapper)
            throws Exception
    {
        // Create

        var proto = oracle.coreExemplar();
        var protoPayload = objMapper.writeValueAsString(proto);

        var created = spec.
            when().
                contentType(ContentType.JSON).
                body(protoPayload).
                post("/persons").
            then().
                statusCode(HttpStatus.OK.getCode()).
                extract().as(PersonEntity.class);

        assertNotNull(created);
        oracle.hasSameCoreContent(proto, created);


        // Read

        var retrieved = spec.
            when().
                contentType(ContentType.JSON).
                get("/persons/" + created.getId()).
            then().
                statusCode(HttpStatus.OK.getCode()).
                extract().as(PersonEntity.class);

        oracle.hasSameCoreContent(created, retrieved);

        // Replace

        var alteredCore = oracle.refurbishCore();
        var updatePayload = objMapper.writeValueAsString(alteredCore);

        var replace = spec.
                when().
                    contentType(ContentType.JSON).
                    body(updatePayload).
                    put("/persons/" + created.getId()).
                then().
                    statusCode(HttpStatus.OK.getCode()).
                    extract().as(PersonEntity.class);

        oracle.hasSameCoreContent(alteredCore, replace);

        // Delete

        spec.
            when().
                contentType(ContentType.JSON).
                delete("/persons/" + created.getId()).
            then().
                statusCode(HttpStatus.OK.getCode());
    }

    @Test
    @Order(4)
    void canPatch(RequestSpecification spec, ObjectMapper objMapper) throws Exception
    {
        // Create

        var proto = oracle.coreExemplar();
        var protoPayload = objMapper.writeValueAsString(proto);

        var created = spec.
                when().
                    contentType(ContentType.JSON).
                    body(protoPayload).
                    post("/persons").
                then().
                    statusCode(HttpStatus.OK.getCode()).
                    extract().as(PersonEntity.class);

        assertNotNull(created);
        oracle.hasSameCoreContent(proto, created);

        // Read

        var retrieved = spec.
                when().
                    contentType(ContentType.JSON).
                    get("/persons/" + created.getId()).
                then().
                    statusCode(HttpStatus.OK.getCode()).
                    extract().as(PersonEntity.class);

        oracle.hasSameCoreContent(created, retrieved);

        // Replace

        var patch = "{ \"first_name\" : \"Srinivas\", \"last_name\" : null   }";

        var patched = spec.
                when().
                    contentType(ContentType.JSON).
                    body(patch).
                    log().all().
                    patch("/persons/" + created.getId()).
                then().
                    log().all().
                    statusCode(HttpStatus.OK.getCode()).
                    extract().as(PersonEntity.class);

        assertEquals("Srinivas", patched.getFirstName());
        assertNull(patched.getLastName());
    }

}