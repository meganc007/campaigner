package com.mcommings.campaigner.services;

import com.mcommings.campaigner.interfaces.ITerrain;
import com.mcommings.campaigner.models.Terrain;
import com.mcommings.campaigner.models.repositories.ITerrainRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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

    @Override
    @Transactional
    public void saveTerrain(Terrain terrain) throws IllegalArgumentException, DataIntegrityViolationException {

    }

    @Override
    @Transactional
    public void deleteTerrain(int terrainId) throws IllegalArgumentException, DataIntegrityViolationException {

    }

    @Override
    @Transactional
    public void updateTerrain(int terrainId, Terrain terrain) throws IllegalArgumentException, DataIntegrityViolationException {

    }
}
