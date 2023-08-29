package com.mcommings.campaigner.services;

import com.mcommings.campaigner.interfaces.ITerrain;
import com.mcommings.campaigner.models.RepositoryHelper;
import com.mcommings.campaigner.models.Terrain;
import com.mcommings.campaigner.models.repositories.IPlaceRepository;
import com.mcommings.campaigner.models.repositories.ITerrainRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mcommings.campaigner.enums.ErrorMessage.*;
import static com.mcommings.campaigner.enums.ForeignKey.FK_TERRAIN;

@Service
public class TerrainService implements ITerrain {

    private final ITerrainRepository terrainRepository;
    private final IPlaceRepository placeRepository;

    @Autowired
    public TerrainService(ITerrainRepository terrainRepository, IPlaceRepository placeRepository) {
        this.terrainRepository = terrainRepository;
        this.placeRepository = placeRepository;
    }

    @Override
    public List<Terrain> getTerrains() {
        return terrainRepository.findAll();
    }

    @Override
    @Transactional
    public void saveTerrain(Terrain terrain) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.nameIsNullOrEmpty(terrain)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (RepositoryHelper.nameAlreadyExists(terrainRepository, terrain)) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }

        terrainRepository.saveAndFlush(terrain);
    }

    @Override
    @Transactional
    public void deleteTerrain(int terrainId) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(terrainRepository, terrainId)) {
            throw new IllegalArgumentException(DELETE_NOT_FOUND.message);
        }
        if (RepositoryHelper.isForeignKey(getReposWhereTerrainIsAForeignKey(), FK_TERRAIN.columnName, terrainId)) {
            throw new DataIntegrityViolationException(DELETE_FOREIGN_KEY.message);
        }

        terrainRepository.deleteById(terrainId);
    }

    @Override
    @Transactional
    public void updateTerrain(int terrainId, Terrain terrain) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(terrainRepository, terrainId)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }

        Terrain terrainToUpdate = RepositoryHelper.getById(terrainRepository, terrainId);
        terrainToUpdate.setName(terrain.getName());
        terrainToUpdate.setDescription(terrain.getDescription());
    }

    private List<CrudRepository> getReposWhereTerrainIsAForeignKey() {
        List<CrudRepository> repositories = new ArrayList<>(Arrays.asList(placeRepository));
        return repositories;
    }
}
