package com.mcommings.campaigner.services.calendar;

import com.mcommings.campaigner.modules.calendar.dtos.celestial_events.CreateCelestialEventDTO;
import com.mcommings.campaigner.modules.calendar.dtos.celestial_events.UpdateCelestialEventDTO;
import com.mcommings.campaigner.modules.calendar.dtos.celestial_events.ViewCelestialEventDTO;
import com.mcommings.campaigner.modules.calendar.entities.CelestialEvent;
import com.mcommings.campaigner.modules.calendar.mappers.CelestialEventMapper;
import com.mcommings.campaigner.modules.calendar.repositories.ICelestialEventRepository;
import com.mcommings.campaigner.modules.calendar.services.CelestialEventService;
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
public class CelestialEventTest {

    @Mock
    private ICelestialEventRepository celestialEventRepository;

    @Mock
    private ICampaignRepository campaignRepository;

    @Mock
    private CelestialEventMapper celestialEventMapper;

    @InjectMocks
    private CelestialEventService celestialEventService;

    private CelestialEvent celestialEvent;
    private ViewCelestialEventDTO viewDto;
    private CreateCelestialEventDTO createDto;
    private UpdateCelestialEventDTO updateDto;

    @BeforeEach
    void setUp() {
        celestialEvent = CalendarTestDataFactory.celestialEvent();
        viewDto = CalendarTestDataFactory.viewCelestialEventDTO();
        createDto = CalendarTestDataFactory.createCelestialEventDTO();
        updateDto = CalendarTestDataFactory.updateCelestialEventDTO();
    }

    @Test
    void getAll_returnsMappedDtos() {

        when(celestialEventRepository.findAll()).thenReturn(List.of(celestialEvent));
        when(celestialEventMapper.toDto(celestialEvent)).thenReturn(viewDto);

        List<ViewCelestialEventDTO> result = celestialEventService.getAll();

        assertEquals(1, result.size());
        assertEquals(viewDto, result.get(0));

        verify(celestialEventRepository).findAll();
        verify(celestialEventMapper).toDto(celestialEvent);
    }

    @Test
    void getCelestialEventsByCampaignUUID_returnsMappedList() {

        when(celestialEventRepository.findByCampaign_Uuid(CalendarTestConstants.CAMPAIGN_UUID))
                .thenReturn(List.of(celestialEvent));

        when(celestialEventMapper.toDto(celestialEvent))
                .thenReturn(viewDto);

        List<ViewCelestialEventDTO> result =
                celestialEventService.getCelestialEventsByCampaignUUID(
                        CalendarTestConstants.CAMPAIGN_UUID);

        assertEquals(1, result.size());
        assertEquals(viewDto, result.get(0));

        verify(celestialEventRepository)
                .findByCampaign_Uuid(CalendarTestConstants.CAMPAIGN_UUID);

        verify(celestialEventMapper).toDto(celestialEvent);
    }

    @Test
    void getCelestialEventsByMoonId_returnsMappedList() {

        when(celestialEventRepository.findByMoon_Id(CalendarTestConstants.MOON_ID))
                .thenReturn(List.of(celestialEvent));

        when(celestialEventMapper.toDto(celestialEvent))
                .thenReturn(viewDto);

        List<ViewCelestialEventDTO> result =
                celestialEventService.getCelestialEventsByMoonId(
                        CalendarTestConstants.MOON_ID);

        assertEquals(1, result.size());
        assertEquals(viewDto, result.get(0));

        verify(celestialEventRepository)
                .findByMoon_Id((CalendarTestConstants.MOON_ID));

        verify(celestialEventMapper).toDto(celestialEvent);
    }

    @Test
    void getCelestialEventsBySunId_returnsMappedList() {

        when(celestialEventRepository.findBySun_Id(CalendarTestConstants.SUN_ID))
                .thenReturn(List.of(celestialEvent));

        when(celestialEventMapper.toDto(celestialEvent))
                .thenReturn(viewDto);

        List<ViewCelestialEventDTO> result =
                celestialEventService.getCelestialEventsBySunId(
                        CalendarTestConstants.SUN_ID);

        assertEquals(1, result.size());
        assertEquals(viewDto, result.get(0));

        verify(celestialEventRepository)
                .findBySun_Id((CalendarTestConstants.SUN_ID));

        verify(celestialEventMapper).toDto(celestialEvent);
    }

    @Test
    void getCelestialEventsByMonthId_returnsMappedList() {

        when(celestialEventRepository.findByMonth_Id(CalendarTestConstants.MONTH_ID))
                .thenReturn(List.of(celestialEvent));

        when(celestialEventMapper.toDto(celestialEvent))
                .thenReturn(viewDto);

        List<ViewCelestialEventDTO> result =
                celestialEventService.getCelestialEventsByMonthId(
                        CalendarTestConstants.MONTH_ID);

        assertEquals(1, result.size());
        assertEquals(viewDto, result.get(0));

        verify(celestialEventRepository)
                .findByMonth_Id((CalendarTestConstants.MONTH_ID));

        verify(celestialEventMapper).toDto(celestialEvent);
    }

    @Test
    void getCelestialEventsByYearId_returnsMappedList() {

        when(celestialEventRepository.findByYear_Id(CalendarTestConstants.CELESTIAL_EVENT_YEAR))
                .thenReturn(List.of(celestialEvent));

        when(celestialEventMapper.toDto(celestialEvent))
                .thenReturn(viewDto);

        List<ViewCelestialEventDTO> result =
                celestialEventService.getCelestialEventsByYearId(
                        CalendarTestConstants.CELESTIAL_EVENT_YEAR);

        assertEquals(1, result.size());
        assertEquals(viewDto, result.get(0));

        verify(celestialEventRepository)
                .findByYear_Id((CalendarTestConstants.CELESTIAL_EVENT_YEAR));

        verify(celestialEventMapper).toDto(celestialEvent);
    }

    @Test
    void getById_whenExists_returnsDto() {

        when(celestialEventRepository.findById(celestialEvent.getId()))
                .thenReturn(Optional.of(celestialEvent));

        when(celestialEventMapper.toDto(celestialEvent))
                .thenReturn(viewDto);

        ViewCelestialEventDTO result = celestialEventService.getById(celestialEvent.getId());

        assertEquals(viewDto, result);

        verify(celestialEventRepository).findById(celestialEvent.getId());
        verify(celestialEventMapper).toDto(celestialEvent);
    }

    @Test
    void getById_whenMissing_throwsException() {

        when(celestialEventRepository.findById(celestialEvent.getId()))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> celestialEventService.getById(celestialEvent.getId())
        );

        verify(celestialEventRepository).findById(celestialEvent.getId());
    }

    @Test
    void create_whenValid_savesAndReturnsDto() {

        Campaign campaign = new Campaign();
        campaign.setUuid(createDto.getCampaignUuid());

        when(celestialEventMapper.toEntity(createDto)).thenReturn(celestialEvent);
        when(campaignRepository.getReferenceById(createDto.getCampaignUuid()))
                .thenReturn(campaign);

        when(celestialEventRepository.save(celestialEvent)).thenReturn(celestialEvent);
        when(celestialEventMapper.toDto(celestialEvent)).thenReturn(viewDto);

        ViewCelestialEventDTO result = celestialEventService.create(createDto);

        assertEquals(viewDto, result);

        verify(celestialEventMapper).toEntity(createDto);
        verify(campaignRepository).getReferenceById(createDto.getCampaignUuid());
        verify(celestialEventRepository).save(celestialEvent);
        verify(celestialEventMapper).toDto(celestialEvent);
    }

    @Test
    void update_whenValid_updatesAndReturnsDto() {

        Campaign campaign = new Campaign();
        campaign.setUuid(updateDto.getCampaignUuid());

        when(celestialEventRepository.findById(updateDto.getId()))
                .thenReturn(Optional.of(celestialEvent));

        when(campaignRepository.getReferenceById(updateDto.getCampaignUuid()))
                .thenReturn(campaign);

        when(celestialEventRepository.save(celestialEvent)).thenReturn(celestialEvent);
        when(celestialEventMapper.toDto(celestialEvent)).thenReturn(viewDto);

        ViewCelestialEventDTO result = celestialEventService.update(updateDto);

        assertEquals(viewDto, result);

        verify(celestialEventRepository).findById(updateDto.getId());
        verify(celestialEventMapper).updateCelestialEventFromDto(updateDto, celestialEvent);
        verify(campaignRepository).getReferenceById(updateDto.getCampaignUuid());
        verify(celestialEventRepository).save(celestialEvent);
        verify(celestialEventMapper).toDto(celestialEvent);
    }

    @Test
    void update_whenMissing_throwsException() {

        when(celestialEventRepository.findById(updateDto.getId()))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> celestialEventService.update(updateDto)
        );

        verify(celestialEventRepository).findById(updateDto.getId());
    }

    @Test
    void delete_callsRepository() {

        celestialEventService.delete(celestialEvent.getId());

        verify(celestialEventRepository).deleteById(celestialEvent.getId());
    }
}
