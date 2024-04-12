package org.saltations.mre.feature.places;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.saltations.mre.layer.EntityOracleBase;

import java.util.UUID;

/**
 * Provides exemplars of Place cores and entities.
 */

@Singleton
public class PlaceOracle extends EntityOracleBase<Place, PlaceCore, PlaceEntity>
{
    private static final long UUID_MOST_SIGNIFICANT_LONG = 0xFFFFFFFF;
    private final PlaceMapper mapper;

    @Inject
    public PlaceOracle(PlaceMapper mapper)
    {
        super(PlaceCore.class, PlaceEntity.class, Place.class, 1L);
        this.mapper = mapper;
    }

    @Override
    public PlaceCore coreExemplar(long sharedInitialValue, int offset)
    {
        int currIndex = (int) sharedInitialValue + offset;

        return PlaceCore.of()
                .name("City Hall #" + currIndex)
                .street1(currIndex + " Mass Ave")
                .street2("Suite 1" + currIndex)
                .city("Boston")
                .state(USState.MA)
                .done();
    }

    @Override
    public PlaceEntity entityExemplar(long sharedInitialValue, int offset)
    {
        var currIndex = initialSharedValue + offset;

        var core = coreExemplar(initialSharedValue, offset);
        var entity = mapper.createEntity(core);

        entity.setId(new UUID(UUID_MOST_SIGNIFICANT_LONG, currIndex));

        return entity;
    }

    @Override
    public PlaceCore refurbishCore(PlaceCore original)
    {
        var refurb = mapper.copyCore(original);

        refurb.setName(original.getName()+11);
        refurb.setCity("los Angeles");
        refurb.setStreet1(original.getStreet1()+"B");
        refurb.setStreet2(original.getStreet2()+"C");
        refurb.setState(USState.NH);

        return original;
    }

}
