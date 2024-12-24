package org.saltations.mre.domain;

import java.util.List;

import jakarta.inject.Singleton;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.saltations.mre.common.domain.EntityMapper;


@Singleton
@Mapper(componentModel = "jsr330")
public interface PlaceMapper extends EntityMapper<PlaceCore, PlaceEntity>
{
    /**
     * Creates a copy of a PlaceCore
     *
     * @param source <em>core object</em> to be copied
     *
     * @return Valid PlaceCore
     */

    PlaceCore copyCore(PlaceCore source);

    /**
     * Maps a (PlaceCore) prototype to an entity.
     *
     * @param proto prototype with core attributes to create an PlaceEntity.
     *
     * @return Valid PlaceEntity
     */

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created",  ignore = true)
    @Mapping(target = "updated",  ignore = true)
    PlaceEntity createEntity(PlaceCore proto);

    /**
     * Maps a list of (PlaceCore) prototypes to a list of entities.
     *
     * @param protos prototypes with core attributes to create an PlaceEntity.
     *
     * @return List of valid PlaceEntity
     */

    @Mapping(target = "id",  ignore = true)
    @Mapping(target = "created",  ignore = true)
    @Mapping(target = "updated",  ignore = true)
    List<PlaceEntity> createEntities(List<PlaceCore> protos);

    /**
     * Patches the <em>entity</em> with non-null values from the patch object
     *
     * @param patch <em>core object</em> with core attributes used to update the <em>entity</em>.
     * @param entity object to be updated
     *
     * @return Patched entity
     */

    @Mapping(target = "id",  ignore = true)
    @Mapping(target = "created",  ignore = true)
    @Mapping(target = "updated",  ignore = true)
    @Mapping(target = "withId",  ignore = true)
    @Mapping(target = "withCreated",  ignore = true)
    @Mapping(target = "withUpdated",  ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    PlaceEntity patchEntity(PlaceCore patch, @MappingTarget PlaceEntity entity);

}
