package org.saltations.mre.persons.repo;

import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import org.saltations.mre.core.EntityRepoBase;
import org.saltations.mre.persons.model.PersonEntity;

@JdbcRepository(dialect = Dialect.MYSQL)
public abstract class PersonRepo extends EntityRepoBase<Long, PersonEntity>
{
}
