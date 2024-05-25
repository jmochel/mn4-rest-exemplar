package org.saltations.mre.feature.places;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.web.router.RouteBuilder;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.saltations.mre.feature.places.model.Place;
import org.saltations.mre.feature.places.model.PlaceCore;
import org.saltations.mre.feature.places.model.PlaceEntity;
import org.saltations.mre.domain.services.controller.EntityControllerBase;
import org.saltations.mre.domain.core.annotations.StdController;

import java.util.UUID;

/**
 * Provides REST access to the Place entity
 */

@Slf4j
@StdController
@Controller(value = "/places", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
@Tag(name="Places", description = "Place info")
public class PlaceController extends EntityControllerBase<UUID, Place, PlaceCore, PlaceEntity, PlaceRepo, PlaceMapper, PlaceService>
{
    @Inject
    public PlaceController(RouteBuilder.UriNamingStrategy uriNaming, PlaceService service)
    {
        super(uriNaming, PlaceEntity.class, service);
    }

    @Override
    public String getResourceName()
    {
        return "place";
    }
}
