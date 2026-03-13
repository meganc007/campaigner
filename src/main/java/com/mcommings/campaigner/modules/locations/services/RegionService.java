package com.mcommings.campaigner.modules.locations.services;

import com.mcommings.campaigner.config.BaseService;
import com.mcommings.campaigner.modules.common.repositories.ICampaignRepository;
import com.mcommings.campaigner.modules.locations.dtos.regions.CreateRegionDTO;
import com.mcommings.campaigner.modules.locations.dtos.regions.UpdateRegionDTO;
import com.mcommings.campaigner.modules.locations.dtos.regions.ViewRegionDTO;
import com.mcommings.campaigner.modules.locations.entities.Region;
import com.mcommings.campaigner.modules.locations.mappers.RegionMapper;
import com.mcommings.campaigner.modules.locations.repositories.IRegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegionService extends BaseService<
        Region,
        Integer,
        ViewRegionDTO,
        CreateRegionDTO,
        UpdateRegionDTO> {

    private final IRegionRepository regionRepository;
    private final ICampaignRepository campaignRepository;
    private final RegionMapper regionMapper;

    @Override
    protected JpaRepository<Region, Integer> getRepository() {
        return regionRepository;
    }

    @Override
    protected ViewRegionDTO toViewDto(Region entity) {
        return regionMapper.toDto(entity);
    }

    @Override
    protected Region toEntity(CreateRegionDTO dto) {
        Region entity = regionMapper.toEntity(dto);

        entity.setCampaign(
                campaignRepository.getReferenceById(dto.getCampaignUuid())
        );
        return entity;
    }

    @Override
    protected void updateEntity(UpdateRegionDTO dto, Region entity) {
        regionMapper.updateRegionFromDto(dto, entity);

        if (dto.getCampaignUuid() != null) {
            entity.setCampaign(
                    campaignRepository.getReferenceById(dto.getCampaignUuid())
            );
        }
    }

    @Override
    protected Integer getId(UpdateRegionDTO dto) {
        return dto.getId();
    }

    public List<ViewRegionDTO> getRegionsByCampaignUUID(UUID uuid) {
        return query(regionRepository::findByCampaign_Uuid, uuid);
    }

    public List<ViewRegionDTO> getRegionsByClimateId(int climateId) {
        return query(regionRepository::findByClimate_Id, climateId);
    }

    public List<ViewRegionDTO> getRegionsByCountryId(int countryId) {
        return query(regionRepository::findByCountry_Id, countryId);
    }
}
