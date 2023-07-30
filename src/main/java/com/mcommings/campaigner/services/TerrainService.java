package com.mcommings.campaigner.services;

import com.mcommings.campaigner.interfaces.ITerrain;
import com.mcommings.campaigner.models.ITerrainRepository;
import com.mcommings.campaigner.models.Terrain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TerrainService implements ITerrain {

    private final ITerrainRepository terrainRepository;

    @Autowired
    public TerrainService(ITerrainRepository terrainRepository) {this.terrainRepository = terrainRepository;}
    @Override
    public List<Terrain> getTerrains() {
        return terrainRepository.findAll();
    }
}
