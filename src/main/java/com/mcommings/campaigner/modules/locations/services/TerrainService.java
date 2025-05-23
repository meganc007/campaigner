package com.mcommings.campaigner.modules.locations.services;

import com.mcommings.campaigner.modules.RepositoryHelper;
import com.mcommings.campaigner.modules.locations.dtos.TerrainDTO;
import com.mcommings.campaigner.modules.locations.mappers.TerrainMapper;
import com.mcommings.campaigner.modules.locations.repositories.ITerrainRepository;
import com.mcommings.campaigner.modules.locations.services.interfaces.ITerrain;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.mcommings.campaigner.enums.ErrorMessage.*;

@Service
@RequiredArgsConstructor
public class TerrainService implements ITerrain {

    private final ITerrainRepository terrainRepository;
    private final TerrainMapper terrainMapper;

    @Override
    public List<TerrainDTO> getTerrains() {

        return terrainRepository.findAll()
                .stream()
                .map(terrainMapper::mapToTerrainDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<TerrainDTO> getTerrain(int terrainId) {

        return terrainRepository.findById(terrainId)
                .map(terrainMapper::mapToTerrainDto);
    }

    @Override
    public void saveTerrain(TerrainDTO terrain) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.nameIsNullOrEmpty(terrain)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (RepositoryHelper.nameAlreadyExists(terrainRepository, terrain.getName())) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }

        terrainMapper.mapToTerrainDto(
                terrainRepository.save(
                        terrainMapper.mapFromTerrainDto(terrain))
        );
    }

    @Override
    public void deleteTerrain(int terrainId) throws IllegalArgumentException {
        if (RepositoryHelper.cannotFindId(terrainRepository, terrainId)) {
            throw new IllegalArgumentException(DELETE_NOT_FOUND.message);
        }
        terrainRepository.deleteById(terrainId);
    }

    @Override
    public Optional<TerrainDTO> updateTerrain(int terrainId, TerrainDTO terrain) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(terrainRepository, terrainId)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        if (RepositoryHelper.nameIsNullOrEmpty(terrain)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (RepositoryHelper.nameAlreadyExists(terrainRepository, terrain.getName())) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }

        return terrainRepository.findById(terrainId).map(foundTerrain -> {
            if (terrain.getName() != null) foundTerrain.setName(terrain.getName());
            if (terrain.getDescription() != null) foundTerrain.setDescription(terrain.getDescription());

            return terrainMapper.mapToTerrainDto(terrainRepository.save(foundTerrain));
        });
    }
}
