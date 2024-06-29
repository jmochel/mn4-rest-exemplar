package org.saltations.mre.feature.persons.model;

import io.micronaut.context.annotation.Prototype;
import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Inject;
import org.saltations.mre.feature.persons.PersonMapper;
import org.saltations.mre.feature.persons.inport.CreatePersonCmd;
import org.saltations.mre.feature.persons.inport.CreatePersonUseCase;
import org.saltations.mre.feature.persons.outport.PersonRepo;
import org.saltations.mre.shared.core.outcomes.FailureParticulars;
import org.saltations.mre.shared.core.outcomes.Outcome;
import org.saltations.mre.shared.core.outcomes.Outcomes;

@Prototype
public class CreatePersonSvc implements CreatePersonUseCase
{
    private final PersonRepo repo;
    private final PersonMapper mapper;

    @Inject
    public CreatePersonSvc(PersonRepo repo, PersonMapper mapper)
    {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public Outcome<FailureParticulars,PersonEntity> createPerson(CreatePersonCmd cmd)
    {
        PersonEntity personEntity = mapper.createEntity(cmd);

        return Outcomes.success(repo.save(personEntity));
    }
}
