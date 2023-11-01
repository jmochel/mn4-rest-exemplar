package org.saltations.mre.persons.service;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.saltations.mre.core.EntityServiceBase;
import org.saltations.mre.persons.mapping.PersonMapper;
import org.saltations.mre.persons.model.Person;
import org.saltations.mre.persons.model.PersonCore;
import org.saltations.mre.persons.model.PersonEntity;
import org.saltations.mre.persons.repo.PersonRepo;

@Singleton
public class PersonService extends EntityServiceBase<Long, Person, PersonCore, PersonEntity, PersonRepo, PersonMapper>
{
    @Inject
    public PersonService(PersonRepo repo, PersonMapper mapper)
    {
        super(PersonEntity.class, repo, mapper);
    }
}