package com.mcommings.campaigner.models.locations;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PlaceTest {
    @Test
    public void shouldCreateADefaultPlace() {
        Place place = new Place();
        Assertions.assertNotNull(place);
        Assertions.assertEquals(0, place.getId());
        Assertions.assertNull(place.getName());
        Assertions.assertNull(place.getDescription());
        Assertions.assertNull(place.getFk_place_type());
        Assertions.assertNull(place.getFk_terrain());
        Assertions.assertNull(place.getFk_country());
        Assertions.assertNull(place.getFk_city());
        Assertions.assertNull(place.getFk_region());
    }

    @Test
    public void shouldCreateACustomPlaceWithNoForeignKeys() {
        int id = 1;
        String name = "Custom Place";
        String description = "This is a custom Place.";

        Place place = new Place(id, name, description);

        Assertions.assertEquals(id, place.getId());
        Assertions.assertEquals(name, place.getName());
        Assertions.assertEquals(description, place.getDescription());
    }

    @Test
    public void shouldConvertPlaceToStringWithNoForeignKeys() {
        int id = 1;
        String name = "Custom Place";
        String description = "This is a custom Place.";

        Place place = new Place(id, name, description);
        String expected = "Place(super=BaseEntity(name=Custom Place, description=This is a custom Place.), id=1, fk_place_type=null, fk_terrain=null, fk_country=null, fk_city=null, fk_region=null, placeType=null, terrain=null, country=null, city=null, region=null)";

        Assertions.assertEquals(expected, place.toString());
    }

    @Test
    public void shouldCreateACustomPlaceWithForeignKeys() {
        int id = 1;
        String name = "Custom Place";
        String description = "This is a custom Place.";
        Integer fk_place_type = 1;
        Integer fk_terrain = 2;
        Integer fk_country = 3;
        Integer fk_city = 4;
        Integer fk_region = 5;

        Place place = new Place(id, name, description, fk_place_type, fk_terrain, fk_country, fk_city, fk_region);

        Assertions.assertEquals(id, place.getId());
        Assertions.assertEquals(name, place.getName());
        Assertions.assertEquals(description, place.getDescription());
        Assertions.assertEquals(fk_place_type, place.getFk_place_type());
        Assertions.assertEquals(fk_terrain, place.getFk_terrain());
        Assertions.assertEquals(fk_country, place.getFk_country());
        Assertions.assertEquals(fk_city, place.getFk_city());
        Assertions.assertEquals(fk_region, place.getFk_region());
    }

    @Test
    public void shouldConvertPlaceToStringWithForeignKeys() {
        int id = 1;
        String name = "Custom Place";
        String description = "This is a custom Place.";
        Integer fk_place_type = 1;
        Integer fk_terrain = 2;
        Integer fk_country = 3;
        Integer fk_city = 4;
        Integer fk_region = 5;

        Place place = new Place(id, name, description, fk_place_type, fk_terrain, fk_country, fk_city, fk_region);
        String expected = "Place(super=BaseEntity(name=Custom Place, description=This is a custom Place.), id=1, fk_place_type=1, fk_terrain=2, fk_country=3, fk_city=4, fk_region=5, placeType=null, terrain=null, country=null, city=null, region=null)";

        Assertions.assertEquals(expected, place.toString());
    }
}
