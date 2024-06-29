package org.saltations.mre.feature.persons.outport;

import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import org.saltations.mre.feature.persons.model.PersonEntity;
import org.saltations.mre.shared.domain.model.EntityRepoBase;

@JdbcRepository(dialect = Dialect.POSTGRES)
public abstract class PersonRepo extends EntityRepoBase<Long, PersonEntity>
{
}
