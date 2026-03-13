package com.mcommings.campaigner.services.locations;

import com.mcommings.campaigner.modules.common.entities.Campaign;
import com.mcommings.campaigner.modules.common.repositories.ICampaignRepository;
import com.mcommings.campaigner.modules.locations.dtos.landmarks.CreateLandmarkDTO;
import com.mcommings.campaigner.modules.locations.dtos.landmarks.UpdateLandmarkDTO;
import com.mcommings.campaigner.modules.locations.dtos.landmarks.ViewLandmarkDTO;
import com.mcommings.campaigner.modules.locations.entities.Landmark;
import com.mcommings.campaigner.modules.locations.mappers.LandmarkMapper;
import com.mcommings.campaigner.modules.locations.repositories.ILandmarkRepository;
import com.mcommings.campaigner.modules.locations.services.LandmarkService;
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
public class LandmarkTest {

    @Mock
    private ILandmarkRepository landmarkRepository;

    @Mock
    private ICampaignRepository campaignRepository;
    
    @Mock
    private LandmarkMapper landmarkMapper;

    @InjectMocks
    private LandmarkService landmarkService;

    private Landmark landmark;
    private ViewLandmarkDTO viewDto;
    private CreateLandmarkDTO createDto;
    private UpdateLandmarkDTO updateDto;

    @BeforeEach
    void setUp() {
        landmark = LocationsTestDataFactory.landmark();
        viewDto = LocationsTestDataFactory.viewLandmarkDTO();
        createDto = LocationsTestDataFactory.createLandmarkDTO();
        updateDto = LocationsTestDataFactory.updateLandmarkDTO();
    }

    @Test
    void getAll_returnsMappedDtos() {

        when(landmarkRepository.findAll()).thenReturn(List.of(landmark));
        when(landmarkMapper.toDto(landmark)).thenReturn(viewDto);

        List<ViewLandmarkDTO> result = landmarkService.getAll();

        assertEquals(1, result.size());
        assertEquals(viewDto, result.get(0));

        verify(landmarkRepository).findAll();
        verify(landmarkMapper).toDto(landmark);
    }

    @Test
    void getLandmarksByCampaignUUID_returnsMappedList() {

        when(landmarkRepository.findByCampaign_Uuid(LocationsTestConstants.CAMPAIGN_UUID))
                .thenReturn(List.of(landmark));

        when(landmarkMapper.toDto(landmark))
                .thenReturn(viewDto);

        List<ViewLandmarkDTO> result =
                landmarkService.getLandmarksByCampaignUUID(
                        LocationsTestConstants.CAMPAIGN_UUID);

        assertEquals(1, result.size());
        assertEquals(viewDto, result.get(0));

        verify(landmarkRepository)
                .findByCampaign_Uuid(LocationsTestConstants.CAMPAIGN_UUID);

        verify(landmarkMapper).toDto(landmark);
    }

    @Test
    void getLandmarksByRegionId_returnsMappedList() {

        when(landmarkRepository.findByRegion_Id(LocationsTestConstants.REGION_ID))
                .thenReturn(List.of(landmark));

        when(landmarkMapper.toDto(landmark))
                .thenReturn(viewDto);

        List<ViewLandmarkDTO> result =
                landmarkService.getLandmarksByRegionId(
                        LocationsTestConstants.REGION_ID);

        assertEquals(1, result.size());
        assertEquals(viewDto, result.get(0));

        verify(landmarkRepository)
                .findByRegion_Id((LocationsTestConstants.REGION_ID));

        verify(landmarkMapper).toDto(landmark);
    }


    @Test
    void getById_whenExists_returnsDto() {

        when(landmarkRepository.findById(landmark.getId()))
                .thenReturn(Optional.of(landmark));

        when(landmarkMapper.toDto(landmark))
                .thenReturn(viewDto);

        ViewLandmarkDTO result = landmarkService.getById(landmark.getId());

        assertEquals(viewDto, result);

        verify(landmarkRepository).findById(landmark.getId());
        verify(landmarkMapper).toDto(landmark);
    }

    @Test
    void getById_whenMissing_throwsException() {

        when(landmarkRepository.findById(landmark.getId()))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> landmarkService.getById(landmark.getId())
        );

        verify(landmarkRepository).findById(landmark.getId());
    }

    @Test
    void create_whenValid_savesAndReturnsDto() {

        Campaign campaign = new Campaign();
        campaign.setUuid(createDto.getCampaignUuid());

        when(landmarkMapper.toEntity(createDto)).thenReturn(landmark);
        when(campaignRepository.getReferenceById(createDto.getCampaignUuid()))
                .thenReturn(campaign);

        when(landmarkRepository.save(landmark)).thenReturn(landmark);
        when(landmarkMapper.toDto(landmark)).thenReturn(viewDto);

        ViewLandmarkDTO result = landmarkService.create(createDto);

        assertEquals(viewDto, result);

        verify(landmarkMapper).toEntity(createDto);
        verify(campaignRepository).getReferenceById(createDto.getCampaignUuid());
        verify(landmarkRepository).save(landmark);
        verify(landmarkMapper).toDto(landmark);
    }

    @Test
    void update_whenValid_updatesAndReturnsDto() {

        Campaign campaign = new Campaign();
        campaign.setUuid(updateDto.getCampaignUuid());

        when(landmarkRepository.findById(updateDto.getId()))
                .thenReturn(Optional.of(landmark));

        when(campaignRepository.getReferenceById(updateDto.getCampaignUuid()))
                .thenReturn(campaign);

        when(landmarkRepository.save(landmark)).thenReturn(landmark);
        when(landmarkMapper.toDto(landmark)).thenReturn(viewDto);

        ViewLandmarkDTO result = landmarkService.update(updateDto);

        assertEquals(viewDto, result);

        verify(landmarkRepository).findById(updateDto.getId());
        verify(landmarkMapper).updateLandmarkFromDto(updateDto, landmark);
        verify(campaignRepository).getReferenceById(updateDto.getCampaignUuid());
        verify(landmarkRepository).save(landmark);
        verify(landmarkMapper).toDto(landmark);
    }

    @Test
    void update_whenMissing_throwsException() {

        when(landmarkRepository.findById(updateDto.getId()))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> landmarkService.update(updateDto)
        );

        verify(landmarkRepository).findById(updateDto.getId());
    }

    @Test
    void delete_callsRepository() {

        landmarkService.delete(landmark.getId());

        verify(landmarkRepository).deleteById(landmark.getId());
    }
}
