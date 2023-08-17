package com.mcommings.campaigner.interfaces;

import com.mcommings.campaigner.models.Terrain;

import java.util.List;

public interface ITerrain {

    List<Terrain> getTerrains();

    void saveTerrain(Terrain terrain);

    void deleteTerrain(int terrainId);

    void updateTerrain(int terrainId, Terrain terrain);
}
