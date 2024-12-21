package org.saltations.mre.places;

import io.micronaut.validation.validator.Validator;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.saltations.mre.domain.Place;
import org.saltations.mre.domain.PlaceCore;
import org.saltations.mre.domain.PlaceEntity;
import org.saltations.mre.common.application.CrudEntityServiceFoundation;

import java.util.UUID;

@Singleton
public class PlaceCRUDService extends CrudEntityServiceFoundation<UUID, Place, PlaceCore, PlaceEntity, PlaceRepo, PlaceMapper>
{
    @Inject
    public PlaceCRUDService(PlaceRepo repo, PlaceMapper mapper, Validator validator)
    {
        super(PlaceEntity.class, repo, mapper, validator);
    }
}
