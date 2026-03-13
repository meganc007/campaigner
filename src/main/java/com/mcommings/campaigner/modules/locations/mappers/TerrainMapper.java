package com.mcommings.campaigner.modules.locations.mappers;

import com.mcommings.campaigner.config.GlobalMapperConfig;
import com.mcommings.campaigner.modules.locations.dtos.terrains.CreateTerrainDTO;
import com.mcommings.campaigner.modules.locations.dtos.terrains.UpdateTerrainDTO;
import com.mcommings.campaigner.modules.locations.dtos.terrains.ViewTerrainDTO;
import com.mcommings.campaigner.modules.locations.entities.Terrain;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = GlobalMapperConfig.class)
public interface TerrainMapper {

    @Mapping(target = "id", ignore = true)
    Terrain toEntity(CreateTerrainDTO dto);

    ViewTerrainDTO toDto(Terrain terrain);

    void updateTerrainFromDto(
            UpdateTerrainDTO dto,
            @MappingTarget Terrain entity
    );
}
