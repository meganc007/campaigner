package com.mcommings.campaigner.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GovernmentTest {

    @Test
    public void shouldCreateADefaultGovernment() {
        Government government = new Government();
        Assertions.assertNotNull(government);
        Assertions.assertEquals(0, government.getId());
        Assertions.assertNull(government.getName());
        Assertions.assertNull(government.getDescription());
    }

    @Test
    public void shouldCreateACustomGovernment() {
        int id = 1;
        String name = "Custom Government";
        String description = "This is a custom Government.";

        Government government = new Government(id, name, description);

        Assertions.assertEquals(id, government.getId());
        Assertions.assertEquals(name, government.getName());
        Assertions.assertEquals(description, government.getDescription());
    }
    @Test
    public void shouldConvertGovernmentToString() {
        int id = 1;
        String name = "Custom Government";
        String description = "This is a custom Government.";

        Government government = new Government(id, name, description);
        String expected = "Government(super=BaseEntity(name=Custom Government, description=This is a custom Government.), id=1)";

        Assertions.assertEquals(expected, government.toString());
    }
}
