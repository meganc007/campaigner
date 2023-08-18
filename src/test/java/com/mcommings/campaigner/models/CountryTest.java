package com.mcommings.campaigner.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CountryTest {
    
    @Test
    public void shouldCreateADefaultCountryWithNoForeignKeys() {
        Country country = new Country();
        Assertions.assertNotNull(country);
        Assertions.assertEquals(0, country.getId());
        Assertions.assertNull(country.getName());
        Assertions.assertNull(country.getDescription());
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
        String expected = "Country(super=BaseEntity(name=Custom Country, description=This is a custom Country.), id=1, continentId=null, governmentId=null)";

        Assertions.assertEquals(expected, country.toString());
    }

    @Test
    public void shouldCreateADefaultCountryWithForeignKeys() {
        Country country = new Country();
        Assertions.assertNotNull(country);
        Assertions.assertEquals(0, country.getId());
        Assertions.assertNull(country.getName());
        Assertions.assertNull(country.getDescription());
        Assertions.assertNull(country.getContinentId());
        Assertions.assertNull(country.getGovernmentId());
    }

    @Test
    public void shouldCreateACustomCountryWithForeignKeys() {
        int id = 1;
        String name = "Custom Country";
        String description = "This is a custom Country.";
        Integer continentId = 1;
        Integer governmentId = 3;

        Country country = new Country(id, name, description, continentId, governmentId);

        Assertions.assertEquals(id, country.getId());
        Assertions.assertEquals(name, country.getName());
        Assertions.assertEquals(description, country.getDescription());
        Assertions.assertEquals(continentId, country.getContinentId());
        Assertions.assertEquals(governmentId, country.getGovernmentId());
    }

    @Test
    public void shouldConvertCountryToStringWithForeignKeys() {
        int id = 1;
        String name = "Custom Country";
        String description = "This is a custom Country.";
        Integer continentId = 1;
        Integer governmentId = 3;

        Country country = new Country(id, name, description, continentId, governmentId);
        String expected = "Country(super=BaseEntity(name=Custom Country, description=This is a custom Country.), id=1, continentId=1, governmentId=3)";

        Assertions.assertEquals(expected, country.toString());
    }
}
