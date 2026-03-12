package com.mcommings.campaigner.modules.locations.mappers;

import com.mcommings.campaigner.config.GlobalMapperConfig;
import com.mcommings.campaigner.modules.locations.dtos.cities.CreateCityDTO;
import com.mcommings.campaigner.modules.locations.dtos.cities.UpdateCityDTO;
import com.mcommings.campaigner.modules.locations.dtos.cities.ViewCityDTO;
import com.mcommings.campaigner.modules.locations.entities.City;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = GlobalMapperConfig.class)
public interface CityMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "campaign", ignore = true)
    City toEntity(CreateCityDTO dto);

    @Mapping(source = "campaign.uuid", target = "campaignUuid")
    @Mapping(source = "wealth.id", target = "wealthId")
    @Mapping(source = "country.id", target = "countryId")
    @Mapping(source = "settlementType.id", target = "settlementTypeId")
    @Mapping(source = "government.id", target = "governmentId")
    @Mapping(source = "region.id", target = "regionId")
    ViewCityDTO toDto(City city);

    void updateCityFromDto(
            UpdateCityDTO dto,
            @MappingTarget City entity
    );
}
