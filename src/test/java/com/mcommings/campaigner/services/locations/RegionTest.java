package com.mcommings.campaigner.services.locations;

import com.mcommings.campaigner.modules.common.entities.Campaign;
import com.mcommings.campaigner.modules.common.repositories.ICampaignRepository;
import com.mcommings.campaigner.modules.locations.dtos.regions.CreateRegionDTO;
import com.mcommings.campaigner.modules.locations.dtos.regions.UpdateRegionDTO;
import com.mcommings.campaigner.modules.locations.dtos.regions.ViewRegionDTO;
import com.mcommings.campaigner.modules.locations.entities.Region;
import com.mcommings.campaigner.modules.locations.mappers.RegionMapper;
import com.mcommings.campaigner.modules.locations.repositories.IRegionRepository;
import com.mcommings.campaigner.modules.locations.services.RegionService;
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
public class RegionTest {
    @Mock
    private IRegionRepository regionRepository;

    @Mock
    private ICampaignRepository campaignRepository;
    
    @Mock
    private RegionMapper regionMapper;

    @InjectMocks
    private RegionService regionService;

    private Region region;
    private ViewRegionDTO viewDto;
    private CreateRegionDTO createDto;
    private UpdateRegionDTO updateDto;

    @BeforeEach
    void setUp() {
        region = LocationsTestDataFactory.region();
        viewDto = LocationsTestDataFactory.viewRegionDTO();
        createDto = LocationsTestDataFactory.createRegionDTO();
        updateDto = LocationsTestDataFactory.updateRegionDTO();
    }

    @Test
    void getAll_returnsMappedDtos() {

        when(regionRepository.findAll()).thenReturn(List.of(region));
        when(regionMapper.toDto(region)).thenReturn(viewDto);

        List<ViewRegionDTO> result = regionService.getAll();

        assertEquals(1, result.size());
        assertEquals(viewDto, result.get(0));

        verify(regionRepository).findAll();
        verify(regionMapper).toDto(region);
    }

    @Test
    void getRegionsByCampaignUUID_returnsMappedList() {

        when(regionRepository.findByCampaign_Uuid(LocationsTestConstants.CAMPAIGN_UUID))
                .thenReturn(List.of(region));

        when(regionMapper.toDto(region))
                .thenReturn(viewDto);

        List<ViewRegionDTO> result =
                regionService.getRegionsByCampaignUUID(
                        LocationsTestConstants.CAMPAIGN_UUID);

        assertEquals(1, result.size());
        assertEquals(viewDto, result.get(0));

        verify(regionRepository)
                .findByCampaign_Uuid(LocationsTestConstants.CAMPAIGN_UUID);

        verify(regionMapper).toDto(region);
    }

    @Test
    void getRegionsByClimateId_returnsMappedList() {
        when(regionRepository.findByClimate_Id(LocationsTestConstants.CLIMATE_ID))
                .thenReturn(List.of(region));

        when(regionMapper.toDto(region))
                .thenReturn(viewDto);

        List<ViewRegionDTO> result =
                regionService.getRegionsByClimateId(
                        LocationsTestConstants.CLIMATE_ID);

        assertEquals(1, result.size());
        assertEquals(viewDto, result.get(0));

        verify(regionRepository)
                .findByClimate_Id((LocationsTestConstants.CLIMATE_ID));

        verify(regionMapper).toDto(region);
    }

    @Test
    void getRegionsByCountryId_returnsMappedList() {
        when(regionRepository.findByCountry_Id(LocationsTestConstants.COUNTRY_ID))
                .thenReturn(List.of(region));

        when(regionMapper.toDto(region))
                .thenReturn(viewDto);

        List<ViewRegionDTO> result =
                regionService.getRegionsByCountryId(
                        LocationsTestConstants.COUNTRY_ID);

        assertEquals(1, result.size());
        assertEquals(viewDto, result.get(0));

        verify(regionRepository)
                .findByCountry_Id((LocationsTestConstants.COUNTRY_ID));

        verify(regionMapper).toDto(region);
    }

    @Test
    void getById_whenExists_returnsDto() {

        when(regionRepository.findById(region.getId()))
                .thenReturn(Optional.of(region));

        when(regionMapper.toDto(region))
                .thenReturn(viewDto);

        ViewRegionDTO result = regionService.getById(region.getId());

        assertEquals(viewDto, result);

        verify(regionRepository).findById(region.getId());
        verify(regionMapper).toDto(region);
    }

    @Test
    void getById_whenMissing_throwsException() {

        when(regionRepository.findById(region.getId()))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> regionService.getById(region.getId())
        );

        verify(regionRepository).findById(region.getId());
    }

    @Test
    void create_whenValid_savesAndReturnsDto() {

        Campaign campaign = new Campaign();
        campaign.setUuid(createDto.getCampaignUuid());

        when(regionMapper.toEntity(createDto)).thenReturn(region);
        when(campaignRepository.getReferenceById(createDto.getCampaignUuid()))
                .thenReturn(campaign);

        when(regionRepository.save(region)).thenReturn(region);
        when(regionMapper.toDto(region)).thenReturn(viewDto);

        ViewRegionDTO result = regionService.create(createDto);

        assertEquals(viewDto, result);

        verify(regionMapper).toEntity(createDto);
        verify(campaignRepository).getReferenceById(createDto.getCampaignUuid());
        verify(regionRepository).save(region);
        verify(regionMapper).toDto(region);
    }

    @Test
    void update_whenValid_updatesAndReturnsDto() {

        Campaign campaign = new Campaign();
        campaign.setUuid(updateDto.getCampaignUuid());

        when(regionRepository.findById(updateDto.getId()))
                .thenReturn(Optional.of(region));

        when(campaignRepository.getReferenceById(updateDto.getCampaignUuid()))
                .thenReturn(campaign);

        when(regionRepository.save(region)).thenReturn(region);
        when(regionMapper.toDto(region)).thenReturn(viewDto);

        ViewRegionDTO result = regionService.update(updateDto);

        assertEquals(viewDto, result);

        verify(regionRepository).findById(updateDto.getId());
        verify(regionMapper).updateRegionFromDto(updateDto, region);
        verify(campaignRepository).getReferenceById(updateDto.getCampaignUuid());
        verify(regionRepository).save(region);
        verify(regionMapper).toDto(region);
    }

    @Test
    void update_whenMissing_throwsException() {

        when(regionRepository.findById(updateDto.getId()))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> regionService.update(updateDto)
        );

        verify(regionRepository).findById(updateDto.getId());
    }

    @Test
    void delete_callsRepository() {

        regionService.delete(region.getId());

        verify(regionRepository).deleteById(region.getId());
    }
}
