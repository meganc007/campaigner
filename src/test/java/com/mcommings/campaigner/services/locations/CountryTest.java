package com.mcommings.campaigner.services.locations;


import com.mcommings.campaigner.modules.common.entities.Campaign;
import com.mcommings.campaigner.modules.common.repositories.ICampaignRepository;
import com.mcommings.campaigner.modules.locations.dtos.countries.CreateCountryDTO;
import com.mcommings.campaigner.modules.locations.dtos.countries.UpdateCountryDTO;
import com.mcommings.campaigner.modules.locations.dtos.countries.ViewCountryDTO;
import com.mcommings.campaigner.modules.locations.entities.Country;
import com.mcommings.campaigner.modules.locations.mappers.CountryMapper;
import com.mcommings.campaigner.modules.locations.repositories.ICountryRepository;
import com.mcommings.campaigner.modules.locations.services.CountryService;
import com.mcommings.campaigner.setup.locations.factories.LocationsTestDataFactory;
import com.mcommings.campaigner.setup.locations.fixtures.LocationsTestConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CountryTest {

    @Mock
    private ICountryRepository countryRepository;

    @Mock
    private ICampaignRepository campaignRepository;

    @Mock
    private CountryMapper countryMapper;

    @InjectMocks
    private CountryService countryService;

    private Country country;
    private ViewCountryDTO viewDto;
    private CreateCountryDTO createDto;
    private UpdateCountryDTO updateDto;

    @BeforeEach
    void setUp() {
        country = LocationsTestDataFactory.country();
        viewDto = LocationsTestDataFactory.viewCountryDTO();
        createDto = LocationsTestDataFactory.createCountryDTO();
        updateDto = LocationsTestDataFactory.updateCountryDTO();
    }

    @Test
    void getAll_returnsMappedDtos() {

        when(countryRepository.findAll()).thenReturn(List.of(country));
        when(countryMapper.toDto(country)).thenReturn(viewDto);

        List<ViewCountryDTO> result = countryService.getAll();

        assertEquals(1, result.size());
        assertEquals(viewDto, result.get(0));

        verify(countryRepository).findAll();
        verify(countryMapper).toDto(country);
    }

    @Test
    void getCountriesByCampaignUUID_returnsMappedList() {

        when(countryRepository.findByCampaign_Uuid(LocationsTestConstants.CAMPAIGN_UUID))
                .thenReturn(List.of(country));

        when(countryMapper.toDto(country))
                .thenReturn(viewDto);

        List<ViewCountryDTO> result =
                countryService.getCountriesByCampaignUUID(
                        LocationsTestConstants.CAMPAIGN_UUID);

        assertEquals(1, result.size());
        assertEquals(viewDto, result.get(0));

        verify(countryRepository)
                .findByCampaign_Uuid(LocationsTestConstants.CAMPAIGN_UUID);

        verify(countryMapper).toDto(country);
    }

    @Test
    void getCountriesByContinentId_returnsMappedList() {

        when(countryRepository.findByContinent_Id(LocationsTestConstants.CONTINENT_ID))
                .thenReturn(List.of(country));

        when(countryMapper.toDto(country))
                .thenReturn(viewDto);

        List<ViewCountryDTO> result =
                countryService.getCountriesByContinentId(
                        LocationsTestConstants.CONTINENT_ID);

        assertEquals(1, result.size());
        assertEquals(viewDto, result.get(0));

        verify(countryRepository)
                .findByContinent_Id((LocationsTestConstants.CONTINENT_ID));

        verify(countryMapper).toDto(country);
    }

    @Test
    void getCountriesByGovernmentId_returnsMappedList() {

        when(countryRepository.findByGovernment_Id(LocationsTestConstants.GOVERNMENT_ID))
                .thenReturn(List.of(country));

        when(countryMapper.toDto(country))
                .thenReturn(viewDto);

        List<ViewCountryDTO> result =
                countryService.getCountriesByGovernmentId(
                        LocationsTestConstants.GOVERNMENT_ID);

        assertEquals(1, result.size());
        assertEquals(viewDto, result.get(0));

        verify(countryRepository)
                .findByGovernment_Id(LocationsTestConstants.GOVERNMENT_ID);

        verify(countryMapper).toDto(country);
    }

    @Test
    void getById_whenExists_returnsDto() {

        when(countryRepository.findById(country.getId()))
                .thenReturn(Optional.of(country));

        when(countryMapper.toDto(country))
                .thenReturn(viewDto);

        ViewCountryDTO result = countryService.getById(country.getId());

        assertEquals(viewDto, result);

        verify(countryRepository).findById(country.getId());
        verify(countryMapper).toDto(country);
    }

    @Test
    void getById_whenMissing_throwsException() {

        when(countryRepository.findById(country.getId()))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> countryService.getById(country.getId())
        );

        verify(countryRepository).findById(country.getId());
    }

    @Test
    void create_whenValid_savesAndReturnsDto() {

        Campaign campaign = new Campaign();
        campaign.setUuid(createDto.getCampaignUuid());

        when(countryMapper.toEntity(createDto)).thenReturn(country);
        when(campaignRepository.getReferenceById(createDto.getCampaignUuid()))
                .thenReturn(campaign);

        when(countryRepository.save(country)).thenReturn(country);
        when(countryMapper.toDto(country)).thenReturn(viewDto);

        ViewCountryDTO result = countryService.create(createDto);

        assertEquals(viewDto, result);

        verify(countryMapper).toEntity(createDto);
        verify(campaignRepository).getReferenceById(createDto.getCampaignUuid());
        verify(countryRepository).save(country);
        verify(countryMapper).toDto(country);
    }

    @Test
    void update_whenValid_updatesAndReturnsDto() {

        Campaign campaign = new Campaign();
        campaign.setUuid(updateDto.getCampaignUuid());

        when(countryRepository.findById(updateDto.getId()))
                .thenReturn(Optional.of(country));

        when(campaignRepository.getReferenceById(updateDto.getCampaignUuid()))
                .thenReturn(campaign);

        when(countryRepository.save(country)).thenReturn(country);
        when(countryMapper.toDto(country)).thenReturn(viewDto);

        ViewCountryDTO result = countryService.update(updateDto);

        assertEquals(viewDto, result);

        verify(countryRepository).findById(updateDto.getId());
        verify(countryMapper).updateCountryFromDto(updateDto, country);
        verify(campaignRepository).getReferenceById(updateDto.getCampaignUuid());
        verify(countryRepository).save(country);
        verify(countryMapper).toDto(country);
    }

    @Test
    void update_whenMissing_throwsException() {

        when(countryRepository.findById(updateDto.getId()))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> countryService.update(updateDto)
        );

        verify(countryRepository).findById(updateDto.getId());
    }

    @Test
    void delete_callsRepository() {

        countryService.delete(country.getId());

        verify(countryRepository).deleteById(country.getId());
    }
}
