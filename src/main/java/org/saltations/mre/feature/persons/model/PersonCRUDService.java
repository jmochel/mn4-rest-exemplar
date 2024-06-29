package org.saltations.mre.feature.persons.model;

import io.micronaut.validation.validator.Validator;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.saltations.mre.feature.persons.PersonMapper;
import org.saltations.mre.feature.persons.outport.PersonRepo;
import org.saltations.mre.shared.domain.services.EntityCRUDServiceBase;

@Singleton
public class PersonCRUDService extends EntityCRUDServiceBase<Long, Person, PersonCore, PersonEntity, PersonRepo, PersonMapper>
{
    @Inject
    public PersonCRUDService(PersonRepo repo, PersonMapper mapper, Validator validator)
    {
        super(PersonEntity.class, repo, mapper, validator);
    }
}
