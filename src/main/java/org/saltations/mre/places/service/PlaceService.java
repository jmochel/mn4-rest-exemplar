package org.saltations.mre.places.service;

import io.micronaut.validation.validator.Validator;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.saltations.mre.core.EntityServiceBase;
import org.saltations.mre.places.mapping.PlaceMapper;
import org.saltations.mre.places.model.Place;
import org.saltations.mre.places.model.PlaceCore;
import org.saltations.mre.places.model.PlaceEntity;
import org.saltations.mre.places.repo.PlaceRepo;

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