package org.saltations.mre.persons.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.web.router.RouteBuilder;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.saltations.mre.core.EntityController;
import org.saltations.mre.core.annotations.StdController;
import org.saltations.mre.persons.mapping.PersonMapper;
import org.saltations.mre.persons.model.Person;
import org.saltations.mre.persons.model.PersonCore;
import org.saltations.mre.persons.model.PersonEntity;
import org.saltations.mre.persons.repo.PersonRepo;
import org.saltations.mre.persons.service.PersonService;

/**
 * Provides REST access to the Person entity
 */

@Slf4j
@StdController
@Controller(value = "/persons", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
@Tag(name="Persons", description = "People's names and contact info")
public class PersonController extends EntityController<Long, Person, PersonCore, PersonEntity, PersonRepo, PersonMapper, PersonService>
{
    @Inject
    public PersonController(RouteBuilder.UriNamingStrategy uriNaming, PersonService service)
    {
        super(uriNaming, PersonEntity.class, service);
    }

}
