package com.mcommings.campaigner.services.calendar;

import com.mcommings.campaigner.modules.calendar.dtos.moons.CreateMoonDTO;
import com.mcommings.campaigner.modules.calendar.dtos.moons.UpdateMoonDTO;
import com.mcommings.campaigner.modules.calendar.dtos.moons.ViewMoonDTO;
import com.mcommings.campaigner.modules.calendar.entities.Moon;
import com.mcommings.campaigner.modules.calendar.mappers.MoonMapper;
import com.mcommings.campaigner.modules.calendar.repositories.IMoonRepository;
import com.mcommings.campaigner.modules.calendar.services.MoonService;
import com.mcommings.campaigner.modules.common.entities.Campaign;
import com.mcommings.campaigner.modules.common.repositories.ICampaignRepository;
import com.mcommings.campaigner.setup.calendar.factories.CalendarTestDataFactory;
import com.mcommings.campaigner.setup.calendar.fixtures.CalendarTestConstants;
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
public class MoonTest {
    @Mock
    private IMoonRepository moonRepository;

    @Mock
    private ICampaignRepository campaignRepository;
    
    @Mock
    private MoonMapper moonMapper;

    @InjectMocks
    private MoonService moonService;

    private Moon moon;
    private ViewMoonDTO viewDto;
    private CreateMoonDTO createDto;
    private UpdateMoonDTO updateDto;

    @BeforeEach
    void setUp() {
        moon = CalendarTestDataFactory.moon();
        viewDto = CalendarTestDataFactory.viewMoonDTO();
        createDto = CalendarTestDataFactory.createMoonDTO();
        updateDto = CalendarTestDataFactory.updateMoonDTO();
    }

    @Test
    void getAll_returnsMappedDtos() {

        when(moonRepository.findAll()).thenReturn(List.of(moon));
        when(moonMapper.toDto(moon)).thenReturn(viewDto);

        List<ViewMoonDTO> result = moonService.getAll();

        assertEquals(1, result.size());
        assertEquals(viewDto, result.get(0));

        verify(moonRepository).findAll();
        verify(moonMapper).toDto(moon);
    }

    @Test
    void getMoonsByCampaignUUID_returnsMappedList() {

        when(moonRepository.findByCampaign_Uuid(CalendarTestConstants.CAMPAIGN_UUID))
                .thenReturn(List.of(moon));

        when(moonMapper.toDto(moon))
                .thenReturn(viewDto);

        List<ViewMoonDTO> result =
                moonService.getMoonsByCampaignUUID(
                        CalendarTestConstants.CAMPAIGN_UUID);

        assertEquals(1, result.size());
        assertEquals(viewDto, result.get(0));

        verify(moonRepository)
                .findByCampaign_Uuid(CalendarTestConstants.CAMPAIGN_UUID);

        verify(moonMapper).toDto(moon);
    }

    @Test
    void getById_whenExists_returnsDto() {

        when(moonRepository.findById(moon.getId()))
                .thenReturn(Optional.of(moon));

        when(moonMapper.toDto(moon))
                .thenReturn(viewDto);

        ViewMoonDTO result = moonService.getById(moon.getId());

        assertEquals(viewDto, result);

        verify(moonRepository).findById(moon.getId());
        verify(moonMapper).toDto(moon);
    }

    @Test
    void getById_whenMissing_throwsException() {

        when(moonRepository.findById(moon.getId()))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> moonService.getById(moon.getId())
        );

        verify(moonRepository).findById(moon.getId());
    }

    @Test
    void create_whenValid_savesAndReturnsDto() {

        Campaign campaign = new Campaign();
        campaign.setUuid(createDto.getCampaignUuid());

        when(moonMapper.toEntity(createDto)).thenReturn(moon);
        when(campaignRepository.getReferenceById(createDto.getCampaignUuid()))
                .thenReturn(campaign);

        when(moonRepository.save(moon)).thenReturn(moon);
        when(moonMapper.toDto(moon)).thenReturn(viewDto);

        ViewMoonDTO result = moonService.create(createDto);

        assertEquals(viewDto, result);

        verify(moonMapper).toEntity(createDto);
        verify(campaignRepository).getReferenceById(createDto.getCampaignUuid());
        verify(moonRepository).save(moon);
        verify(moonMapper).toDto(moon);
    }

    @Test
    void update_whenValid_updatesAndReturnsDto() {

        Campaign campaign = new Campaign();
        campaign.setUuid(updateDto.getCampaignUuid());

        when(moonRepository.findById(updateDto.getId()))
                .thenReturn(Optional.of(moon));

        when(campaignRepository.getReferenceById(updateDto.getCampaignUuid()))
                .thenReturn(campaign);

        when(moonRepository.save(moon)).thenReturn(moon);
        when(moonMapper.toDto(moon)).thenReturn(viewDto);

        ViewMoonDTO result = moonService.update(updateDto);

        assertEquals(viewDto, result);

        verify(moonRepository).findById(updateDto.getId());
        verify(moonMapper).updateMoonFromDto(updateDto, moon);
        verify(campaignRepository).getReferenceById(updateDto.getCampaignUuid());
        verify(moonRepository).save(moon);
        verify(moonMapper).toDto(moon);
    }

    @Test
    void update_whenMissing_throwsException() {

        when(moonRepository.findById(updateDto.getId()))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> moonService.update(updateDto)
        );

        verify(moonRepository).findById(updateDto.getId());
    }

    @Test
    void delete_callsRepository() {

        moonService.delete(moon.getId());

        verify(moonRepository).deleteById(moon.getId());
    }
}
