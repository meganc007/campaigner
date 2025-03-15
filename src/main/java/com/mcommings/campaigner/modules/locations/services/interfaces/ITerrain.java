package com.mcommings.campaigner.modules.locations.services.interfaces;

import com.mcommings.campaigner.modules.locations.dtos.TerrainDTO;

import java.util.List;
import java.util.Optional;

public interface ITerrain {

    List<TerrainDTO> getTerrains();

    Optional<TerrainDTO> getTerrain(int terrainId);

    void saveTerrain(TerrainDTO terrain);

    void deleteTerrain(int terrainId);

    Optional<TerrainDTO> updateTerrain(int terrainId, TerrainDTO terrain);
}
