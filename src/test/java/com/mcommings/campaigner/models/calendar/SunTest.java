package com.mcommings.campaigner.models.calendar;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SunTest {
    @Test
    public void shouldCreateADefaultSun() {
        Sun sun = new Sun();
        Assertions.assertNotNull(sun);
        Assertions.assertEquals(0, sun.getId());
        Assertions.assertNull(sun.getName());
        Assertions.assertNull(sun.getDescription());
    }

    @Test
    public void shouldCreateACustomSun() {
        int id = 1;
        String name = "This is a custom Sun.";
        String description = "This is a custom description.";

        Sun sun = new Sun(id, name, description);

        Assertions.assertEquals(id, sun.getId());
        Assertions.assertEquals(name, sun.getName());
        Assertions.assertEquals(description, sun.getDescription());
    }

    @Test
    public void shouldConvertSunToStringWithForeignKeys() {
        int id = 1;
        String name = "This is a custom Sun.";
        String description = "This is a custom description.";

        Sun sun = new Sun(id, name, description);

        String expected = "Sun(super=BaseEntity(name=This is a custom Sun., description=This is a custom description.), id=1)";

        Assertions.assertEquals(expected, sun.toString());
    }
}
