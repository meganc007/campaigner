package com.mcommings.campaigner.modules.common.mappers;

import com.mcommings.campaigner.modules.common.dtos.CampaignDTO;
import com.mcommings.campaigner.modules.common.entities.Campaign;
import org.mapstruct.Mapper;

@Mapper
public interface CampaignMapper {
    Campaign mapFromCampaignDto(CampaignDTO dto);

    CampaignDTO mapToCampaignDto(Campaign campaign);
}
