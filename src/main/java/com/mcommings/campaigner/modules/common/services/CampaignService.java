package com.mcommings.campaigner.modules.common.services;

import com.mcommings.campaigner.modules.RepositoryHelper;
import com.mcommings.campaigner.modules.common.dtos.CampaignDTO;
import com.mcommings.campaigner.modules.common.mappers.CampaignMapper;
import com.mcommings.campaigner.modules.common.repositories.ICampaignRepository;
import com.mcommings.campaigner.modules.common.services.interfaces.ICampaign;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.mcommings.campaigner.enums.ErrorMessage.*;

@Service
@RequiredArgsConstructor
public class CampaignService implements ICampaign {
    
    private final ICampaignRepository campaignRepository;
    private final CampaignMapper campaignMapper;

    @Override
    public List<CampaignDTO> getCampaigns() {

        return campaignRepository.findAll()
                .stream()
                .map(campaignMapper::mapToCampaignDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CampaignDTO> getCampaign(UUID uuid) {
        return campaignRepository.findByUuid(uuid)
                .map(campaignMapper::mapToCampaignDto);
    }

    @Override
    @Transactional
    public void saveCampaign(CampaignDTO campaign) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.nameIsNullOrEmpty(campaign)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (nameAlreadyExists(campaign)) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }

        campaignMapper.mapToCampaignDto(
                campaignRepository.save(
                        campaignMapper.mapFromCampaignDto(campaign)
                ));
    }
    
    @Override
    @Transactional
    public void deleteCampaign(UUID uuid) throws IllegalArgumentException, DataIntegrityViolationException {
        if (cannotFindUuid(uuid)) {
            System.out.println("couldn't find it");
            throw new IllegalArgumentException(DELETE_NOT_FOUND.message);
        }
        campaignRepository.deleteByUuid(uuid);
    }
    
    @Override
    public Optional<CampaignDTO> updateCampaign(UUID uuid, CampaignDTO campaign) throws IllegalArgumentException, DataIntegrityViolationException {
        if (cannotFindUuid(uuid)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        if (nameAlreadyExists(campaign)) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }

        return campaignRepository.findByUuid(uuid).map(foundCampaign -> {
            if (campaign.getName() != null) foundCampaign.setName(campaign.getName());
            if (campaign.getDescription() != null) foundCampaign.setDescription(campaign.getDescription());

            return campaignMapper.mapToCampaignDto(campaignRepository.save(foundCampaign));
        });
    }

    private boolean cannotFindUuid(UUID uuid) {
        return !campaignRepository.findByUuid(uuid).isPresent();
    }

    private boolean nameAlreadyExists(CampaignDTO campaign) {
        return campaignRepository.findByName(campaign.getName()).isPresent();
    }
}
