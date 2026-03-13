package com.mcommings.campaigner.setup.locations.builders;

import com.mcommings.campaigner.modules.locations.entities.Terrain;
import com.mcommings.campaigner.setup.locations.fixtures.LocationsTestConstants;

public class TerrainBuilder {
    private int id = LocationsTestConstants.TERRAIN_ID;
    private String name = LocationsTestConstants.TERRAIN_NAME;
    private String description = LocationsTestConstants.TERRAIN_DESCRIPTION;

    public static TerrainBuilder aTerrain() {
        return new TerrainBuilder();
    }

    public TerrainBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public TerrainBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public TerrainBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public Terrain build() {
        Terrain terrain = new Terrain();
        terrain.setId(id);
        terrain.setName(name);
        terrain.setDescription(description);

        return terrain;
    }
}
