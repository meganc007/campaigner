package com.mcommings.campaigner.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PlaceTypeTest {

    @Test
    public void shouldCreateADefaultPlaceType() {
        PlaceType placeType = new PlaceType();
        Assertions.assertNotNull(placeType);
        Assertions.assertEquals(0, placeType.getId());
        Assertions.assertNull(placeType.getName());
        Assertions.assertNull(placeType.getDescription());
    }

    @Test
    public void shouldCreateACustomPlaceType() {
        int id = 1;
        String name = "Custom PlaceType";
        String description = "This is a custom PlaceType.";

        PlaceType placeType = new PlaceType(id, name, description);

        Assertions.assertEquals(id, placeType.getId());
        Assertions.assertEquals(name, placeType.getName());
        Assertions.assertEquals(description, placeType.getDescription());
    }
    @Test
    public void shouldConvertPlaceTypeToString() {
        int id = 1;
        String name = "Custom PlaceType";
        String description = "This is a custom PlaceType.";

        PlaceType placeType = new PlaceType(id, name, description);
        String expected = "PlaceType(super=BaseEntity(name=Custom PlaceType, description=This is a custom PlaceType.), id=1)";

        Assertions.assertEquals(expected, placeType.toString());
    }
}
