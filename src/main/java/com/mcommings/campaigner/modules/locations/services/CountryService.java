package com.mcommings.campaigner.modules.locations.services;

import com.mcommings.campaigner.config.BaseService;
import com.mcommings.campaigner.modules.common.repositories.ICampaignRepository;
import com.mcommings.campaigner.modules.locations.dtos.countries.CreateCountryDTO;
import com.mcommings.campaigner.modules.locations.dtos.countries.UpdateCountryDTO;
import com.mcommings.campaigner.modules.locations.dtos.countries.ViewCountryDTO;
import com.mcommings.campaigner.modules.locations.entities.Country;
import com.mcommings.campaigner.modules.locations.mappers.CountryMapper;
import com.mcommings.campaigner.modules.locations.repositories.ICountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CountryService extends BaseService<
        Country,
        Integer,
        ViewCountryDTO,
        CreateCountryDTO,
        UpdateCountryDTO> {

    private final ICountryRepository countryRepository;
    private final ICampaignRepository campaignRepository;
    private final CountryMapper countryMapper;

    @Override
    protected JpaRepository<Country, Integer> getRepository() {
        return countryRepository;
    }

    @Override
    protected ViewCountryDTO toViewDto(Country entity) {
        return countryMapper.toDto(entity);
    }

    @Override
    protected Country toEntity(CreateCountryDTO dto) {
        Country entity = countryMapper.toEntity(dto);

        entity.setCampaign(
                campaignRepository.getReferenceById(dto.getCampaignUuid())
        );
        return entity;
    }

    @Override
    protected void updateEntity(UpdateCountryDTO dto, Country entity) {
        countryMapper.updateCountryFromDto(dto, entity);

        if (dto.getCampaignUuid() != null) {
            entity.setCampaign(
                    campaignRepository.getReferenceById(dto.getCampaignUuid())
            );
        }
    }

    @Override
    protected Integer getId(UpdateCountryDTO dto) {
        return dto.getId();
    }

    public List<ViewCountryDTO> getCountriesByCampaignUUID(UUID uuid) {

        return query(countryRepository::findByCampaign_Uuid, uuid);
    }

    public List<ViewCountryDTO> getCountriesByContinentId(int continentId) {

        return query(countryRepository::findByContinent_Id, continentId);
    }

    public List<ViewCountryDTO> getCountriesByGovernmentId(int governmentId) {

        return query(countryRepository::findByGovernment_Id, governmentId);
    }

}
