package com.mcommings.campaigner.modules.locations.mappers;

import com.mcommings.campaigner.modules.locations.dtos.TerrainDTO;
import com.mcommings.campaigner.modules.locations.entities.Terrain;
import org.mapstruct.Mapper;

@Mapper
public interface TerrainMapper {

    Terrain mapFromTerrainDto(TerrainDTO dto);

    TerrainDTO mapToTerrainDto(Terrain terrain);
}
