package com.mcommings.campaigner.locations.mappers;

import com.mcommings.campaigner.locations.dtos.TerrainDTO;
import com.mcommings.campaigner.locations.entities.Terrain;
import org.mapstruct.Mapper;

@Mapper
public interface TerrainMapper {

    Terrain mapFromTerrainDto(TerrainDTO dto);

    TerrainDTO mapToTerrainDto(Terrain terrain);
}
