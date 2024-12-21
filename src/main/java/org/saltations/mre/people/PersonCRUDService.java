package org.saltations.mre.people;

import io.micronaut.validation.validator.Validator;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.saltations.mre.domain.Person;
import org.saltations.mre.domain.PersonCore;
import org.saltations.mre.domain.PersonEntity;
import org.saltations.mre.common.application.CrudEntityServiceFoundation;

@Singleton
public class PersonCRUDService extends CrudEntityServiceFoundation<Long, Person, PersonCore, PersonEntity, PersonRepo, PersonMapper>
{
    @Inject
    public PersonCRUDService(PersonRepo repo, PersonMapper mapper, Validator validator)
    {
        super(PersonEntity.class, repo, mapper, validator);
    }
}
