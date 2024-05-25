package org.saltations.mre.feature.places;

import io.micronaut.validation.validator.Validator;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.saltations.mre.feature.places.model.Place;
import org.saltations.mre.feature.places.model.PlaceCore;
import org.saltations.mre.feature.places.model.PlaceEntity;
import org.saltations.mre.domain.services.service.EntityServiceBase;

import java.util.UUID;

@Singleton
public class PlaceService extends EntityServiceBase<UUID, Place, PlaceCore, PlaceEntity, PlaceRepo, PlaceMapper>
{
    @Inject
    public PlaceService(PlaceRepo repo, PlaceMapper mapper, Validator validator)
    {
        super(PlaceEntity.class, repo, mapper, validator);
    }
}
