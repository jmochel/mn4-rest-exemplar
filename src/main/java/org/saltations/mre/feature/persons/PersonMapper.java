package org.saltations.mre.feature.persons;

import jakarta.inject.Singleton;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.saltations.mre.layer.EntityMapper;

import java.util.List;


@Singleton
@Mapper(componentModel = "jsr330")
public interface PersonMapper extends EntityMapper<PersonCore, PersonEntity>
{
    /**
     * Creates a copy of a PersonCore
     *
     * @param source <em>core object</em> to be copied
     *
     * @return Valid PersonCore
     */

    PersonCore copyCore(PersonCore source);

    /**
     * Maps a (PersonCore) prototype to an entity.
     *
     * @param proto prototype with core attributes to create an PersonEntity.
     *
     * @return Valid PersonEntity
     */

    @Mapping(target = "id",  ignore = true)
    @Mapping(target = "created",  ignore = true)
    @Mapping(target = "updated",  ignore = true)
    PersonEntity createEntity(PersonCore proto);

    /**
     * Maps a list of (PersonCore) prototypes to a list of entities.
     *
     * @param protos prototypes with core attributes to create an PersonEntity.
     *
     * @return List of valid PersonEntity
     */

    @Mapping(target = "id",  ignore = true)
    @Mapping(target = "created",  ignore = true)
    @Mapping(target = "updated",  ignore = true)
    List<PersonEntity> createEntities(List<PersonCore> protos);

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
    PersonEntity patchEntity(PersonCore patch, @MappingTarget PersonEntity entity);

}
