package com.mcommings.campaigner.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ClimateTest {

    @Test
    public void shouldCreateADefaultClimate() {
        Climate climate = new Climate();
        Assertions.assertNotNull(climate);
        Assertions.assertEquals(0, climate.getId());
        Assertions.assertNull(climate.getName());
        Assertions.assertNull(climate.getDescription());
    }

    @Test
    public void shouldCreateACustomClimate() {
        int id = 1;
        String name = "Custom Climate";
        String description = "This is a custom Climate.";

        Climate climate = new Climate(id, name, description);

        Assertions.assertEquals(id, climate.getId());
        Assertions.assertEquals(name, climate.getName());
        Assertions.assertEquals(description, climate.getDescription());
    }
    @Test
    public void shouldConvertClimateToString() {
        int id = 1;
        String name = "Custom Climate";
        String description = "This is a custom Climate.";

        Climate climate = new Climate(id, name, description);
        String expected = "Climate(super=BaseEntity(name=Custom Climate, description=This is a custom Climate.), id=1)";

        Assertions.assertEquals(expected, climate.toString());
    }
}
