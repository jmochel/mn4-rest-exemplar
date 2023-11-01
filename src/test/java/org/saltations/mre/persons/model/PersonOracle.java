package org.saltations.mre.persons.model;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.saltations.mre.core.EntityOracleBase;
import org.saltations.mre.persons.mapping.PersonMapper;

/**
 * Provides exemplars of Person cores and entities.
 */

@Singleton
public class PersonOracle extends EntityOracleBase<Person, PersonCore, PersonEntity>
{
    private final PersonMapper mapper;

    @Inject
    public PersonOracle(PersonMapper mapper)
    {
        super(PersonCore.class, PersonEntity.class, Person.class, 1L);
        this.mapper = mapper;
    }

    @Override
    public PersonCore coreExemplar(long sharedInitialValue, int offset)
    {
        int currIndex = (int) sharedInitialValue + offset;

        return PersonCore.of()
            .age(12 + currIndex)
            .firstName("Samuel")
            .lastName("Clemens")
            .emailAddress("shmoil" + currIndex + "@agiga.com")
            .done();
    }

    @Override
    public PersonEntity entityExemplar(long sharedInitialValue, int offset)
    {
        var currIndex = initialSharedValue + offset;

        var core = coreExemplar(initialSharedValue, offset);
        var entity = mapper.createEntity(core);

        entity.setId(currIndex);

        return entity;
    }

    @Override
    public PersonCore refurbishCore(PersonCore original)
    {
        var refurb = mapper.copyCore(original);

        refurb.setAge(original.getAge()+1);
        refurb.setFirstName(original.getFirstName()+"A");
        refurb.setLastName(original.getLastName()+"B");
        refurb.setEmailAddress("mod"+ original.getEmailAddress());

        return original;
    }

}
