package org.saltations.mre.places;

import java.util.UUID;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.validation.validator.Validator;
import io.micronaut.web.router.RouteBuilder;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.saltations.mre.common.presentation.RestCrudEntityControllerFoundation;
import org.saltations.mre.common.presentation.StdController;
import org.saltations.mre.domain.Place;
import org.saltations.mre.domain.PlaceCore;
import org.saltations.mre.domain.PlaceEntity;
import org.saltations.mre.domain.PlaceMapper;

/**
 * Provides REST access to the Place entity
 */

@Slf4j
@StdController
@Controller(value = "/places", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
@Tag(name="Places", description = "Place info")
public class PlaceCRUDController extends RestCrudEntityControllerFoundation<UUID, Place, PlaceCore, PlaceEntity, PlaceRepo, PlaceMapper, PlaceCRUDService>
{
    @Inject
    public PlaceCRUDController(RouteBuilder.UriNamingStrategy uriNaming, PlaceCRUDService entityService, PlaceRepo entityRepo, PlaceMapper entityMapper, Validator validator)
    {
        super(uriNaming, PlaceEntity.class, entityService, entityRepo, entityMapper, validator);
    }

    @Override
    public String getEntityName()
    {
        return "place";
    }
}
