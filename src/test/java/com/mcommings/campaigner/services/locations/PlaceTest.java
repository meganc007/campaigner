package com.mcommings.campaigner.services.locations;

import com.mcommings.campaigner.modules.common.entities.Campaign;
import com.mcommings.campaigner.modules.common.repositories.ICampaignRepository;
import com.mcommings.campaigner.modules.locations.dtos.places.CreatePlaceDTO;
import com.mcommings.campaigner.modules.locations.dtos.places.UpdatePlaceDTO;
import com.mcommings.campaigner.modules.locations.dtos.places.ViewPlaceDTO;
import com.mcommings.campaigner.modules.locations.entities.Place;
import com.mcommings.campaigner.modules.locations.mappers.PlaceMapper;
import com.mcommings.campaigner.modules.locations.repositories.IPlaceRepository;
import com.mcommings.campaigner.modules.locations.services.PlaceService;
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
public class PlaceTest {

    @Mock
    private IPlaceRepository placeRepository;

    @Mock
    private ICampaignRepository campaignRepository;
    
    @Mock
    private PlaceMapper placeMapper;

    @InjectMocks
    private PlaceService placeService;

    private Place place;
    private ViewPlaceDTO viewDto;
    private CreatePlaceDTO createDto;
    private UpdatePlaceDTO updateDto;

    @BeforeEach
    void setUp() {
        place = LocationsTestDataFactory.place();
        viewDto = LocationsTestDataFactory.viewPlaceDTO();
        createDto = LocationsTestDataFactory.createPlaceDTO();
        updateDto = LocationsTestDataFactory.updatePlaceDTO();
    }

    @Test
    void getAll_returnsMappedDtos() {

        when(placeRepository.findAll()).thenReturn(List.of(place));
        when(placeMapper.toDto(place)).thenReturn(viewDto);

        List<ViewPlaceDTO> result = placeService.getAll();

        assertEquals(1, result.size());
        assertEquals(viewDto, result.get(0));

        verify(placeRepository).findAll();
        verify(placeMapper).toDto(place);
    }

    @Test
    void getPlacesByCampaignUUID_returnsMappedList() {

        when(placeRepository.findByCampaign_Uuid(LocationsTestConstants.CAMPAIGN_UUID))
                .thenReturn(List.of(place));

        when(placeMapper.toDto(place))
                .thenReturn(viewDto);

        List<ViewPlaceDTO> result =
                placeService.getPlacesByCampaignUUID(
                        LocationsTestConstants.CAMPAIGN_UUID);

        assertEquals(1, result.size());
        assertEquals(viewDto, result.get(0));

        verify(placeRepository)
                .findByCampaign_Uuid(LocationsTestConstants.CAMPAIGN_UUID);

        verify(placeMapper).toDto(place);
    }

    @Test
    void getPlacesByPlaceTypeId_returnsMappedList() {
        when(placeRepository.findByPlaceType_Id(LocationsTestConstants.PLACETYPE_ID))
                .thenReturn(List.of(place));

        when(placeMapper.toDto(place))
                .thenReturn(viewDto);

        List<ViewPlaceDTO> result =
                placeService.getPlacesByPlaceTypeId(
                        LocationsTestConstants.PLACETYPE_ID);

        assertEquals(1, result.size());
        assertEquals(viewDto, result.get(0));

        verify(placeRepository)
                .findByPlaceType_Id((LocationsTestConstants.PLACETYPE_ID));

        verify(placeMapper).toDto(place);
    }

    @Test
    void getPlacesByTerrainId_returnsMappedList() {
        when(placeRepository.findByTerrain_Id(LocationsTestConstants.TERRAIN_ID))
                .thenReturn(List.of(place));

        when(placeMapper.toDto(place))
                .thenReturn(viewDto);

        List<ViewPlaceDTO> result =
                placeService.getPlacesByTerrainId(
                        LocationsTestConstants.TERRAIN_ID);

        assertEquals(1, result.size());
        assertEquals(viewDto, result.get(0));

        verify(placeRepository)
                .findByTerrain_Id((LocationsTestConstants.TERRAIN_ID));

        verify(placeMapper).toDto(place);
    }

    @Test
    void getPlacesByCountryId_returnsMappedList() {
        when(placeRepository.findByCountry_Id(LocationsTestConstants.COUNTRY_ID))
                .thenReturn(List.of(place));

        when(placeMapper.toDto(place))
                .thenReturn(viewDto);

        List<ViewPlaceDTO> result =
                placeService.getPlacesByCountryId(
                        LocationsTestConstants.COUNTRY_ID);

        assertEquals(1, result.size());
        assertEquals(viewDto, result.get(0));

        verify(placeRepository)
                .findByCountry_Id((LocationsTestConstants.COUNTRY_ID));

        verify(placeMapper).toDto(place);
    }

    @Test
    void getPlacesByCityId_returnsMappedList() {
        when(placeRepository.findByCity_Id(LocationsTestConstants.CITY_ID))
                .thenReturn(List.of(place));

        when(placeMapper.toDto(place))
                .thenReturn(viewDto);

        List<ViewPlaceDTO> result =
                placeService.getPlacesByCityId(
                        LocationsTestConstants.CITY_ID);

        assertEquals(1, result.size());
        assertEquals(viewDto, result.get(0));

        verify(placeRepository)
                .findByCity_Id((LocationsTestConstants.CITY_ID));

        verify(placeMapper).toDto(place);
    }

    @Test
    void getPlacesByRegionId_returnsMappedList() {
        when(placeRepository.findByRegion_Id(LocationsTestConstants.REGION_ID))
                .thenReturn(List.of(place));

        when(placeMapper.toDto(place))
                .thenReturn(viewDto);

        List<ViewPlaceDTO> result =
                placeService.getPlacesByRegionId(
                        LocationsTestConstants.REGION_ID);

        assertEquals(1, result.size());
        assertEquals(viewDto, result.get(0));

        verify(placeRepository)
                .findByRegion_Id((LocationsTestConstants.REGION_ID));

        verify(placeMapper).toDto(place);
    }

    @Test
    void getById_whenExists_returnsDto() {

        when(placeRepository.findById(place.getId()))
                .thenReturn(Optional.of(place));

        when(placeMapper.toDto(place))
                .thenReturn(viewDto);

        ViewPlaceDTO result = placeService.getById(place.getId());

        assertEquals(viewDto, result);

        verify(placeRepository).findById(place.getId());
        verify(placeMapper).toDto(place);
    }

    @Test
    void getById_whenMissing_throwsException() {

        when(placeRepository.findById(place.getId()))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> placeService.getById(place.getId())
        );

        verify(placeRepository).findById(place.getId());
    }

    @Test
    void create_whenValid_savesAndReturnsDto() {

        Campaign campaign = new Campaign();
        campaign.setUuid(createDto.getCampaignUuid());

        when(placeMapper.toEntity(createDto)).thenReturn(place);
        when(campaignRepository.getReferenceById(createDto.getCampaignUuid()))
                .thenReturn(campaign);

        when(placeRepository.save(place)).thenReturn(place);
        when(placeMapper.toDto(place)).thenReturn(viewDto);

        ViewPlaceDTO result = placeService.create(createDto);

        assertEquals(viewDto, result);

        verify(placeMapper).toEntity(createDto);
        verify(campaignRepository).getReferenceById(createDto.getCampaignUuid());
        verify(placeRepository).save(place);
        verify(placeMapper).toDto(place);
    }

    @Test
    void update_whenValid_updatesAndReturnsDto() {

        Campaign campaign = new Campaign();
        campaign.setUuid(updateDto.getCampaignUuid());

        when(placeRepository.findById(updateDto.getId()))
                .thenReturn(Optional.of(place));

        when(campaignRepository.getReferenceById(updateDto.getCampaignUuid()))
                .thenReturn(campaign);

        when(placeRepository.save(place)).thenReturn(place);
        when(placeMapper.toDto(place)).thenReturn(viewDto);

        ViewPlaceDTO result = placeService.update(updateDto);

        assertEquals(viewDto, result);

        verify(placeRepository).findById(updateDto.getId());
        verify(placeMapper).updatePlaceFromDto(updateDto, place);
        verify(campaignRepository).getReferenceById(updateDto.getCampaignUuid());
        verify(placeRepository).save(place);
        verify(placeMapper).toDto(place);
    }

    @Test
    void update_whenMissing_throwsException() {

        when(placeRepository.findById(updateDto.getId()))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> placeService.update(updateDto)
        );

        verify(placeRepository).findById(updateDto.getId());
    }

    @Test
    void delete_callsRepository() {

        placeService.delete(place.getId());

        verify(placeRepository).deleteById(place.getId());
    }
}
