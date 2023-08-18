package com.mcommings.campaigner.models;

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
    }

    @Test
    public void shouldCreateACustomLandmark() {
        int id = 1;
        String name = "Custom Landmark";
        String description = "This is a custom Landmark.";

        Landmark landmark = new Landmark(id, name, description);

        Assertions.assertEquals(id, landmark.getId());
        Assertions.assertEquals(name, landmark.getName());
        Assertions.assertEquals(description, landmark.getDescription());
    }
    @Test
    public void shouldConvertLandmarkToString() {
        int id = 1;
        String name = "Custom Landmark";
        String description = "This is a custom Landmark.";

        Landmark landmark = new Landmark(id, name, description);
        String expected = "Landmark(super=BaseEntity(name=Custom Landmark, description=This is a custom Landmark.), id=1)";

        Assertions.assertEquals(expected, landmark.toString());
    }
}
