package org.saltations.mre.feature.persons;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.saltations.mre.fixtures.ReplaceBDDCamelCase;
import org.saltations.mre.layer.CannotCreateEntity;
import org.saltations.mre.layer.CannotDeleteEntity;
import org.saltations.mre.layer.CannotFindEntity;
import org.saltations.mre.layer.CannotPatchEntity;
import org.saltations.mre.layer.CannotUpdateEntity;

import java.io.IOException;

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

        var prototype = oracle.coreExemplar();
        var saved = service.create(prototype);
        assertNotNull(saved);
        assertNotNull(saved.getId());
        oracle.hasSameCoreContent(prototype, saved);

        // Read

        var retrieved = service.find(saved.getId()).get();
        oracle.hasSameCoreContent(saved, retrieved);
        assertEquals(saved.getId(), retrieved.getId());

        // Update

        var alteredCore = oracle.refurbishCore();
        var modified = modelMapper.patchEntity(alteredCore, retrieved);
        service.update(modified);

        var updated = service.find(saved.getId()).get();
        oracle.hasSameCoreContent(alteredCore, updated);

        // Delete

        service.delete(saved.getId());
        var possible = service.find(saved.getId());
        assertTrue(possible.isEmpty());
    }

    @Test
    @Order(4)
    void canPatch() throws CannotCreateEntity, CannotUpdateEntity, CannotDeleteEntity, CannotFindEntity, IOException, JsonPatchException, CannotPatchEntity
    {
        var jacksonMapper = new ObjectMapper();
        jacksonMapper.registerModule(new JavaTimeModule());

        // Save

        var prototype = oracle.coreExemplar();
        var saved = service.create(prototype);
        assertNotNull(saved);
        assertNotNull(saved.getId());
        oracle.hasSameCoreContent(prototype, saved);

        // Read

        var retrieved = service.find(saved.getId()).get();
        oracle.hasSameCoreContent(saved, retrieved);
        assertEquals(saved.getId(), retrieved.getId());

        // Patch with valid values

        var refurb = oracle.refurbishCore();
        var mergePatch = jacksonMapper.readValue(jacksonMapper.writeValueAsString(refurb), JsonMergePatch.class);
        var patched = service.patch(saved.getId(), mergePatch);

        // Delete

        service.delete(saved.getId());
        var possible = service.find(saved.getId());
        assertTrue(possible.isEmpty());
    }
}
