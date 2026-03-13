package com.mcommings.campaigner.modules.locations.services;

import com.mcommings.campaigner.config.BaseService;
import com.mcommings.campaigner.modules.common.repositories.ICampaignRepository;
import com.mcommings.campaigner.modules.locations.dtos.cities.CreateCityDTO;
import com.mcommings.campaigner.modules.locations.dtos.cities.UpdateCityDTO;
import com.mcommings.campaigner.modules.locations.dtos.cities.ViewCityDTO;
import com.mcommings.campaigner.modules.locations.entities.City;
import com.mcommings.campaigner.modules.locations.mappers.CityMapper;
import com.mcommings.campaigner.modules.locations.repositories.ICityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CityService extends BaseService<
        City,
        Integer,
        ViewCityDTO,
        CreateCityDTO,
        UpdateCityDTO> {

    private final ICityRepository cityRepository;
    private final ICampaignRepository campaignRepository;
    private final CityMapper cityMapper;

    @Override
    protected JpaRepository<City, Integer> getRepository() {
        return cityRepository;
    }

    @Override
    protected ViewCityDTO toViewDto(City entity) {
        return cityMapper.toDto(entity);
    }

    @Override
    protected City toEntity(CreateCityDTO dto) {
        City entity = cityMapper.toEntity(dto);

        entity.setCampaign(
                campaignRepository.getReferenceById(dto.getCampaignUuid())
        );
        return entity;
    }

    @Override
    protected void updateEntity(UpdateCityDTO dto, City entity) {
        cityMapper.updateCityFromDto(dto, entity);

        if (dto.getCampaignUuid() != null) {
            entity.setCampaign(
                    campaignRepository.getReferenceById(dto.getCampaignUuid())
            );
        }
    }

    @Override
    protected Integer getId(UpdateCityDTO dto) {
        return dto.getId();
    }

    public List<ViewCityDTO> getCitiesByCampaignUUID(UUID uuid) {
        return query(cityRepository::findByCampaign_Uuid, uuid);
    }

    public List<ViewCityDTO> getCitiesByWealthId(int wealthId) {
        return query(cityRepository::findByWealth_Id, wealthId);
    }

    public List<ViewCityDTO> getCitiesByCountryId(int countryId) {
        return query(cityRepository::findByCountry_Id, countryId);
    }

    public List<ViewCityDTO> getCitiesBySettlementTypeId(int settlementTypeId) {
        return query(cityRepository::findBySettlementType_Id, settlementTypeId);
    }

    public List<ViewCityDTO> getCitiesByGovernmentId(int governmentId) {
        return query(cityRepository::findByGovernment_Id, governmentId);
    }

    public List<ViewCityDTO> getCitiesByRegionId(int regionId) {
        return query(cityRepository::findByRegion_Id, regionId);
    }

}
