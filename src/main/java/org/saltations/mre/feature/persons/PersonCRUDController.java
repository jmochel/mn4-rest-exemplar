package org.saltations.mre.feature.persons;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.web.router.RouteBuilder;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.saltations.mre.feature.persons.model.Person;
import org.saltations.mre.feature.persons.model.PersonCRUDService;
import org.saltations.mre.feature.persons.model.PersonCore;
import org.saltations.mre.feature.persons.model.PersonEntity;
import org.saltations.mre.feature.persons.outport.PersonRepo;
import org.saltations.mre.shared.app.EntityCRUDControllerBase;
import org.saltations.mre.shared.core.annotations.StdController;

/**
 * Provides REST access to the Person entity
 */

@Slf4j
@StdController
@Controller(value = "/persons", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
@Tag(name="Persons", description = "People's names and contact info")
public class PersonCRUDController extends EntityCRUDControllerBase<Long, Person, PersonCore, PersonEntity, PersonRepo, PersonMapper, PersonCRUDService>
{
    @Inject
    public PersonCRUDController(RouteBuilder.UriNamingStrategy uriNaming, PersonCRUDService service)
    {
        super(uriNaming, PersonEntity.class, service);
    }

    @Override
    public String getResourceName()
    {
        return "person";
    }
}
