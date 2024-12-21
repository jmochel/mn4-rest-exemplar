package org.saltations.mre.domain;

import java.time.OffsetDateTime;
import java.util.UUID;

import io.micronaut.data.annotation.AutoPopulated;
import io.micronaut.data.annotation.DateCreated;
import io.micronaut.data.annotation.DateUpdated;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.serde.annotation.Serdeable;
import io.micronaut.serde.config.naming.SnakeCaseStrategy;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.With;
import lombok.experimental.SuperBuilder;
import org.saltations.mre.common.domain.Entity;

/**
 *  Identifiable, persistable Person
 */

@Getter
@Setter
@With()
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@MappedEntity("place")
@Serdeable(naming = SnakeCaseStrategy.class)
@SuperBuilder(builderMethodName = "of", buildMethodName = "done", toBuilder = true)
public class PlaceEntity extends PlaceCore implements Entity<UUID>
{
    @Id
    @AutoPopulated
    private UUID id;

    @DateCreated
    private OffsetDateTime created;

    @DateUpdated
    private OffsetDateTime updated;
}
