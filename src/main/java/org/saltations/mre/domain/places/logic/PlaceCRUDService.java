package org.saltations.mre.domain.places.logic;

import io.micronaut.validation.validator.Validator;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.saltations.mre.domain.places.model.PlaceMapper;
import org.saltations.mre.domain.places.model.Place;
import org.saltations.mre.domain.places.model.PlaceCore;
import org.saltations.mre.domain.places.model.PlaceEntity;
import org.saltations.mre.domain.places.outport.PlaceRepo;
import org.saltations.mre.common.domain.logic.CrudEntityServiceFoundation;

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
