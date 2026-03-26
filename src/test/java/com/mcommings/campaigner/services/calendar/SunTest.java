package com.mcommings.campaigner.services.calendar;

import com.mcommings.campaigner.modules.calendar.dtos.suns.CreateSunDTO;
import com.mcommings.campaigner.modules.calendar.dtos.suns.UpdateSunDTO;
import com.mcommings.campaigner.modules.calendar.dtos.suns.ViewSunDTO;
import com.mcommings.campaigner.modules.calendar.entities.Sun;
import com.mcommings.campaigner.modules.calendar.mappers.SunMapper;
import com.mcommings.campaigner.modules.calendar.repositories.ISunRepository;
import com.mcommings.campaigner.modules.calendar.services.SunService;
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
public class SunTest {
    @Mock
    private ISunRepository sunRepository;

    @Mock
    private ICampaignRepository campaignRepository;

    @Mock
    private SunMapper sunMapper;

    @InjectMocks
    private SunService sunService;

    private Sun sun;
    private ViewSunDTO viewDto;
    private CreateSunDTO createDto;
    private UpdateSunDTO updateDto;

    @BeforeEach
    void setUp() {
        sun = CalendarTestDataFactory.sun();
        viewDto = CalendarTestDataFactory.viewSunDTO();
        createDto = CalendarTestDataFactory.createSunDTO();
        updateDto = CalendarTestDataFactory.updateSunDTO();
    }

    @Test
    void getAll_returnsMappedDtos() {

        when(sunRepository.findAll()).thenReturn(List.of(sun));
        when(sunMapper.toDto(sun)).thenReturn(viewDto);

        List<ViewSunDTO> result = sunService.getAll();

        assertEquals(1, result.size());
        assertEquals(viewDto, result.get(0));

        verify(sunRepository).findAll();
        verify(sunMapper).toDto(sun);
    }

    @Test
    void getSunsByCampaignUUID_returnsMappedList() {

        when(sunRepository.findByCampaign_Uuid(CalendarTestConstants.CAMPAIGN_UUID))
                .thenReturn(List.of(sun));

        when(sunMapper.toDto(sun))
                .thenReturn(viewDto);

        List<ViewSunDTO> result =
                sunService.getSunsByCampaignUUID(
                        CalendarTestConstants.CAMPAIGN_UUID);

        assertEquals(1, result.size());
        assertEquals(viewDto, result.get(0));

        verify(sunRepository)
                .findByCampaign_Uuid(CalendarTestConstants.CAMPAIGN_UUID);

        verify(sunMapper).toDto(sun);
    }

    @Test
    void getById_whenExists_returnsDto() {

        when(sunRepository.findById(sun.getId()))
                .thenReturn(Optional.of(sun));

        when(sunMapper.toDto(sun))
                .thenReturn(viewDto);

        ViewSunDTO result = sunService.getById(sun.getId());

        assertEquals(viewDto, result);

        verify(sunRepository).findById(sun.getId());
        verify(sunMapper).toDto(sun);
    }

    @Test
    void getById_whenMissing_throwsException() {

        when(sunRepository.findById(sun.getId()))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> sunService.getById(sun.getId())
        );

        verify(sunRepository).findById(sun.getId());
    }

    @Test
    void create_whenValid_savesAndReturnsDto() {

        Campaign campaign = new Campaign();
        campaign.setUuid(createDto.getCampaignUuid());

        when(sunMapper.toEntity(createDto)).thenReturn(sun);
        when(campaignRepository.getReferenceById(createDto.getCampaignUuid()))
                .thenReturn(campaign);

        when(sunRepository.save(sun)).thenReturn(sun);
        when(sunMapper.toDto(sun)).thenReturn(viewDto);

        ViewSunDTO result = sunService.create(createDto);

        assertEquals(viewDto, result);

        verify(sunMapper).toEntity(createDto);
        verify(campaignRepository).getReferenceById(createDto.getCampaignUuid());
        verify(sunRepository).save(sun);
        verify(sunMapper).toDto(sun);
    }

    @Test
    void update_whenValid_updatesAndReturnsDto() {

        Campaign campaign = new Campaign();
        campaign.setUuid(updateDto.getCampaignUuid());

        when(sunRepository.findById(updateDto.getId()))
                .thenReturn(Optional.of(sun));

        when(campaignRepository.getReferenceById(updateDto.getCampaignUuid()))
                .thenReturn(campaign);

        when(sunRepository.save(sun)).thenReturn(sun);
        when(sunMapper.toDto(sun)).thenReturn(viewDto);

        ViewSunDTO result = sunService.update(updateDto);

        assertEquals(viewDto, result);

        verify(sunRepository).findById(updateDto.getId());
        verify(sunMapper).updateSunFromDto(updateDto, sun);
        verify(campaignRepository).getReferenceById(updateDto.getCampaignUuid());
        verify(sunRepository).save(sun);
        verify(sunMapper).toDto(sun);
    }

    @Test
    void update_whenMissing_throwsException() {

        when(sunRepository.findById(updateDto.getId()))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> sunService.update(updateDto)
        );

        verify(sunRepository).findById(updateDto.getId());
    }

    @Test
    void delete_callsRepository() {

        sunService.delete(sun.getId());

        verify(sunRepository).deleteById(sun.getId());
    }
}
