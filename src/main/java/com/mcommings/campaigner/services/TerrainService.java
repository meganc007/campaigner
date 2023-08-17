package com.mcommings.campaigner.services;

import com.mcommings.campaigner.interfaces.ITerrain;
import com.mcommings.campaigner.models.RepositoryHelper;
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
        if(RepositoryHelper.nameIsNullOrEmpty(terrain)) {
            throw new IllegalArgumentException("Terrain name cannot be null or empty.");
        }
        if(RepositoryHelper.nameAlreadyExists(terrainRepository, terrain)) {
            throw new DataIntegrityViolationException("Terrain already exists.");
        }

        terrainRepository.saveAndFlush(terrain);
    }

    @Override
    @Transactional
    public void deleteTerrain(int terrainId) throws IllegalArgumentException, DataIntegrityViolationException {
        if(RepositoryHelper.cannotFindId(terrainRepository, terrainId)) {
            throw new IllegalArgumentException("Unable to delete; This terrain not found.");
        }
        //TODO: check if foreign key

        terrainRepository.deleteById(terrainId);
    }

    @Override
    @Transactional
    public void updateTerrain(int terrainId, Terrain terrain) throws IllegalArgumentException, DataIntegrityViolationException {
        if(RepositoryHelper.cannotFindId(terrainRepository, terrainId)) {
            throw new IllegalArgumentException("Unable to update; This terrain not found.");
        }

        Terrain terrainToUpdate = RepositoryHelper.getById(terrainRepository, terrainId);
        terrainToUpdate.setName(terrain.getName());
        terrainToUpdate.setDescription(terrain.getDescription());
    }
}
