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

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.hasItems;
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
    public static final String RESOURCE_ENDPOINT = "/persons/";
    public static final String VALID_JSON_MERGE_PATCH_STRING = "{ \"first_name\" : \"Srinivas\", \"last_name\" : null   }";
    public static final Class<PersonEntity> ENTITY_CLASS = PersonEntity.class;


    @Inject
    private PersonOracle oracle;

    @Inject
    private PersonRepo repo;

    /**
     * Confirms that the patched resource matches the {@code VALID_JSON_MERGE_PATCH_STRING}
     */

    private void resourceMatchesValidMergePatch(PersonEntity patched)
    {
        assertEquals("Srinivas", patched.getFirstName());
        assertNull(patched.getLastName());
    }

    @Test
    @Order(2)
    void canCreateReadReplaceAndDelete(RequestSpecification spec, ObjectMapper objMapper)
            throws Exception
    {
        //@formatter:off
        // Create

        var proto = oracle.coreExemplar();
        var protoPayload = objMapper.writeValueAsString(proto);

        var created = spec.
            when().
                contentType(ContentType.JSON).
                body(protoPayload).
                post(RESOURCE_ENDPOINT).
            then().
                statusCode(HttpStatus.OK.getCode()).
                extract().as(ENTITY_CLASS);


        assertNotNull(created);
        oracle.hasSameCoreContent(proto, created);


        // Read

        var retrieved = spec.
            when().
                contentType(ContentType.JSON).
                get(RESOURCE_ENDPOINT + created.getId()).
            then().
                statusCode(HttpStatus.OK.getCode()).
                extract().as(ENTITY_CLASS);

        oracle.hasSameCoreContent(created, retrieved);

        // Replace

        var alteredCore = oracle.refurbishCore();
        var updatePayload = objMapper.writeValueAsString(alteredCore);

        var replace = spec.
                when().
                    contentType(ContentType.JSON).
                    body(updatePayload).
                    put(RESOURCE_ENDPOINT + created.getId()).
                then().
                    statusCode(HttpStatus.OK.getCode()).
                    extract().as(ENTITY_CLASS);

        oracle.hasSameCoreContent(alteredCore, replace);

        // Delete

        spec.
            when().
                contentType(ContentType.JSON).
                delete(RESOURCE_ENDPOINT + created.getId()).
            then().
                statusCode(HttpStatus.OK.getCode());

        //@formatter:on
    }

    @Test
    @Order(4)
    void canPatch(RequestSpecification spec, ObjectMapper objMapper) throws Exception
    {
        //@formatter:off
        // Create

        var proto = oracle.coreExemplar();
        var protoPayload = objMapper.writeValueAsString(proto);

        var created = spec.
                when().
                    contentType(ContentType.JSON).
                    body(protoPayload).
                    post(RESOURCE_ENDPOINT).
                then().
                    statusCode(HttpStatus.OK.getCode()).
                    extract().as(ENTITY_CLASS);

        assertNotNull(created);
        oracle.hasSameCoreContent(proto, created);

        // Read

        var retrieved = spec.
                when().
                    contentType(ContentType.JSON).
                    get(RESOURCE_ENDPOINT + created.getId()).
                then().
                    statusCode(HttpStatus.OK.getCode()).
                    extract().as(ENTITY_CLASS);

        oracle.hasSameCoreContent(created, retrieved);

        // Replace

        var patch = VALID_JSON_MERGE_PATCH_STRING;

        var patched = spec.
                when().
                    contentType(ContentType.JSON).
                    body(patch).
                    log().all().
                    patch(RESOURCE_ENDPOINT + created.getId()).
                then().
                    log().all().
                    statusCode(HttpStatus.OK.getCode()).
                    extract().as(ENTITY_CLASS);

        resourceMatchesValidMergePatch(patched);
        //@formatter:on
    }


    @Test
    @Order(20)
    void whenCreatingResourceWithIncorrectInputReturnsValidProblemDetails(RequestSpecification spec) throws Exception
    {
        //@formatter:off
        var created = spec.
                when().
                    contentType(ContentType.JSON).
                    body("{}").
                    post(RESOURCE_ENDPOINT).
                then().
                    log().all().
                    statusCode(HttpStatus.BAD_REQUEST.getCode()).
                    assertThat().body(matchesJsonSchemaInClasspath("json-schema/cannot-create-constraint-violations.schema.json"));
        //@formatter:on
    }

    @Test
    @Order(22)
    void whenGettingNonexistentResourceReturnsProblemDetails(RequestSpecification spec) throws Exception
    {
        //@formatter:off
        var retrieved = spec.
                when().
                    contentType(ContentType.JSON).
                    get("/persons/" + 274).
                then().
                    statusCode(HttpStatus.NOT_FOUND.getCode()).
                    assertThat().body(matchesJsonSchemaInClasspath("json-schema/cannot-find-resource.schema.json"));
        //@formatter:on
    }

    @Test
    @Order(24)
    void whenReplacingResourceWithIncorrectInputReturnsValidProblemDetails(RequestSpecification spec, ObjectMapper objMapper) throws Exception
    {
        //@formatter:off
        // Create

        var proto = oracle.coreExemplar();
        var protoPayload = objMapper.writeValueAsString(proto);

        var created = spec.
                when().
                    contentType(ContentType.JSON).
                    body(protoPayload).
                    post(RESOURCE_ENDPOINT).
                then().
                    statusCode(HttpStatus.OK.getCode()).
                    extract().as(ENTITY_CLASS);

        // Replace

        var alteredCore = oracle.refurbishCore();

        alteredCore.setAge(0);

        var updatePayload = objMapper.writeValueAsString(alteredCore);

        spec.
                when().
                    contentType(ContentType.JSON).
                    body(updatePayload).
                    put(RESOURCE_ENDPOINT + created.getId()).
                then().
                    log().all().
                    statusCode(HttpStatus.BAD_REQUEST.getCode()).
                    assertThat().body(matchesJsonSchemaInClasspath("json-schema/cannot-create-constraint-violations.schema.json"));
        //@formatter:on
    }

}