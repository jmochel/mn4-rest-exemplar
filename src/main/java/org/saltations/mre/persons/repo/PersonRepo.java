package org.saltations.mre.persons.repo;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.jpa.criteria.PredicateSpecification;
import org.saltations.mre.core.EntityRepoBase;
import org.saltations.mre.persons.model.PersonEntity;

@JdbcRepository(dialect = Dialect.POSTGRES)
public abstract class PersonRepo extends EntityRepoBase<Long, PersonEntity>
{
}
