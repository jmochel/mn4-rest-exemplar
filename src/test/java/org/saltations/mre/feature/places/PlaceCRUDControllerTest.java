package org.saltations.mre.feature.places;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import org.saltations.mre.feature.places.model.PlaceEntity;
import org.saltations.mre.fixtures.ReplaceBDDCamelCase;

import java.util.UUID;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@MicronautTest
@DisplayNameGeneration(ReplaceBDDCamelCase.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PlaceCRUDControllerTest
{
    public static final String RESOURCE_ENDPOINT = "/places/";
    public static final Class<PlaceEntity> ENTITY_CLASS = PlaceEntity.class;


    @Inject
    private PlaceOracle oracle;

    @Inject
    private PlaceMapper modelMapper;


    /**
     * Confirms that the patched resource matches the {@code VALID_JSON_MERGE_PATCH_STRING}
     */


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

        var altered = oracle.refurbishCore();
        var updatePayload = objMapper.writeValueAsString(modelMapper.patchEntity(altered, retrieved));

        var replaced = spec.
                when().
                    contentType(ContentType.JSON).
                    body(updatePayload).
                    put(RESOURCE_ENDPOINT + created.getId()).
                then().
                    statusCode(HttpStatus.OK.getCode()).
                    extract().as(ENTITY_CLASS);

        oracle.hasSameCoreContent(altered, replaced);

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

        // Patch with valid values

        var jacksonMapper = new com.fasterxml.jackson.databind.ObjectMapper();
        jacksonMapper.registerModule(new JavaTimeModule());

        var refurb = oracle.refurbishCore();
        var patch = jacksonMapper.writeValueAsString(refurb);

        var patched = spec.
                when().
                    contentType(ContentType.JSON).
                    body(patch).
                    patch(RESOURCE_ENDPOINT + created.getId()).
                then().
                    statusCode(HttpStatus.OK.getCode()).
                    extract().as(ENTITY_CLASS);

        oracle.hasSameCoreContent(refurb, patched);
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
        var id = new UUID(11111L,22222L);

        //@formatter:off
        var retrieved = spec.
                when().
                    contentType(ContentType.JSON).
                    get("/places/" + id).
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
        alteredCore.setName(null);

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
