package com.mcommings.campaigner.models.locations;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LandmarkTest {
    
    @Test
    public void shouldCreateADefaultLandmark() {
        Landmark landmark = new Landmark();
        Assertions.assertNotNull(landmark);
        Assertions.assertEquals(0, landmark.getId());
        Assertions.assertNull(landmark.getName());
        Assertions.assertNull(landmark.getDescription());
        Assertions.assertNull(landmark.getFk_region());
    }

    @Test
    public void shouldCreateACustomLandmark() {
        int id = 1;
        String name = "Custom Landmark";
        String description = "This is a custom Landmark.";
        Integer fk_region = 3;

        Landmark landmark = new Landmark(id, name, description, fk_region);

        Assertions.assertEquals(id, landmark.getId());
        Assertions.assertEquals(name, landmark.getName());
        Assertions.assertEquals(description, landmark.getDescription());
        Assertions.assertEquals(fk_region, landmark.getFk_region());
    }
    @Test
    public void shouldConvertLandmarkToString() {
        int id = 1;
        String name = "Custom Landmark";
        String description = "This is a custom Landmark.";
        Integer fk_region = 3;

        Landmark landmark = new Landmark(id, name, description, fk_region);

        String expected = "Landmark(super=BaseEntity(name=Custom Landmark, description=This is a custom Landmark.), id=1, fk_region=3)";

        Assertions.assertEquals(expected, landmark.toString());
    }
}
