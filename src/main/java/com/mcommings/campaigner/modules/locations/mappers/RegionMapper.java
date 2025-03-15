package com.mcommings.campaigner.modules.locations.mappers;

import com.mcommings.campaigner.modules.locations.dtos.RegionDTO;
import com.mcommings.campaigner.modules.locations.entities.Region;
import org.mapstruct.Mapper;

@Mapper
public interface RegionMapper {

    Region mapFromRegionDto(RegionDTO dto);

    RegionDTO mapToRegionDto(Region region);
}
