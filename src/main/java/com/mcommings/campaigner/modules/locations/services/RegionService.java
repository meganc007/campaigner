package com.mcommings.campaigner.modules.locations.services;

import com.mcommings.campaigner.modules.RepositoryHelper;
import com.mcommings.campaigner.modules.locations.dtos.RegionDTO;
import com.mcommings.campaigner.modules.locations.mappers.RegionMapper;
import com.mcommings.campaigner.modules.locations.repositories.IRegionRepository;
import com.mcommings.campaigner.modules.locations.services.interfaces.IRegion;
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
public class RegionService implements IRegion {

    private final IRegionRepository regionRepository;
    private final RegionMapper regionMapper;

    @Override
    public List<RegionDTO> getRegions() {
        return regionRepository.findAll()
                .stream()
                .map(regionMapper::mapToRegionDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<RegionDTO> getRegion(int regionId) {
        return regionRepository.findById(regionId)
                .map(regionMapper::mapToRegionDto);
    }

    @Override
    public List<RegionDTO> getRegionsByCampaignUUID(UUID uuid) {
        return regionRepository.findByfk_campaign_uuid(uuid)
                .stream()
                .map(regionMapper::mapToRegionDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RegionDTO> getRegionsByCountryId(int countryId) {
        return regionRepository.findByfk_country(countryId)
                .stream()
                .map(regionMapper::mapToRegionDto)
                .collect(Collectors.toList());
    }

    @Override
    public void saveRegion(RegionDTO region) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.nameIsNullOrEmpty(region)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (RepositoryHelper.nameAlreadyExists(regionRepository, region.getName())) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }
        regionMapper.mapToRegionDto(
                regionRepository.save(regionMapper.mapFromRegionDto(region))
        );
    }

    @Override
    public void deleteRegion(int regionId) throws IllegalArgumentException {
        if (RepositoryHelper.cannotFindId(regionRepository, regionId)) {
            throw new IllegalArgumentException(DELETE_NOT_FOUND.message);
        }
        regionRepository.deleteById(regionId);
    }

    @Override
    public Optional<RegionDTO> updateRegion(int regionId, RegionDTO region) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(regionRepository, regionId)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        if (RepositoryHelper.nameIsNullOrEmpty(region)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (RepositoryHelper.nameAlreadyExists(regionRepository, region.getName())) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }

        return regionRepository.findById(regionId).map(foundRegion -> {
            if (region.getName() != null) foundRegion.setName(region.getName());
            if (region.getDescription() != null) foundRegion.setDescription(region.getDescription());
            if (region.getFk_campaign_uuid() != null) foundRegion.setFk_campaign_uuid(region.getFk_campaign_uuid());
            if (region.getFk_country() != null) foundRegion.setFk_country(region.getFk_country());
            if (region.getFk_climate() != null) foundRegion.setFk_climate(region.getFk_climate());

            return regionMapper.mapToRegionDto(regionRepository.save(foundRegion));
        });
    }
}
