package com.mcommings.campaigner.models.calendar;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MoonTest {
    @Test
    public void shouldCreateADefaultMoon() {
        Moon moon = new Moon();
        Assertions.assertNotNull(moon);
        Assertions.assertEquals(0, moon.getId());
        Assertions.assertNull(moon.getName());
        Assertions.assertNull(moon.getDescription());
    }

    @Test
    public void shouldCreateACustomMoon() {
        int id = 1;
        String name = "This is a custom Moon.";
        String description = "This is a custom description.";

        Moon moon = new Moon(id, name, description);

        Assertions.assertEquals(id, moon.getId());
        Assertions.assertEquals(name, moon.getName());
        Assertions.assertEquals(description, moon.getDescription());
    }

    @Test
    public void shouldConvertMoonToStringWithForeignKeys() {
        int id = 1;
        String name = "This is a custom Moon.";
        String description = "This is a custom description.";

        Moon moon = new Moon(id, name, description);

        String expected = "Moon(super=BaseEntity(name=This is a custom Moon., description=This is a custom description.), id=1)";

        Assertions.assertEquals(expected, moon.toString());
    }
}
