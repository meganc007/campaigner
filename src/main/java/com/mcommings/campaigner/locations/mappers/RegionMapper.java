package com.mcommings.campaigner.locations.mappers;

import com.mcommings.campaigner.locations.dtos.RegionDTO;
import com.mcommings.campaigner.locations.entities.Region;
import org.mapstruct.Mapper;

@Mapper
public interface RegionMapper {

    Region mapFromRegionDto(RegionDTO dto);

    RegionDTO mapToRegionDto(Region region);
}
