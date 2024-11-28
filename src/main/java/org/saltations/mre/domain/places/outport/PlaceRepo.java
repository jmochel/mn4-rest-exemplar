package org.saltations.mre.domain.places.outport;

import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import org.saltations.mre.domain.places.model.PlaceEntity;
import org.saltations.mre.common.domain.outport.CrudEntityRepoFoundation;

import java.util.UUID;

@JdbcRepository(dialect = Dialect.POSTGRES)
public abstract class PlaceRepo extends CrudEntityRepoFoundation<UUID, PlaceEntity>
{
}
