package com.mcommings.campaigner.common.services;

import com.mcommings.campaigner.common.entities.Campaign;
import com.mcommings.campaigner.common.entities.RepositoryHelper;
import com.mcommings.campaigner.common.repositories.ICampaignRepository;
import com.mcommings.campaigner.common.services.interfaces.ICampaign;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.mcommings.campaigner.enums.ErrorMessage.*;

@Service
public class CampaignService implements ICampaign {
    
    private final ICampaignRepository campaignRepository;

    @Autowired
    public CampaignService(ICampaignRepository campaignRepository) {
        this.campaignRepository = campaignRepository;
    }

    @Override
    public List<Campaign> getCampaigns() {
        return campaignRepository.findAll();
    }

    @Override
    public Campaign getCampaign(UUID uuid) {
        if (cannotFindUuid(uuid)) {
            throw new IllegalArgumentException(ID_NOT_FOUND.message);
        }
        return getByUuid(uuid);
    }

    @Override
    @Transactional
    public void saveCampaign(Campaign campaign) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.nameIsNullOrEmpty(campaign)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (RepositoryHelper.nameAlreadyExists(campaignRepository, campaign)) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }

        campaignRepository.saveAndFlush(campaign);
    }
    
    @Override
    @Transactional
    public void deleteCampaign(UUID uuid) throws IllegalArgumentException, DataIntegrityViolationException {
        if (cannotFindUuid(uuid)) {
            throw new IllegalArgumentException(DELETE_NOT_FOUND.message);
        }
        // TODO: need to think about this - (Campaign uuid is a fk on 30+ tables)
//        if (RepositoryHelper.isForeignKey(getReposWhereCampaignIsAForeignKey(), FK_CAMPAIGN.columnName, campaignId)) {
//            throw new DataIntegrityViolationException(DELETE_FOREIGN_KEY.message);
//        }
        campaignRepository.deleteByUuid(uuid);
    }
    
    @Override
    @Transactional
    public void updateCampaign(UUID uuid, Campaign campaign) throws IllegalArgumentException, DataIntegrityViolationException {
        if (cannotFindUuid(uuid)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        Campaign campaignToUpdate = getByUuid(uuid);
        if (campaign.getName() != null) campaignToUpdate.setName(campaign.getName());
        if (campaign.getDescription() != null) campaignToUpdate.setDescription(campaign.getDescription());
    }

    private boolean cannotFindUuid(UUID uuid) {
        return !campaignRepository.findByUuid(uuid).isPresent();
    }

    private Campaign getByUuid(UUID uuid) {
        return campaignRepository.findByUuid(uuid).get();
    }
}
