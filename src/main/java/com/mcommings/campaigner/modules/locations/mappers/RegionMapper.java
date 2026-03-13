package com.mcommings.campaigner.modules.locations.mappers;

import com.mcommings.campaigner.config.GlobalMapperConfig;
import com.mcommings.campaigner.modules.locations.dtos.regions.CreateRegionDTO;
import com.mcommings.campaigner.modules.locations.dtos.regions.UpdateRegionDTO;
import com.mcommings.campaigner.modules.locations.dtos.regions.ViewRegionDTO;
import com.mcommings.campaigner.modules.locations.entities.Region;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = GlobalMapperConfig.class)
public interface RegionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "campaign", ignore = true)
    Region toEntity(CreateRegionDTO dto);

    @Mapping(source = "campaign.uuid", target = "campaignUuid")
    @Mapping(source = "country.id", target = "countryId")
    @Mapping(source = "climate.id", target = "climateId")
    ViewRegionDTO toDto(Region region);

    void updateRegionFromDto(
            UpdateRegionDTO dto,
            @MappingTarget Region entity
    );
}
