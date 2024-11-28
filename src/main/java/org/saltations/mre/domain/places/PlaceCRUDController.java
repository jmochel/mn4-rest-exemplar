package org.saltations.mre.domain.places;

import java.util.UUID;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.web.router.RouteBuilder;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.saltations.mre.domain.places.logic.PlaceCRUDService;
import org.saltations.mre.domain.places.model.PlaceMapper;
import org.saltations.mre.domain.places.outport.PlaceRepo;
import org.saltations.mre.common.domain.RestCrudEntityControllerFoundation;
import org.saltations.mre.common.core.annotations.StdController;
import org.saltations.mre.domain.places.model.Place;
import org.saltations.mre.domain.places.model.PlaceCore;
import org.saltations.mre.domain.places.model.PlaceEntity;

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
    public PlaceCRUDController(RouteBuilder.UriNamingStrategy uriNaming, PlaceCRUDService service)
    {
        super(uriNaming, PlaceEntity.class, service);
    }

    @Override
    public String getEntityName()
    {
        return "place";
    }
}
