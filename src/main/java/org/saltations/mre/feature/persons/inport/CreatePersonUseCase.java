package org.saltations.mre.feature.persons.inport;

import org.saltations.mre.feature.persons.model.PersonEntity;
import org.saltations.mre.shared.core.outcomes.FailureParticulars;
import org.saltations.mre.shared.core.outcomes.Outcome;

public interface CreatePersonUseCase
{
    Outcome<FailureParticulars,PersonEntity> createPerson(CreatePersonCmd cmd);
}
