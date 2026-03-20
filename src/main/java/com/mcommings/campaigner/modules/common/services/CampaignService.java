package com.mcommings.campaigner.modules.common.services;

import com.mcommings.campaigner.config.BaseService;
import com.mcommings.campaigner.modules.common.dtos.campaigns.CreateCampaignDTO;
import com.mcommings.campaigner.modules.common.dtos.campaigns.UpdateCampaignDTO;
import com.mcommings.campaigner.modules.common.dtos.campaigns.ViewCampaignDTO;
import com.mcommings.campaigner.modules.common.entities.Campaign;
import com.mcommings.campaigner.modules.common.mappers.CampaignMapper;
import com.mcommings.campaigner.modules.common.repositories.ICampaignRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CampaignService extends BaseService<
        Campaign,
        UUID,
        ViewCampaignDTO,
        CreateCampaignDTO,
        UpdateCampaignDTO> {

    private final ICampaignRepository campaignRepository;
    private final CampaignMapper campaignMapper;

    @Override
    protected JpaRepository<Campaign, UUID> getRepository() {
        return campaignRepository;
    }

    @Override
    protected ViewCampaignDTO toViewDto(Campaign entity) {
        return campaignMapper.toDto(entity);
    }

    @Override
    protected Campaign toEntity(CreateCampaignDTO dto) {
        return campaignMapper.toEntity(dto);
    }

    @Override
    protected void updateEntity(UpdateCampaignDTO dto, Campaign entity) {
        campaignMapper.updateEntity(dto, entity);
    }

    @Override
    protected UUID getId(UpdateCampaignDTO dto) {
        return dto.getUuid();
    }
}
