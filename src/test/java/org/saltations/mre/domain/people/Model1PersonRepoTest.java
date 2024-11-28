package org.saltations.mre.domain.people;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import io.micronaut.data.runtime.criteria.RuntimeCriteriaBuilder;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.saltations.mre.domain.people.model.PersonMapper;
import org.saltations.mre.domain.people.outport.PersonRepo;
import org.saltations.mre.fixtures.ReplaceBDDCamelCase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SuppressWarnings("ClassHasNoToStringMethod")
@MicronautTest(transactional = false)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayNameGeneration(ReplaceBDDCamelCase.class)
class Model1PersonRepoTest
{
    @Inject
    private PersonOracle oracle;

    @Inject
    private PersonMapper mapper;

    @Inject
    private PersonRepo repo;

    @Inject
    private RuntimeCriteriaBuilder runtimeCriteriaBuilder;

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

        assertTrue(repo.existsById(saved.getId()),"It does exist");
        var retrieved = repo.findById(saved.getId()).orElseThrow();
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
    void canInsertAndUpdateACollection()
    {
        var protos = oracle.coreExemplars(1,20);

        var saved = repo.saveAll(mapper.createEntities(protos));
        assertEquals(protos.size(), saved.size(), "Created the expected amount");

        var modified = saved.stream().map(x -> {
            var modifiedCore = oracle.refurbishCore(x);
            return mapper.patchEntity(modifiedCore, x);
        }
        ).collect(Collectors.toList());

        var updated = repo.updateAll(modified);
        assertEquals(modified.size(), updated.size(), "Updated the expected amount");

        IntStream.range(0,20).forEach(i -> oracle.hasSameCoreContent(modified.get(i), updated.get(i)));
    }

    @Test
    @Order(8)
    void canInsertAndFindACollectionByIds()
    {
        var protos = oracle.coreExemplars(1,20);

        var saved = repo.saveAll(mapper.createEntities(protos));
        assertEquals(protos.size(), saved.size(), "Created the expected amount");

        var ids = saved.stream().map(e -> e.getId()).collect(Collectors.toList());

        var retrieved = repo.findAllByIdIn(ids);
        assertEquals(ids.size(), retrieved.size(), "Retrieved the expected amount");

        IntStream.range(0,20).forEach(i -> oracle.hasSameCoreContent(saved.get(i), retrieved.get(i)));

        repo.deleteByIdIn(ids);
        assertEquals(0, repo.count(),"They should be gone");
    }

}
