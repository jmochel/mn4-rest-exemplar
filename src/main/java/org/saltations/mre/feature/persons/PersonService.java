package org.saltations.mre.feature.persons;

import io.micronaut.validation.validator.Validator;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.saltations.mre.layer.EntityServiceBase;

@Singleton
public class PersonService extends EntityServiceBase<Long, Person, PersonCore, PersonEntity, PersonRepo, PersonMapper>
{
    @Inject
    public PersonService(PersonRepo repo, PersonMapper mapper, Validator validator)
    {
        super(PersonEntity.class, repo, mapper, validator);
    }
}
