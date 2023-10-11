package org.saltations.mre.persons.service;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.saltations.mre.core.ReplaceBDDCamelCase;
import org.saltations.mre.core.errors.CannotCreateEntity;
import org.saltations.mre.core.errors.CannotDeleteEntity;
import org.saltations.mre.core.errors.CannotFindEntity;
import org.saltations.mre.core.errors.CannotUpdateEntity;
import org.saltations.mre.persons.mapping.PersonMapper;
import org.saltations.mre.persons.model.PersonOracle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
@DisplayNameGeneration(ReplaceBDDCamelCase.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PersonServiceTest
{
    @Inject
    private PersonOracle oracle;
    @Inject
    private PersonMapper modelMapper;
    @Inject
    private PersonService service;

    @Test
    @Order(2)
    void canCreateReadUpdateAndDelete() throws CannotCreateEntity, CannotUpdateEntity, CannotDeleteEntity, CannotFindEntity
    {
        // Save

        var prototype = oracle.corePrototype();
        var saved = service.create(prototype);
        assertNotNull(saved);
        assertNotNull(saved.getId());
        oracle.hasSameCoreContent(prototype, saved);

        // Read

        var retrieved = service.findById(saved.getId()).get();
        oracle.hasSameCoreContent(saved, retrieved);
        assertEquals(saved.getId(), retrieved.getId());

        // Update

        var alteredCore = oracle.modifiedCore();
        var modified = modelMapper.patchEntity(alteredCore, retrieved);
        service.update(modified);

        var updated = service.findById(saved.getId()).get();
        oracle.hasSameCoreContent(alteredCore, updated);

        // Delete

        service.delete(saved.getId());
        var possible = service.findById(saved.getId());
        assertTrue(possible.isEmpty());
    }
}