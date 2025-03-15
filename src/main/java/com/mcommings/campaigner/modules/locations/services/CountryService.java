package com.mcommings.campaigner.modules.locations.services;

import com.mcommings.campaigner.modules.common.entities.RepositoryHelper;
import com.mcommings.campaigner.modules.locations.dtos.CountryDTO;
import com.mcommings.campaigner.modules.locations.mappers.CountryMapper;
import com.mcommings.campaigner.modules.locations.repositories.ICountryRepository;
import com.mcommings.campaigner.modules.locations.services.interfaces.ICountry;
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
public class CountryService implements ICountry {

    private final ICountryRepository countryRepository;
    private final CountryMapper countryMapper;

    @Override
    public List<CountryDTO> getCountries() {
        return countryRepository.findAll()
                .stream()
                .map(countryMapper::mapToCountryDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CountryDTO> getCountry(int countryId) {
        return countryRepository.findById(countryId)
                .map(countryMapper::mapToCountryDto);
    }

    @Override
    public List<CountryDTO> getCountriesByCampaignUUID(UUID uuid) {
        return countryRepository.findByfk_campaign_uuid(uuid)
                .stream()
                .map(countryMapper::mapToCountryDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CountryDTO> getCountriesByContinentId(int continentId) {
        return countryRepository.findByfk_continent(continentId)
                .stream()
                .map(countryMapper::mapToCountryDto)
                .collect(Collectors.toList());
    }

    @Override
    public void saveCountry(CountryDTO country) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.nameIsNullOrEmpty(country)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (RepositoryHelper.nameAlreadyExists(countryRepository, country.getName())) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }
        countryMapper.mapToCountryDto(
                countryRepository.save(countryMapper.mapFromCountryDto(country)
                ));
    }

    @Override
    public void deleteCountry(int countryId) throws IllegalArgumentException {
        if (RepositoryHelper.cannotFindId(countryRepository, countryId)) {
            throw new IllegalArgumentException(DELETE_NOT_FOUND.message);
        }
        countryRepository.deleteById(countryId);
    }

    @Override
    public Optional<CountryDTO> updateCountry(int countryId, CountryDTO country) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(countryRepository, countryId)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        if (RepositoryHelper.nameIsNullOrEmpty(country)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (RepositoryHelper.nameAlreadyExists(countryRepository, country.getName())) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }

        return countryRepository.findById(countryId).map(foundCountry -> {
            if (country.getName() != null) foundCountry.setName(country.getName());
            if (country.getDescription() != null) foundCountry.setDescription(country.getDescription());
            if (country.getFk_campaign_uuid() != null) foundCountry.setFk_campaign_uuid(country.getFk_campaign_uuid());
            if (country.getFk_continent() != null) foundCountry.setFk_continent(country.getFk_continent());
            if (country.getFk_government() != null) foundCountry.setFk_government(country.getFk_government());

            return countryMapper.mapToCountryDto(countryRepository.save(foundCountry));
        });
    }
}
