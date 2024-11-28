package org.saltations.mre.domain.people.outport;

import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import org.saltations.mre.domain.people.model.PersonEntity;
import org.saltations.mre.common.domain.outport.CrudEntityRepoFoundation;

/**
 * Repository for the Person entity
 */

@JdbcRepository(dialect = Dialect.POSTGRES)
public abstract class PersonRepo extends CrudEntityRepoFoundation<Long, PersonEntity>
{
}
