package com.mcommings.campaigner.interfaces.locations;

import com.mcommings.campaigner.models.locations.Terrain;

import java.util.List;

public interface ITerrain {

    List<Terrain> getTerrains();

    void saveTerrain(Terrain terrain);

    void deleteTerrain(int terrainId);

    void updateTerrain(int terrainId, Terrain terrain);
}
