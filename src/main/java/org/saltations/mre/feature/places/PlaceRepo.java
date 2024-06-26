package org.saltations.mre.feature.places;

import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import org.saltations.mre.feature.places.model.PlaceEntity;
import org.saltations.mre.shared.domain.model.EntityRepoBase;

import java.util.UUID;

@JdbcRepository(dialect = Dialect.POSTGRES)
public abstract class PlaceRepo extends EntityRepoBase<UUID, PlaceEntity>
{
}
