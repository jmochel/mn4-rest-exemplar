package org.saltations.mre.persons.repo;

import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import org.saltations.mre.core.EntityRepo;
import org.saltations.mre.persons.model.PersonEntity;

/**
 * Repository for Person entities
 */

@JdbcRepository(dialect = Dialect.MYSQL)
public abstract class PersonRepo extends EntityRepo<Long, PersonEntity>
{
}
