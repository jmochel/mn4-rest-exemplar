package org.saltations.mre.domain.people;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.web.router.RouteBuilder;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.saltations.mre.domain.people.model.Person;
import org.saltations.mre.domain.people.logic.PersonCRUDService;
import org.saltations.mre.domain.people.model.PersonCore;
import org.saltations.mre.domain.people.model.PersonEntity;
import org.saltations.mre.domain.people.model.PersonMapper;
import org.saltations.mre.domain.people.gateway.PersonRepo;
import org.saltations.mre.common.domain.RestCrudEntityControllerFoundation;
import org.saltations.mre.common.core.annotations.StdController;

/**
 * Provides REST access to the Person entity
 */

@Slf4j
@StdController
@Controller(
        value = "/people/1",
        consumes = MediaType.APPLICATION_JSON,
        produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON_PROBLEM }
)
@Tag(name="Persons", description = "People's names and contact info")
public class PersonCRUDController extends RestCrudEntityControllerFoundation<Long, Person, PersonCore, PersonEntity, PersonRepo, PersonMapper, PersonCRUDService>
{
    @Inject
    public PersonCRUDController(RouteBuilder.UriNamingStrategy uriNaming, PersonCRUDService service)
    {
        super(uriNaming, PersonEntity.class, service);
    }

    @Override
    public String getEntityName()
    {
        return "person";
    }
}
