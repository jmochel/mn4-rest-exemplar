package org.saltations.mre.domain.places;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.saltations.mre.domain.places.model.PlaceMapper;
import org.saltations.mre.fixtures.ReplaceBDDCamelCase;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit test for the PlaceMapper
 */

@Testcontainers
@MicronautTest(transactional = false)
@DisplayNameGeneration(ReplaceBDDCamelCase.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Model1PlaceMapperTest
{
    @Inject
    private PlaceOracle oracle;

    @Inject
    private PlaceMapper mapper;

    @Test
    @Order(2)
    void canCreateAnEntityFromAPrototype()
    {
        var prototype = oracle.coreExemplar();
        var created = mapper.createEntity(prototype);
        oracle.hasSameCoreContent(prototype, created);
    }

    @Test
    @Order(4)
    void canPatchAnEntityFromAPrototype()
    {
        var prototype = oracle.coreExemplar();
        var created = mapper.createEntity(prototype);
        oracle.hasSameCoreContent(prototype, created);

        var patch = oracle.refurbishCore();
        var updated = mapper.patchEntity(patch, created);

        oracle.hasSameCoreContent(patch, updated);
    }

    @Test
    @Order(6)
    void doesNotPatchNulls()
    {
        var prototype = oracle.coreExemplar();
        var created = mapper.createEntity(prototype);
        oracle.hasSameCoreContent(prototype, created);

        var patch = oracle.refurbishCore();
        patch.setCity(null);

        var updated = mapper.patchEntity(patch, created);
        assertEquals(prototype.getCity(), updated.getCity(), "City was left untouched");
    }


}
