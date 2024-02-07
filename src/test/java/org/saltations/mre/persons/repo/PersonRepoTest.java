package org.saltations.mre.persons.repo;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.saltations.mre.core.ReplaceBDDCamelCase;
import org.saltations.mre.persons.mapping.PersonMapper;
import org.saltations.mre.persons.model.PersonOracle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;


@SuppressWarnings("ClassHasNoToStringMethod")
@MicronautTest(transactional = false)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayNameGeneration(ReplaceBDDCamelCase.class)
class PersonRepoTest
{
    @Inject
    private PersonOracle oracle;

    @Inject
    private PersonMapper mapper;

    @Inject
    private PersonRepo repo;

    @BeforeEach
    public void cleanDB()
    {
        repo.deleteAll();
    }

    @Test
    @Order(2)
    void canInsertFindAndDeleteAnEntity()
    {
        var prototype = mapper.createEntity(oracle.coreExemplar());

        // Save and validate

        var saved = repo.save(prototype);
        oracle.hasSameCoreContent(prototype, saved);

        // Retrieve and validate

        var retrieved = repo.findById(saved.getId()).get();
        oracle.hasSameCoreContent(saved, retrieved);

        repo.deleteById(saved.getId());
        assertFalse(repo.existsById(saved.getId()),"Should have been deleted");
    }

    @Test
    @Order(4)
    void canUpdateAnEntity()
    {
        // Given a saved entity

        var saved = repo.save(mapper.createEntity(oracle.coreExemplar()));

        // When updated

        var update = mapper.patchEntity(oracle.refurbishCore(), saved);
        var updated = repo.update(update);

        // Then

        oracle.hasSameCoreContent(update, updated);
    }

    @Test
    @Order(6)
    void canInsertACollection()
    {
        var protos = oracle.coreExemplars(1,20);

        var saved = repo.saveAll(mapper.createEntities(protos));
        
        assertEquals(protos.size(), saved.size(), "Created the expected amount");
    }
}
