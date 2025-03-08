package com.mcommings.campaigner.services.locations;

import com.mcommings.campaigner.entities.RepositoryHelper;
import com.mcommings.campaigner.entities.locations.Terrain;
import com.mcommings.campaigner.interfaces.locations.ITerrain;
import com.mcommings.campaigner.repositories.locations.IPlaceRepository;
import com.mcommings.campaigner.repositories.locations.ITerrainRepository;
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
        if (terrain.getName() != null) terrainToUpdate.setName(terrain.getName());
        if (terrain.getDescription() != null) terrainToUpdate.setDescription(terrain.getDescription());
    }

    private List<CrudRepository> getReposWhereTerrainIsAForeignKey() {
        return new ArrayList<>(Arrays.asList(placeRepository));
    }
}
