package org.saltations.mre.feature.persons;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.web.router.RouteBuilder;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.saltations.mre.feature.persons.model.Person;
import org.saltations.mre.feature.persons.model.PersonCore;
import org.saltations.mre.feature.persons.model.PersonEntity;
import org.saltations.mre.domain.services.controller.EntityControllerBase;
import org.saltations.mre.domain.core.annotations.StdController;

/**
 * Provides REST access to the Person entity
 */

@Slf4j
@StdController
@Controller(value = "/persons", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
@Tag(name="Persons", description = "People's names and contact info")
public class PersonController extends EntityControllerBase<Long, Person, PersonCore, PersonEntity, PersonRepo, PersonMapper, PersonService>
{
    @Inject
    public PersonController(RouteBuilder.UriNamingStrategy uriNaming, PersonService service)
    {
        super(uriNaming, PersonEntity.class, service);
    }

    @Override
    public String getResourceName()
    {
        return "person";
    }
}
