package com.mcommings.campaigner.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ContinentTest {
    
    @Test
    public void shouldCreateADefaultContinent() {
        Continent continent = new Continent();
        Assertions.assertNotNull(continent);
        Assertions.assertEquals(0, continent.getId());
        Assertions.assertNull(continent.getName());
        Assertions.assertNull(continent.getDescription());
    }

    @Test
    public void shouldCreateACustomContinent() {
        int id = 1;
        String name = "Custom Continent";
        String description = "This is a custom Continent.";

        Continent continent = new Continent(id, name, description);

        Assertions.assertEquals(id, continent.getId());
        Assertions.assertEquals(name, continent.getName());
        Assertions.assertEquals(description, continent.getDescription());
    }
    @Test
    public void shouldConvertContinentToString() {
        int id = 1;
        String name = "Custom Continent";
        String description = "This is a custom Continent.";

        Continent continent = new Continent(id, name, description);
        String expected = "Continent(super=BaseEntity(name=Custom Continent, description=This is a custom Continent.), id=1)";

        Assertions.assertEquals(expected, continent.toString());
    }
}
