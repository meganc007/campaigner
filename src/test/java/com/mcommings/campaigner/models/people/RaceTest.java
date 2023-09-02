package com.mcommings.campaigner.models.people;

import com.mcommings.campaigner.models.people.Race;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RaceTest {

    @Test
    public void shouldCreateADefaultRace() {
        Race race = new Race();
        Assertions.assertNotNull(race);
        Assertions.assertEquals(0, race.getId());
        Assertions.assertNull(race.getName());
        Assertions.assertNull(race.getDescription());
        Assertions.assertFalse(race.is_exotic());
    }

    @Test
    public void shouldCreateACustomRace() {
        int id = 1;
        String name = "Custom Race";
        String description = "This is a custom race.";
        boolean isExotic = true;

        Race race = new Race(id, name, description, isExotic);

        Assertions.assertEquals(id, race.getId());
        Assertions.assertEquals(name, race.getName());
        Assertions.assertEquals(description, race.getDescription());
        Assertions.assertEquals(isExotic, race.is_exotic());
    }

    @Test
    public void shouldConvertRaceToString() {
        int id = 1;
        String name = "Custom Race";
        String description = "This is a custom race.";
        boolean isExotic = true;

        Race race = new Race(id, name, description, isExotic);
        String expected = "Race(super=BaseEntity(name=Custom Race, description=This is a custom race.), id=1, is_exotic=true)";

        Assertions.assertEquals(expected, race.toString());
    }
}
