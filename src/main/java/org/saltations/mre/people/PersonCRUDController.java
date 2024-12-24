package org.saltations.mre.people;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.validation.validator.Validator;
import io.micronaut.web.router.RouteBuilder;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.saltations.mre.domain.Person;
import org.saltations.mre.domain.PersonCore;
import org.saltations.mre.domain.PersonEntity;
import org.saltations.mre.common.presentation.RestCrudEntityControllerFoundation;
import org.saltations.mre.common.presentation.StdController;
import org.saltations.mre.domain.PersonMapper;


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
    public PersonCRUDController(RouteBuilder.UriNamingStrategy uriNaming, PersonCRUDService entityService, PersonRepo entityRepo, PersonMapper entityMapper, Validator validator)
    {
        super(uriNaming, PersonEntity.class, entityService, entityRepo, entityMapper, validator);
    }

    @Override
    public String getEntityName()
    {
        return "person";
    }
}
