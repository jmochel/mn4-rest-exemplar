package org.saltations.mre.domain.places;

import java.util.UUID;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.saltations.mre.places.PlaceMapper;
import org.saltations.mre.domain.Place;
import org.saltations.mre.domain.PlaceCore;
import org.saltations.mre.domain.PlaceEntity;
import org.saltations.mre.domain.USState;
import org.saltations.mre.fixtures.EntityOracleBase;

/**
 * Provides exemplars of Place cores and entities.
 */

@Singleton
public class PlaceOracle extends EntityOracleBase<Place, PlaceCore, PlaceEntity>
{
    private static final long UUID_MOST_SIGNIFICANT_LONG = 0xffff_ffff_ffff_ffffL;
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
