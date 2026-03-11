package com.mcommings.campaigner.modules.locations.mappers;

import com.mcommings.campaigner.config.GlobalMapperConfig;
import com.mcommings.campaigner.modules.locations.dtos.countries.CreateCountryDTO;
import com.mcommings.campaigner.modules.locations.dtos.countries.UpdateCountryDTO;
import com.mcommings.campaigner.modules.locations.dtos.countries.ViewCountryDTO;
import com.mcommings.campaigner.modules.locations.entities.Country;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = GlobalMapperConfig.class)
public interface CountryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "campaign", ignore = true)
    Country toEntity(CreateCountryDTO dto);

    @Mapping(source = "campaign.uuid", target = "campaignUuid")
    @Mapping(source = "continent.id", target = "continentId")
    @Mapping(source = "government.id", target = "governmentId")
    ViewCountryDTO toDto(Country country);

    void updateCountryFromDto(
            UpdateCountryDTO dto,
            @MappingTarget Country entity
    );
}
