package org.saltations.mre.people;

import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import org.saltations.mre.domain.PersonEntity;
import org.saltations.mre.common.application.CrudEntityRepoFoundation;

/**
 * Repository for the Person entity
 */

@JdbcRepository(dialect = Dialect.POSTGRES)
public abstract class PersonRepo extends CrudEntityRepoFoundation<Long, PersonEntity>
{
}
