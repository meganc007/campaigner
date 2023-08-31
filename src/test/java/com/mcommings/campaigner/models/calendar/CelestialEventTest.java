package com.mcommings.campaigner.models.calendar;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CelestialEventTest {
    
    @Test
    public void shouldCreateADefaultCelestialEventWithNoForeignKeys() {
        CelestialEvent celestialEvent = new CelestialEvent();
        Assertions.assertNotNull(celestialEvent);
        Assertions.assertEquals(0, celestialEvent.getId());
        Assertions.assertNull(celestialEvent.getName());
        Assertions.assertNull(celestialEvent.getDescription());
        Assertions.assertEquals(0, celestialEvent.getEvent_year());
    }

    @Test
    public void shouldCreateACustomCelestialEventWithNoForeignKeys() {
        int id = 1;
        String name = "Custom CelestialEvent";
        String description = "This is a custom CelestialEvent.";

        CelestialEvent celestialEvent = new CelestialEvent(id, name, description, id);

        Assertions.assertEquals(id, celestialEvent.getId());
        Assertions.assertEquals(name, celestialEvent.getName());
        Assertions.assertEquals(description, celestialEvent.getDescription());
        Assertions.assertEquals(id, celestialEvent.getEvent_year());
    }
    @Test
    public void shouldConvertCelestialEventToStringWithNoForeignKeys() {
        int id = 1;
        String name = "Custom CelestialEvent";
        String description = "This is a custom CelestialEvent.";

        CelestialEvent celestialEvent = new CelestialEvent(id, name, description, id);
        String expected = "CelestialEvent(super=BaseEntity(name=Custom CelestialEvent, description=This is a custom CelestialEvent.), id=1, fk_moon=null, fk_sun=null, fk_month=null, fk_week=null, fk_day=null, moon=null, sun=null, month=null, week=null, day=null, event_year=1)";

        Assertions.assertEquals(expected, celestialEvent.toString());
    }

    @Test
    public void shouldCreateACustomCelestialEventWithForeignKeys() {
        int id = 1;
        String name = "Custom CelestialEvent";
        String description = "This is a custom CelestialEvent.";
        Integer fk_moon = 1;
        Integer fk_sun = 2;
        Integer fk_month = 3;
        Integer fk_week = 4;
        Integer fk_day = 5;
        Integer event_year = 1;


        CelestialEvent celestialEvent = new CelestialEvent(id, name, description, fk_moon, fk_sun, fk_month, fk_week, fk_day, event_year);

        Assertions.assertEquals(id, celestialEvent.getId());
        Assertions.assertEquals(name, celestialEvent.getName());
        Assertions.assertEquals(description, celestialEvent.getDescription());
        Assertions.assertEquals(fk_moon, celestialEvent.getFk_moon());
        Assertions.assertEquals(fk_sun, celestialEvent.getFk_sun());
        Assertions.assertEquals(fk_month, celestialEvent.getFk_month());
        Assertions.assertEquals(fk_week, celestialEvent.getFk_week());
        Assertions.assertEquals(fk_day, celestialEvent.getFk_day());
        Assertions.assertEquals(event_year, celestialEvent.getEvent_year());
    }

    @Test
    public void shouldConvertCelestialEventToStringWithForeignKeys() {
        int id = 1;
        String name = "Custom CelestialEvent";
        String description = "This is a custom CelestialEvent.";
        Integer fk_moon = 1;
        Integer fk_sun = 2;
        Integer fk_month = 3;
        Integer fk_week = 4;
        Integer fk_day = 5;
        Integer event_year = 1;


        CelestialEvent celestialEvent = new CelestialEvent(id, name, description, fk_moon, fk_sun, fk_month, fk_week, fk_day, event_year);
        String expected = "CelestialEvent(super=BaseEntity(name=Custom CelestialEvent, description=This is a custom CelestialEvent.), id=1, fk_moon=1, fk_sun=2, fk_month=3, fk_week=4, fk_day=5, moon=null, sun=null, month=null, week=null, day=null, event_year=1)";

        Assertions.assertEquals(expected, celestialEvent.toString());
    }
}
