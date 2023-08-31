package com.mcommings.campaigner.models.locations;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TerrainTest {
    @Test
    public void shouldCreateADefaultTerrain() {
        Terrain terrain = new Terrain();
        Assertions.assertNotNull(terrain);
        Assertions.assertEquals(0, terrain.getId());
        Assertions.assertNull(terrain.getName());
        Assertions.assertNull(terrain.getDescription());
    }

    @Test
    public void shouldCreateACustomTerrain() {
        int id = 1;
        String name = "Custom Terrain";
        String description = "This is a custom Terrain.";

        Terrain terrain = new Terrain(id, name, description);

        Assertions.assertEquals(id, terrain.getId());
        Assertions.assertEquals(name, terrain.getName());
        Assertions.assertEquals(description, terrain.getDescription());
    }
    @Test
    public void shouldConvertTerrainToString() {
        int id = 1;
        String name = "Custom Terrain";
        String description = "This is a custom Terrain.";

        Terrain terrain = new Terrain(id, name, description);
        String expected = "Terrain(super=BaseEntity(name=Custom Terrain, description=This is a custom Terrain.), id=1)";

        Assertions.assertEquals(expected, terrain.toString());
    }
}
