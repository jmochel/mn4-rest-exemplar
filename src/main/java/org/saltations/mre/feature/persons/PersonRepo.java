package org.saltations.mre.feature.persons;

import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import org.saltations.mre.layer.EntityRepoBase;

@JdbcRepository(dialect = Dialect.POSTGRES)
public abstract class PersonRepo extends EntityRepoBase<Long, PersonEntity>
{
}
