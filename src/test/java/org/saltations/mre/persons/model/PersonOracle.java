package org.saltations.mre.persons.model;

import jakarta.inject.Singleton;
import org.saltations.mre.core.EntityOracleBase;
import org.saltations.mre.persons.model.Person;
import org.saltations.mre.persons.model.PersonCore;
import org.saltations.mre.persons.model.PersonEntity;

import java.time.OffsetDateTime;

/**
 * Provides exemplar instances of a Person.
 */

@Singleton
public class PersonOracle extends EntityOracleBase<Person, PersonCore, PersonEntity>
{
    public PersonOracle()
    {
        super(PersonCore.class, PersonEntity.class, Person.class);
    }

    @Override
    public PersonCore corePrototype()
    {
         return PersonCore.of()
                    .age(22)
                    .firstName("Samuel")
                    .lastName("Clemens")
                    .emailAddress("shmoil@agiga.com")
                    .done();
    }

    @Override
    public PersonCore modifiedCore() {
        return corePrototype().toBuilder()
                .age(121)
                .firstName("Frank")
                .lastName("Adams")
                .emailAddress("fad@adams.net")
                .done();
    }

    @Override
    public PersonEntity entityPrototype() {

        var core = corePrototype();
        var dts = OffsetDateTime.now();

        return PersonEntity.of()
                .id(1L)
                .age(core.getAge())
                .firstName(core.getFirstName())
                .lastName(core.getLastName())
                .created(dts)
                .updated(dts)
                .done();
    }



}
