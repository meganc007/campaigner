package com.mcommings.campaigner.modules.locations.services;

import com.mcommings.campaigner.config.BaseService;
import com.mcommings.campaigner.modules.locations.dtos.terrains.CreateTerrainDTO;
import com.mcommings.campaigner.modules.locations.dtos.terrains.UpdateTerrainDTO;
import com.mcommings.campaigner.modules.locations.dtos.terrains.ViewTerrainDTO;
import com.mcommings.campaigner.modules.locations.entities.Terrain;
import com.mcommings.campaigner.modules.locations.mappers.TerrainMapper;
import com.mcommings.campaigner.modules.locations.repositories.ITerrainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TerrainService extends BaseService<
        Terrain,
        Integer,
        ViewTerrainDTO,
        CreateTerrainDTO,
        UpdateTerrainDTO> {

    private final ITerrainRepository terrainRepository;
    private final TerrainMapper terrainMapper;

    @Override
    protected JpaRepository<Terrain, Integer> getRepository() {
        return terrainRepository;
    }

    @Override
    protected ViewTerrainDTO toViewDto(Terrain entity) {
        return terrainMapper.toDto(entity);
    }

    @Override
    protected Terrain toEntity(CreateTerrainDTO dto) {
        return terrainMapper.toEntity(dto);
    }

    @Override
    protected void updateEntity(UpdateTerrainDTO dto, Terrain entity) {
        terrainMapper.updateTerrainFromDto(dto, entity);
    }

    @Override
    protected Integer getId(UpdateTerrainDTO dto) {
        return dto.getId();
    }
}
