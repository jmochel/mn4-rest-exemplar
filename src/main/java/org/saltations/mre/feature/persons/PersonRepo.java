package org.saltations.mre.feature.persons;

import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import org.saltations.mre.feature.persons.model.PersonEntity;
import org.saltations.mre.domain.services.repo.EntityRepoBase;

@JdbcRepository(dialect = Dialect.POSTGRES)
public abstract class PersonRepo extends EntityRepoBase<Long, PersonEntity>
{
}
