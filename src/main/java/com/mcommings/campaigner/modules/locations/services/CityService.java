package com.mcommings.campaigner.modules.locations.services;

import com.mcommings.campaigner.modules.common.entities.RepositoryHelper;
import com.mcommings.campaigner.modules.locations.dtos.CityDTO;
import com.mcommings.campaigner.modules.locations.mappers.CityMapper;
import com.mcommings.campaigner.modules.locations.repositories.ICityRepository;
import com.mcommings.campaigner.modules.locations.services.interfaces.ICity;
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
public class CityService implements ICity {

    private final ICityRepository cityRepository;
    private final CityMapper cityMapper;

    @Override
    public List<CityDTO> getCities() {
        return cityRepository.findAll()
                .stream()
                .map(cityMapper::mapToCityDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CityDTO> getCity(int cityId) {
        return cityRepository.findById(cityId)
                .map(cityMapper::mapToCityDto);
    }

    @Override
    public List<CityDTO> getCitiesByCampaignUUID(UUID uuid) {
        return cityRepository.findByfk_campaign_uuid(uuid)
                .stream()
                .map(cityMapper::mapToCityDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CityDTO> getCitiesByCountryId(int countryId) {
        return cityRepository.findByfk_country(countryId)
                .stream()
                .map(cityMapper::mapToCityDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CityDTO> getCitiesByRegionId(int regionId) {
        return cityRepository.findByfk_region(regionId)
                .stream()
                .map(cityMapper::mapToCityDto)
                .collect(Collectors.toList());
    }

    @Override
    public void saveCity(CityDTO city) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.nameIsNullOrEmpty(city)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (RepositoryHelper.nameAlreadyExists(cityRepository, city.getName())) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }
        cityMapper.mapToCityDto(
                cityRepository.save(cityMapper.mapFromCityDto(city)
                ));
    }

    @Override
    public void deleteCity(int cityId) throws IllegalArgumentException {
        if (RepositoryHelper.cannotFindId(cityRepository, cityId)) {
            throw new IllegalArgumentException(DELETE_NOT_FOUND.message);
        }
        cityRepository.deleteById(cityId);
    }

    @Override
    public Optional<CityDTO> updateCity(int cityId, CityDTO city) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(cityRepository, cityId)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        if (RepositoryHelper.nameIsNullOrEmpty(city)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (RepositoryHelper.nameAlreadyExists(cityRepository, city.getName())) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }

        return cityRepository.findById(cityId).map(foundCity -> {
            if (city.getName() != null) foundCity.setName(city.getName());
            if (city.getDescription() != null) foundCity.setDescription(city.getDescription());
            if (city.getFk_campaign_uuid() != null) foundCity.setFk_campaign_uuid(city.getFk_campaign_uuid());
            if (city.getFk_country() != null) foundCity.setFk_country(city.getFk_country());
            if (city.getFk_wealth() != null) foundCity.setFk_wealth(city.getFk_wealth());
            if (city.getFk_settlement() != null) foundCity.setFk_settlement(city.getFk_settlement());
            if (city.getFk_government() != null) foundCity.setFk_government(city.getFk_government());
            if (city.getFk_region() != null) foundCity.setFk_region(city.getFk_region());

            return cityMapper.mapToCityDto(cityRepository.save(foundCity));
        });
    }
}
