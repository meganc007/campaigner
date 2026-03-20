package com.mcommings.campaigner.modules.common.mappers;

import com.mcommings.campaigner.config.GlobalMapperConfig;
import com.mcommings.campaigner.modules.common.dtos.campaigns.CreateCampaignDTO;
import com.mcommings.campaigner.modules.common.dtos.campaigns.UpdateCampaignDTO;
import com.mcommings.campaigner.modules.common.dtos.campaigns.ViewCampaignDTO;
import com.mcommings.campaigner.modules.common.entities.Campaign;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = GlobalMapperConfig.class)
public interface CampaignMapper {

    ViewCampaignDTO toDto(Campaign entity);

    @Mapping(target = "uuid", ignore = true)
    Campaign toEntity(CreateCampaignDTO dto);

    @Mapping(target = "uuid", ignore = true)
    void updateEntity(UpdateCampaignDTO dto, @MappingTarget Campaign entity);
}
