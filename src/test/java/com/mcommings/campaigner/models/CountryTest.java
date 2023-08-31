package com.mcommings.campaigner.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CountryTest {
    
    @Test
    public void shouldCreateADefaultCountry() {
        Country country = new Country();
        Assertions.assertNotNull(country);
        Assertions.assertEquals(0, country.getId());
        Assertions.assertNull(country.getName());
        Assertions.assertNull(country.getDescription());
        Assertions.assertNull(country.getFk_continent());
        Assertions.assertNull(country.getFk_government());
    }

    @Test
    public void shouldCreateACustomCountryWithNoForeignKeys() {
        int id = 1;
        String name = "Custom Country";
        String description = "This is a custom Country.";

        Country country = new Country(id, name, description);

        Assertions.assertEquals(id, country.getId());
        Assertions.assertEquals(name, country.getName());
        Assertions.assertEquals(description, country.getDescription());
    }
    @Test
    public void shouldConvertCountryToStringWithNoForeignKeys() {
        int id = 1;
        String name = "Custom Country";
        String description = "This is a custom Country.";

        Country country = new Country(id, name, description);
        String expected = "Country(super=BaseEntity(name=Custom Country, description=This is a custom Country.), id=1, fk_continent=null, fk_government=null, continent=null, government=null)";

        Assertions.assertEquals(expected, country.toString());
    }

    @Test
    public void shouldCreateACustomCountryWithForeignKeys() {
        int id = 1;
        String name = "Custom Country";
        String description = "This is a custom Country.";
        Integer fk_continent = 1;
        Integer fk_government = 3;

        Country country = new Country(id, name, description, fk_continent, fk_government);

        Assertions.assertEquals(id, country.getId());
        Assertions.assertEquals(name, country.getName());
        Assertions.assertEquals(description, country.getDescription());
        Assertions.assertEquals(fk_continent, country.getFk_continent());
        Assertions.assertEquals(fk_government, country.getFk_government());
    }

    @Test
    public void shouldConvertCountryToStringWithForeignKeys() {
        int id = 1;
        String name = "Custom Country";
        String description = "This is a custom Country.";
        Integer continentId = 1;
        Integer governmentId = 3;

        Country country = new Country(id, name, description, continentId, governmentId);
        String expected = "Country(super=BaseEntity(name=Custom Country, description=This is a custom Country.), id=1, fk_continent=1, fk_government=3, continent=null, government=null)";

        Assertions.assertEquals(expected, country.toString());
    }
}
