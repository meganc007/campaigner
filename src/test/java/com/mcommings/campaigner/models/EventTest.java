package com.mcommings.campaigner.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EventTest {

    @Test
    public void shouldCreateADefaultEvent() {
        Event event = new Event();
        Assertions.assertNotNull(event);
        Assertions.assertEquals(0, event.getId());
        Assertions.assertNull(event.getName());
        Assertions.assertNull(event.getDescription());
        Assertions.assertEquals(0, event.getEvent_year());
        Assertions.assertNull(event.getFk_month());
        Assertions.assertNull(event.getFk_week());
        Assertions.assertNull(event.getFk_day());
        Assertions.assertNull(event.getFk_city());
        Assertions.assertNull(event.getFk_continent());
        Assertions.assertNull(event.getFk_country());
    }

    @Test
    public void shouldCreateACustomEventWithNoForeignKeys() {
        int id = 1;
        String name = "Custom Event";
        String description = "This is a custom Event.";
        int event_year = 3;

        Event event = new Event(id, name, description, event_year);

        Assertions.assertEquals(id, event.getId());
        Assertions.assertEquals(name, event.getName());
        Assertions.assertEquals(description, event.getDescription());
        Assertions.assertEquals(event_year, event.getEvent_year());
    }

    @Test
    public void shouldConvertEventToStringWithNoForeignKeys() {
        int id = 1;
        String name = "Custom Event";
        String description = "This is a custom Event.";
        int event_year = 3;

        Event event = new Event(id, name, description, event_year);
        String expected = "Event(super=BaseEntity(name=Custom Event, description=This is a custom Event.), id=1, event_year=3, fk_month=null, fk_week=null, fk_day=null, fk_city=null, fk_continent=null, fk_country=null, month=null, week=null, day=null, city=null, continent=null, country=null)";

        Assertions.assertEquals(expected, event.toString());
    }

    @Test
    public void shouldCreateACustomEventWithForeignKeys() {
        int id = 1;
        String name = "Custom Event";
        String description = "This is a custom Event.";
        Integer event_year = 1;
        Integer fk_month = 2;
        Integer fk_week = 3;
        Integer fk_day = 4;
        Integer fk_city = 5;
        Integer fk_continent = 6;
        Integer fk_country = 7;

        Event event = new Event(id, name, description, event_year, fk_month, fk_week, fk_day, fk_city, fk_continent, fk_country);

        Assertions.assertEquals(id, event.getId());
        Assertions.assertEquals(name, event.getName());
        Assertions.assertEquals(description, event.getDescription());
        Assertions.assertEquals(event_year, event.getEvent_year());
        Assertions.assertEquals(fk_month, event.getFk_month());
        Assertions.assertEquals(fk_week, event.getFk_week());
        Assertions.assertEquals(fk_day, event.getFk_day());
        Assertions.assertEquals(fk_city, event.getFk_city());
        Assertions.assertEquals(fk_continent, event.getFk_continent());
        Assertions.assertEquals(fk_country, event.getFk_country());
    }

    @Test
    public void shouldConvertEventToStringWithForeignKeys() {
        int id = 1;
        String name = "Custom Event";
        String description = "This is a custom Event.";
        Integer event_year = 1;
        Integer fk_month = 2;
        Integer fk_week = 3;
        Integer fk_day = 4;
        Integer fk_city = 5;
        Integer fk_continent = 6;
        Integer fk_country = 7;

        Event event = new Event(id, name, description, event_year, fk_month, fk_week, fk_day, fk_city, fk_continent, fk_country);
        String expected = "Event(super=BaseEntity(name=Custom Event, description=This is a custom Event.), id=1, event_year=1, fk_month=2, fk_week=3, fk_day=4, fk_city=5, fk_continent=6, fk_country=7, month=null, week=null, day=null, city=null, continent=null, country=null)";

        Assertions.assertEquals(expected, event.toString());
    }
}
