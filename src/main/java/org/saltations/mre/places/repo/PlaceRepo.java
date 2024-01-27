package org.saltations.mre.places.repo;

import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import org.saltations.mre.core.EntityRepoBase;
import org.saltations.mre.persons.model.PersonEntity;
import org.saltations.mre.places.model.PlaceEntity;

import java.util.UUID;

@JdbcRepository(dialect = Dialect.POSTGRES)
public abstract class PlaceRepo extends EntityRepoBase<UUID, PlaceEntity>
{
}
