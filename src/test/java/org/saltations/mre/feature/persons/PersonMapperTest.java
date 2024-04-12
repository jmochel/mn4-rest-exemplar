package org.saltations.mre.feature.persons;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.saltations.mre.fixtures.ReplaceBDDCamelCase;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit test for the PersonMapper
 */

@Testcontainers
@MicronautTest(transactional = false)
@DisplayNameGeneration(ReplaceBDDCamelCase.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonMapperTest
{
    @Inject
    private PersonOracle oracle;

    @Inject
    private PersonMapper mapper;

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
    void doesNotPatchNulls()
    {
        var prototype = oracle.coreExemplar();
        var created = mapper.createEntity(prototype);
        oracle.hasSameCoreContent(prototype, created);

        var patch = oracle.refurbishCore();
        patch.setLastName(null);

        var updated = mapper.patchEntity(patch, created);
        assertEquals(prototype.getLastName(), updated.getLastName(), "LastName was left untouched");
    }


}
