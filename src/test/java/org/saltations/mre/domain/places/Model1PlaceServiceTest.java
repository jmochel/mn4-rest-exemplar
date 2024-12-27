package org.saltations.mre.domain.places;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.saltations.mre.common.application.CannotCreateEntity;
import org.saltations.mre.common.application.CannotDeleteEntity;
import org.saltations.mre.common.application.CannotUpdateEntity;
import org.saltations.mre.domain.PlaceMapper;
import org.saltations.mre.fixtures.ReplaceBDDCamelCase;
import org.saltations.mre.places.PlaceCRUDService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
@DisplayNameGeneration(ReplaceBDDCamelCase.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Model1PlaceServiceTest
{
    @Inject
    private PlaceOracle oracle;

    @Inject
    private PlaceMapper modelMapper;

    @Inject
    private PlaceCRUDService service;

    @Test
    @Order(2)
    void canCreateReadUpdateAndDelete() throws CannotCreateEntity, CannotUpdateEntity, CannotDeleteEntity
    {
        // Save

        var prototype = oracle.coreExemplar();
        var result = service.create(prototype);
        var saved = result.get();

        assertNotNull(saved);
        assertNotNull(saved.getId());
        oracle.hasSameCoreContent(prototype, saved);

        // Read

        var retrieved = service.find(saved.getId()).orElseThrow();
        oracle.hasSameCoreContent(saved, retrieved);
        assertEquals(saved.getId(), retrieved.getId());

        // Update

        var alteredCore = oracle.refurbishCore();
        var modified = modelMapper.patchEntity(alteredCore, retrieved);
        service.update(modified);

        var updated = service.find(saved.getId()).orElseThrow();
        oracle.hasSameCoreContent(alteredCore, updated);

        // Delete

        service.delete(saved.getId());
        var possible = service.find(saved.getId());
        assertTrue(possible.isEmpty());
    }


}
