package com.mcommings.campaigner.services.locations;

import com.mcommings.campaigner.modules.common.entities.Campaign;
import com.mcommings.campaigner.modules.common.repositories.ICampaignRepository;
import com.mcommings.campaigner.modules.locations.dtos.cities.CreateCityDTO;
import com.mcommings.campaigner.modules.locations.dtos.cities.UpdateCityDTO;
import com.mcommings.campaigner.modules.locations.dtos.cities.ViewCityDTO;
import com.mcommings.campaigner.modules.locations.entities.City;
import com.mcommings.campaigner.modules.locations.mappers.CityMapper;
import com.mcommings.campaigner.modules.locations.repositories.ICityRepository;
import com.mcommings.campaigner.modules.locations.services.CityService;
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
public class CityTest {

    @Mock
    private ICityRepository cityRepository;

    @Mock
    private ICampaignRepository campaignRepository;

    @Mock
    private CityMapper cityMapper;

    @InjectMocks
    private CityService cityService;

    private City city;
    private ViewCityDTO viewDto;
    private CreateCityDTO createDto;
    private UpdateCityDTO updateDto;

    @BeforeEach
    void setUp() {
        city = LocationsTestDataFactory.city();
        viewDto = LocationsTestDataFactory.viewCityDTO();
        createDto = LocationsTestDataFactory.createCityDTO();
        updateDto = LocationsTestDataFactory.updateCityDTO();
    }

    @Test
    void getAll_returnsMappedDtos() {

        when(cityRepository.findAll()).thenReturn(List.of(city));
        when(cityMapper.toDto(city)).thenReturn(viewDto);

        List<ViewCityDTO> result = cityService.getAll();

        assertEquals(1, result.size());
        assertEquals(viewDto, result.get(0));

        verify(cityRepository).findAll();
        verify(cityMapper).toDto(city);
    }

    @Test
    void getCitiesByCampaignUUID_returnsMappedList() {

        when(cityRepository.findByCampaign_Uuid(LocationsTestConstants.CAMPAIGN_UUID))
                .thenReturn(List.of(city));

        when(cityMapper.toDto(city))
                .thenReturn(viewDto);

        List<ViewCityDTO> result =
                cityService.getCitiesByCampaignUUID(
                        LocationsTestConstants.CAMPAIGN_UUID);

        assertEquals(1, result.size());
        assertEquals(viewDto, result.get(0));

        verify(cityRepository)
                .findByCampaign_Uuid(LocationsTestConstants.CAMPAIGN_UUID);

        verify(cityMapper).toDto(city);
    }

    @Test
    void getCitiesByWealthId_returnsMappedList() {
        when(cityRepository.findByWealth_Id(LocationsTestConstants.CONTINENT_ID))
                .thenReturn(List.of(city));

        when(cityMapper.toDto(city))
                .thenReturn(viewDto);

        List<ViewCityDTO> result =
                cityService.getCitiesByWealthId(
                        LocationsTestConstants.CONTINENT_ID);

        assertEquals(1, result.size());
        assertEquals(viewDto, result.get(0));

        verify(cityRepository)
                .findByWealth_Id((LocationsTestConstants.CONTINENT_ID));

        verify(cityMapper).toDto(city);
    }

    @Test
    void getCitiesByCountryId_returnsMappedList() {
        when(cityRepository.findByCountry_Id(LocationsTestConstants.CONTINENT_ID))
                .thenReturn(List.of(city));

        when(cityMapper.toDto(city))
                .thenReturn(viewDto);

        List<ViewCityDTO> result =
                cityService.getCitiesByCountryId(
                        LocationsTestConstants.CONTINENT_ID);

        assertEquals(1, result.size());
        assertEquals(viewDto, result.get(0));

        verify(cityRepository)
                .findByCountry_Id((LocationsTestConstants.CONTINENT_ID));

        verify(cityMapper).toDto(city);
    }

    @Test
    void getCitiesBySettlementTypeId_returnsMappedList() {
        when(cityRepository.findBySettlementType_Id(LocationsTestConstants.CONTINENT_ID))
                .thenReturn(List.of(city));

        when(cityMapper.toDto(city))
                .thenReturn(viewDto);

        List<ViewCityDTO> result =
                cityService.getCitiesBySettlementTypeId(
                        LocationsTestConstants.CONTINENT_ID);

        assertEquals(1, result.size());
        assertEquals(viewDto, result.get(0));

        verify(cityRepository)
                .findBySettlementType_Id((LocationsTestConstants.CONTINENT_ID));

        verify(cityMapper).toDto(city);
    }

    @Test
    void getCitiesByGovernmentId_returnsMappedList() {
        when(cityRepository.findByGovernment_Id(LocationsTestConstants.CONTINENT_ID))
                .thenReturn(List.of(city));

        when(cityMapper.toDto(city))
                .thenReturn(viewDto);

        List<ViewCityDTO> result =
                cityService.getCitiesByGovernmentId(
                        LocationsTestConstants.CONTINENT_ID);

        assertEquals(1, result.size());
        assertEquals(viewDto, result.get(0));

        verify(cityRepository)
                .findByGovernment_Id((LocationsTestConstants.CONTINENT_ID));

        verify(cityMapper).toDto(city);
    }

    @Test
    void getCitiesByRegionId_returnsMappedList() {
        when(cityRepository.findByRegion_Id(LocationsTestConstants.CONTINENT_ID))
                .thenReturn(List.of(city));

        when(cityMapper.toDto(city))
                .thenReturn(viewDto);

        List<ViewCityDTO> result =
                cityService.getCitiesByRegionId(
                        LocationsTestConstants.CONTINENT_ID);

        assertEquals(1, result.size());
        assertEquals(viewDto, result.get(0));

        verify(cityRepository)
                .findByRegion_Id((LocationsTestConstants.CONTINENT_ID));

        verify(cityMapper).toDto(city);
    }

    @Test
    void getById_whenExists_returnsDto() {

        when(cityRepository.findById(city.getId()))
                .thenReturn(Optional.of(city));

        when(cityMapper.toDto(city))
                .thenReturn(viewDto);

        ViewCityDTO result = cityService.getById(city.getId());

        assertEquals(viewDto, result);

        verify(cityRepository).findById(city.getId());
        verify(cityMapper).toDto(city);
    }

    @Test
    void getById_whenMissing_throwsException() {

        when(cityRepository.findById(city.getId()))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> cityService.getById(city.getId())
        );

        verify(cityRepository).findById(city.getId());
    }

    @Test
    void create_whenValid_savesAndReturnsDto() {

        Campaign campaign = new Campaign();
        campaign.setUuid(createDto.getCampaignUuid());

        when(cityMapper.toEntity(createDto)).thenReturn(city);
        when(campaignRepository.getReferenceById(createDto.getCampaignUuid()))
                .thenReturn(campaign);

        when(cityRepository.save(city)).thenReturn(city);
        when(cityMapper.toDto(city)).thenReturn(viewDto);

        ViewCityDTO result = cityService.create(createDto);

        assertEquals(viewDto, result);

        verify(cityMapper).toEntity(createDto);
        verify(campaignRepository).getReferenceById(createDto.getCampaignUuid());
        verify(cityRepository).save(city);
        verify(cityMapper).toDto(city);
    }

    @Test
    void update_whenValid_updatesAndReturnsDto() {

        Campaign campaign = new Campaign();
        campaign.setUuid(updateDto.getCampaignUuid());

        when(cityRepository.findById(updateDto.getId()))
                .thenReturn(Optional.of(city));

        when(campaignRepository.getReferenceById(updateDto.getCampaignUuid()))
                .thenReturn(campaign);

        when(cityRepository.save(city)).thenReturn(city);
        when(cityMapper.toDto(city)).thenReturn(viewDto);

        ViewCityDTO result = cityService.update(updateDto);

        assertEquals(viewDto, result);

        verify(cityRepository).findById(updateDto.getId());
        verify(cityMapper).updateCityFromDto(updateDto, city);
        verify(campaignRepository).getReferenceById(updateDto.getCampaignUuid());
        verify(cityRepository).save(city);
        verify(cityMapper).toDto(city);
    }

    @Test
    void update_whenMissing_throwsException() {

        when(cityRepository.findById(updateDto.getId()))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> cityService.update(updateDto)
        );

        verify(cityRepository).findById(updateDto.getId());
    }

    @Test
    void delete_callsRepository() {

        cityService.delete(city.getId());

        verify(cityRepository).deleteById(city.getId());
    }
}
