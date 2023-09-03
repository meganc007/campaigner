package com.mcommings.campaigner.models.people;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EventPlacePersonTest {

    @Test
    public void shouldCreateADefaultEventPlacePerson() {
        EventPlacePerson eventPlacePerson = new EventPlacePerson();
        Assertions.assertNotNull(eventPlacePerson);
        Assertions.assertEquals(0, eventPlacePerson.getId());
        Assertions.assertNull(eventPlacePerson.getFk_event());
        Assertions.assertNull(eventPlacePerson.getFk_place());
        Assertions.assertNull(eventPlacePerson.getFk_person());
    }

    @Test
    public void shouldCreateACustomEventPlacePerson() {
        int id = 1;
        Integer fk_event = 1;
        Integer fk_place = 2;
        Integer fk_person = 3;

        EventPlacePerson eventPlacePerson = new EventPlacePerson(id, fk_event, fk_place, fk_person);
        Assertions.assertEquals(id, eventPlacePerson.getId());
        Assertions.assertEquals(fk_event, eventPlacePerson.getFk_event());
        Assertions.assertEquals(fk_place, eventPlacePerson.getFk_place());
        Assertions.assertEquals(fk_person, eventPlacePerson.getFk_person());
    }

    @Test
    public void shouldConvertEventPlacePersonToString() {
        int id = 1;
        Integer fk_event = 1;
        Integer fk_place = 2;
        Integer fk_person = 3;

        EventPlacePerson eventPlacePerson = new EventPlacePerson(id, fk_event, fk_place, fk_person);
        String expected = "EventPlacePerson(id=1, fk_event=1, fk_place=2, fk_person=3, event=null, place=null, person=null)";

        Assertions.assertEquals(expected, eventPlacePerson.toString());
    }
}
