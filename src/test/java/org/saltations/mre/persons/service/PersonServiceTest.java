package org.saltations.mre.persons.service;

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
import org.saltations.mre.core.ReplaceBDDCamelCase;
import org.saltations.mre.core.errors.CannotCreateEntity;
import org.saltations.mre.core.errors.CannotDeleteEntity;
import org.saltations.mre.core.errors.CannotFindEntity;
import org.saltations.mre.core.errors.CannotPatchEntity;
import org.saltations.mre.core.errors.CannotUpdateEntity;
import org.saltations.mre.persons.mapping.PersonMapper;
import org.saltations.mre.persons.model.PersonOracle;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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

        var retrieved = service.find(saved.getId()).get();
        oracle.hasSameCoreContent(saved, retrieved);
        assertEquals(saved.getId(), retrieved.getId());

        // Update

        var alteredCore = oracle.modifiedCore();
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

        var prototype = oracle.corePrototype();
        var saved = service.create(prototype);
        assertNotNull(saved);
        assertNotNull(saved.getId());
        oracle.hasSameCoreContent(prototype, saved);

        // Read

        var retrieved = service.find(saved.getId()).get();
        oracle.hasSameCoreContent(saved, retrieved);
        assertEquals(saved.getId(), retrieved.getId());

        // Patch

        var mergePatchAsString =  "{ \"first_name\" : \"Srinivas\", \"last_name\" : null   }";
        var mergePatch = jacksonMapper.readValue(mergePatchAsString, JsonMergePatch.class);

        var patched = service.patch(saved.getId(), mergePatch);
        assertEquals("Srinivas", patched.getFirstName());
        assertNull(patched.getLastName());

        // Delete

        service.delete(saved.getId());
        var possible = service.find(saved.getId());
        assertTrue(possible.isEmpty());
    }
}