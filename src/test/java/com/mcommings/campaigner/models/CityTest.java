package com.mcommings.campaigner.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CityTest {
    
    @Test
    public void shouldCreateADefaultCityWithNoForeignKeys() {
        City city = new City();
        Assertions.assertNotNull(city);
        Assertions.assertEquals(0, city.getId());
        Assertions.assertNull(city.getName());
        Assertions.assertNull(city.getDescription());
    }

    @Test
    public void shouldCreateACustomCityWithNoForeignKeys() {
        int id = 1;
        String name = "Custom City";
        String description = "This is a custom City.";

        City city = new City(id, name, description);

        Assertions.assertEquals(id, city.getId());
        Assertions.assertEquals(name, city.getName());
        Assertions.assertEquals(description, city.getDescription());
    }
    @Test
    public void shouldConvertCityToStringWithNoForeignKeys() {
        int id = 1;
        String name = "Custom City";
        String description = "This is a custom City.";

        City city = new City(id, name, description);
        String expected = "City(super=BaseEntity(name=Custom City, description=This is a custom City.), id=1, fk_wealth=null, fk_country=null, fk_settlement=null, fk_government=null, wealth=null, country=null, settlementType=null, government=null)";

        Assertions.assertEquals(expected, city.toString());
    }

    @Test
    public void shouldCreateADefaultCityWithForeignKeys() {
        City city = new City();
        Assertions.assertNotNull(city);
        Assertions.assertEquals(0, city.getId());
        Assertions.assertNull(city.getName());
        Assertions.assertNull(city.getDescription());
        Assertions.assertNull(city.getFk_wealth());
        Assertions.assertNull(city.getFk_country());
        Assertions.assertNull(city.getFk_settlement());
        Assertions.assertNull(city.getFk_government());
    }

    @Test
    public void shouldCreateACustomCityWithForeignKeys() {
        int id = 1;
        String name = "Custom City";
        String description = "This is a custom City.";
        Integer fk_wealth = 1;
        Integer fk_country = 2;
        Integer fk_settlement = 3;
        Integer fk_government = 4;


        City city = new City(id, name, description, fk_wealth, fk_country, fk_settlement, fk_government);

        Assertions.assertEquals(id, city.getId());
        Assertions.assertEquals(name, city.getName());
        Assertions.assertEquals(description, city.getDescription());
        Assertions.assertEquals(fk_wealth, city.getFk_wealth());
        Assertions.assertEquals(fk_country, city.getFk_country());
        Assertions.assertEquals(fk_settlement, city.getFk_settlement());
        Assertions.assertEquals(fk_government, city.getFk_government());
    }

    @Test
    public void shouldConvertCityToStringWithForeignKeys() {
        int id = 1;
        String name = "Custom City";
        String description = "This is a custom City.";
        Integer fk_wealth = 1;
        Integer fk_country = 2;
        Integer fk_settlement = 3;
        Integer fk_government = 4;

        City city = new City(id, name, description, fk_wealth, fk_country, fk_settlement, fk_government);
        String expected = "City(super=BaseEntity(name=Custom City, description=This is a custom City.), id=1, fk_wealth=1, fk_country=2, fk_settlement=3, fk_government=4, wealth=null, country=null, settlementType=null, government=null)";

        Assertions.assertEquals(expected, city.toString());
    }
}
