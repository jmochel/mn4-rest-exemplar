package org.saltations.mre.places.controller;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.web.router.RouteBuilder;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.saltations.mre.core.EntityControllerBase;
import org.saltations.mre.core.annotations.StdController;
import org.saltations.mre.places.mapping.PlaceMapper;
import org.saltations.mre.places.model.Place;
import org.saltations.mre.places.model.PlaceCore;
import org.saltations.mre.places.model.PlaceEntity;
import org.saltations.mre.places.repo.PlaceRepo;
import org.saltations.mre.places.service.PlaceService;

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
