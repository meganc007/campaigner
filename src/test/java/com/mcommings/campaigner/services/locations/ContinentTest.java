package com.mcommings.campaigner.services.locations;

import com.mcommings.campaigner.modules.common.entities.Campaign;
import com.mcommings.campaigner.modules.common.repositories.ICampaignRepository;
import com.mcommings.campaigner.modules.locations.dtos.continents.CreateContinentDTO;
import com.mcommings.campaigner.modules.locations.dtos.continents.UpdateContinentDTO;
import com.mcommings.campaigner.modules.locations.dtos.continents.ViewContinentDTO;
import com.mcommings.campaigner.modules.locations.entities.Continent;
import com.mcommings.campaigner.modules.locations.mappers.ContinentMapper;
import com.mcommings.campaigner.modules.locations.repositories.IContinentRepository;
import com.mcommings.campaigner.modules.locations.services.ContinentService;
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
public class ContinentTest {

    @Mock
    private IContinentRepository continentRepository;

    @Mock
    private ICampaignRepository campaignRepository;

    @Mock
    private ContinentMapper continentMapper;

    @InjectMocks
    private ContinentService continentService;

    private Continent continent;
    private ViewContinentDTO viewDto;
    private CreateContinentDTO createDto;
    private UpdateContinentDTO updateDto;

    @BeforeEach
    void setup() {
        continent = LocationsTestDataFactory.continent();
        viewDto = LocationsTestDataFactory.viewContinentDTO();
        createDto = LocationsTestDataFactory.createContinentDTO();
        updateDto = LocationsTestDataFactory.updateContinentDTO();
    }

    @Test
    void getAll_returnsMappedDtos() {

        when(continentRepository.findAll()).thenReturn(List.of(continent));
        when(continentMapper.toDto(continent)).thenReturn(viewDto);

        List<ViewContinentDTO> result = continentService.getAll();

        assertEquals(1, result.size());
        assertEquals(viewDto, result.get(0));

        verify(continentRepository).findAll();
        verify(continentMapper).toDto(continent);
    }

    @Test
    void getContinentsByCampaignUUID_returnsMappedList() {

        when(continentRepository.findByCampaign_Uuid(LocationsTestConstants.CAMPAIGN_UUID))
                .thenReturn(List.of(continent));

        when(continentMapper.toDto(continent))
                .thenReturn(viewDto);

        List<ViewContinentDTO> result =
                continentService.getContinentsByCampaignUUID(
                        LocationsTestConstants.CAMPAIGN_UUID);

        assertEquals(1, result.size());
        assertEquals(viewDto, result.get(0));

        verify(continentRepository)
                .findByCampaign_Uuid(LocationsTestConstants.CAMPAIGN_UUID);

        verify(continentMapper).toDto(continent);
    }

    @Test
    void getById_whenExists_returnsDto() {

        when(continentRepository.findById(continent.getId()))
                .thenReturn(Optional.of(continent));

        when(continentMapper.toDto(continent))
                .thenReturn(viewDto);

        ViewContinentDTO result = continentService.getById(continent.getId());

        assertEquals(viewDto, result);

        verify(continentRepository).findById(continent.getId());
        verify(continentMapper).toDto(continent);
    }

    @Test
    void getById_whenMissing_throwsException() {

        when(continentRepository.findById(continent.getId()))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> continentService.getById(continent.getId())
        );

        verify(continentRepository).findById(continent.getId());
    }

    @Test
    void create_whenValid_savesAndReturnsDto() {

        Campaign campaign = new Campaign();
        campaign.setUuid(createDto.getCampaignUuid());

        when(continentMapper.toEntity(createDto)).thenReturn(continent);
        when(campaignRepository.getReferenceById(createDto.getCampaignUuid()))
                .thenReturn(campaign);

        when(continentRepository.save(continent)).thenReturn(continent);
        when(continentMapper.toDto(continent)).thenReturn(viewDto);

        ViewContinentDTO result = continentService.create(createDto);

        assertEquals(viewDto, result);

        verify(continentMapper).toEntity(createDto);
        verify(campaignRepository).getReferenceById(createDto.getCampaignUuid());
        verify(continentRepository).save(continent);
        verify(continentMapper).toDto(continent);
    }

    @Test
    void update_whenValid_updatesAndReturnsDto() {

        Campaign campaign = new Campaign();
        campaign.setUuid(updateDto.getCampaignUuid());

        when(continentRepository.findById(updateDto.getId()))
                .thenReturn(Optional.of(continent));

        when(campaignRepository.getReferenceById(updateDto.getCampaignUuid()))
                .thenReturn(campaign);

        when(continentRepository.save(continent)).thenReturn(continent);
        when(continentMapper.toDto(continent)).thenReturn(viewDto);

        ViewContinentDTO result = continentService.update(updateDto);

        assertEquals(viewDto, result);

        verify(continentRepository).findById(updateDto.getId());
        verify(continentMapper).updateContinentFromDto(updateDto, continent);
        verify(campaignRepository).getReferenceById(updateDto.getCampaignUuid());
        verify(continentRepository).save(continent);
        verify(continentMapper).toDto(continent);
    }

    @Test
    void update_whenMissing_throwsException() {

        when(continentRepository.findById(updateDto.getId()))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> continentService.update(updateDto)
        );

        verify(continentRepository).findById(updateDto.getId());
    }

    @Test
    void delete_callsRepository() {

        continentService.delete(continent.getId());

        verify(continentRepository).deleteById(continent.getId());
    }
}