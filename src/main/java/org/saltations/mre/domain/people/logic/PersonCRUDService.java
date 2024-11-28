package org.saltations.mre.domain.people.logic;

import io.micronaut.validation.validator.Validator;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.saltations.mre.domain.people.model.PersonMapper;
import org.saltations.mre.domain.people.model.Person;
import org.saltations.mre.domain.people.model.PersonCore;
import org.saltations.mre.domain.people.model.PersonEntity;
import org.saltations.mre.domain.people.gateway.PersonRepo;
import org.saltations.mre.common.domain.logic.CrudEntityServiceFoundation;

@Singleton
public class PersonCRUDService extends CrudEntityServiceFoundation<Long, Person, PersonCore, PersonEntity, PersonRepo, PersonMapper>
{
    @Inject
    public PersonCRUDService(PersonRepo repo, PersonMapper mapper, Validator validator)
    {
        super(PersonEntity.class, repo, mapper, validator);
    }
}
