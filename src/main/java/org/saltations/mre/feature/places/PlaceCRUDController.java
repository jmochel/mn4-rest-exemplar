package org.saltations.mre.feature.places;

import java.util.UUID;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.web.router.RouteBuilder;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.saltations.mre.shared.app.EntityCRUDControllerBase;
import org.saltations.mre.shared.core.annotations.StdController;
import org.saltations.mre.feature.places.model.Place;
import org.saltations.mre.feature.places.model.PlaceCore;
import org.saltations.mre.feature.places.model.PlaceEntity;

/**
 * Provides REST access to the Place entity
 */

@Slf4j
@StdController
@Controller(value = "/places", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
@Tag(name="Places", description = "Place info")
public class PlaceCRUDController extends EntityCRUDControllerBase<UUID, Place, PlaceCore, PlaceEntity, PlaceRepo, PlaceMapper, PlaceCRUDService>
{
    @Inject
    public PlaceCRUDController(RouteBuilder.UriNamingStrategy uriNaming, PlaceCRUDService service)
    {
        super(uriNaming, PlaceEntity.class, service);
    }

    @Override
    public String getResourceName()
    {
        return "place";
    }
}
