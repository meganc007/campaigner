package com.mcommings.campaigner.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DayTest {

    @Test
    public void shouldCreateADefaultDay() {
        Day day = new Day();
        Assertions.assertNotNull(day);
        Assertions.assertEquals(0, day.getId());
        Assertions.assertNull(day.getDescription());
        Assertions.assertNull(day.getFk_week());
    }

    @Test
    public void shouldCreateACustomDay() {
        int id = 1;
        String name = "This is a custom Day.";
        String description = "This is a custom description.";
        Integer fk_week = 2;

        Day day = new Day(id, name, description, fk_week);

        Assertions.assertEquals(id, day.getId());
        Assertions.assertEquals(name, day.getName());
        Assertions.assertEquals(description, day.getDescription());
        Assertions.assertEquals(fk_week, day.getFk_week());
    }

    @Test
    public void shouldConvertDayToStringWithForeignKeys() {
        int id = 1;
        String name = "This is a custom Day.";
        String description = "This is a custom description.";
        Integer fk_week = 2;

        Day day = new Day(id, name, description, fk_week);

        String expected = "Day(super=BaseEntity(name=This is a custom Day., description=This is a custom description.), id=1, fk_week=2, week=null)";

        Assertions.assertEquals(expected, day.toString());
    }
}
