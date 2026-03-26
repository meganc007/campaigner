package com.mcommings.campaigner.services.calendar;

import com.mcommings.campaigner.modules.calendar.dtos.events.CreateEventDTO;
import com.mcommings.campaigner.modules.calendar.dtos.events.UpdateEventDTO;
import com.mcommings.campaigner.modules.calendar.dtos.events.ViewEventDTO;
import com.mcommings.campaigner.modules.calendar.entities.Event;
import com.mcommings.campaigner.modules.calendar.mappers.EventMapper;
import com.mcommings.campaigner.modules.calendar.repositories.IEventRepository;
import com.mcommings.campaigner.modules.calendar.services.EventService;
import com.mcommings.campaigner.modules.common.entities.Campaign;
import com.mcommings.campaigner.modules.common.repositories.ICampaignRepository;
import com.mcommings.campaigner.setup.calendar.factories.CalendarTestDataFactory;
import com.mcommings.campaigner.setup.calendar.fixtures.CalendarTestConstants;
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
public class EventTest {

    @Mock
    private IEventRepository eventRepository;

    @Mock
    private ICampaignRepository campaignRepository;

    @Mock
    private EventMapper eventMapper;

    @InjectMocks
    private EventService eventService;

    private Event event;
    private ViewEventDTO viewDto;
    private CreateEventDTO createDto;
    private UpdateEventDTO updateDto;

    @BeforeEach
    void setUp() {
        event = CalendarTestDataFactory.event();
        viewDto = CalendarTestDataFactory.viewEventDTO();
        createDto = CalendarTestDataFactory.createEventDTO();
        updateDto = CalendarTestDataFactory.updateEventDTO();
    }

    @Test
    void getAll_returnsMappedDtos() {

        when(eventRepository.findAll()).thenReturn(List.of(event));
        when(eventMapper.toDto(event)).thenReturn(viewDto);

        List<ViewEventDTO> result = eventService.getAll();

        assertEquals(1, result.size());
        assertEquals(viewDto, result.get(0));

        verify(eventRepository).findAll();
        verify(eventMapper).toDto(event);
    }

    @Test
    void getEventsByCampaignUUID_returnsMappedList() {

        when(eventRepository.findByCampaign_Uuid(CalendarTestConstants.CAMPAIGN_UUID))
                .thenReturn(List.of(event));

        when(eventMapper.toDto(event))
                .thenReturn(viewDto);

        List<ViewEventDTO> result =
                eventService.getEventsByCampaignUUID(
                        CalendarTestConstants.CAMPAIGN_UUID);

        assertEquals(1, result.size());
        assertEquals(viewDto, result.get(0));

        verify(eventRepository)
                .findByCampaign_Uuid(CalendarTestConstants.CAMPAIGN_UUID);

        verify(eventMapper).toDto(event);
    }

    @Test
    void getEventsByMonthId_returnsMappedList() {

        when(eventRepository.findByMonth_Id(CalendarTestConstants.MONTH_ID))
                .thenReturn(List.of(event));

        when(eventMapper.toDto(event))
                .thenReturn(viewDto);

        List<ViewEventDTO> result =
                eventService.getEventsByMonthId(
                        CalendarTestConstants.MONTH_ID);

        assertEquals(1, result.size());
        assertEquals(viewDto, result.get(0));

        verify(eventRepository)
                .findByMonth_Id((CalendarTestConstants.MONTH_ID));

        verify(eventMapper).toDto(event);
    }

    @Test
    void getEventsByWeekId_returnsMappedList() {

        when(eventRepository.findByWeek_Id(CalendarTestConstants.WEEK_ID))
                .thenReturn(List.of(event));

        when(eventMapper.toDto(event))
                .thenReturn(viewDto);

        List<ViewEventDTO> result =
                eventService.getEventsByWeekId(
                        CalendarTestConstants.WEEK_ID);

        assertEquals(1, result.size());
        assertEquals(viewDto, result.get(0));

        verify(eventRepository)
                .findByWeek_Id((CalendarTestConstants.WEEK_ID));

        verify(eventMapper).toDto(event);
    }

    @Test
    void getEventsByDayId_returnsMappedList() {

        when(eventRepository.findByDay_Id(CalendarTestConstants.DAY_ID))
                .thenReturn(List.of(event));

        when(eventMapper.toDto(event))
                .thenReturn(viewDto);

        List<ViewEventDTO> result =
                eventService.getEventsByDayId(
                        CalendarTestConstants.DAY_ID);

        assertEquals(1, result.size());
        assertEquals(viewDto, result.get(0));

        verify(eventRepository)
                .findByDay_Id((CalendarTestConstants.DAY_ID));

        verify(eventMapper).toDto(event);
    }

    @Test
    void getEventsByYear_returnsMappedList() {

        when(eventRepository.findByYear_Id(CalendarTestConstants.EVENT_YEAR))
                .thenReturn(List.of(event));

        when(eventMapper.toDto(event))
                .thenReturn(viewDto);

        List<ViewEventDTO> result =
                eventService.getEventsByYear(
                        CalendarTestConstants.EVENT_YEAR);

        assertEquals(1, result.size());
        assertEquals(viewDto, result.get(0));

        verify(eventRepository)
                .findByYear_Id((CalendarTestConstants.EVENT_YEAR));

        verify(eventMapper).toDto(event);
    }

    @Test
    void getEventsByContinentId_returnsMappedList() {

        when(eventRepository.findByContinent_Id(LocationsTestConstants.CONTINENT_ID))
                .thenReturn(List.of(event));

        when(eventMapper.toDto(event))
                .thenReturn(viewDto);

        List<ViewEventDTO> result =
                eventService.getEventsByContinentId(
                        LocationsTestConstants.CONTINENT_ID);

        assertEquals(1, result.size());
        assertEquals(viewDto, result.get(0));

        verify(eventRepository)
                .findByContinent_Id((LocationsTestConstants.CONTINENT_ID));

        verify(eventMapper).toDto(event);
    }

    @Test
    void getEventsByCountryId_returnsMappedList() {

        when(eventRepository.findByCountry_Id(LocationsTestConstants.COUNTRY_ID))
                .thenReturn(List.of(event));

        when(eventMapper.toDto(event))
                .thenReturn(viewDto);

        List<ViewEventDTO> result =
                eventService.getEventsByCountryId(
                        LocationsTestConstants.COUNTRY_ID);

        assertEquals(1, result.size());
        assertEquals(viewDto, result.get(0));

        verify(eventRepository)
                .findByCountry_Id((LocationsTestConstants.COUNTRY_ID));

        verify(eventMapper).toDto(event);
    }

    @Test
    void getEventsByCityId_returnsMappedList() {

        when(eventRepository.findByCity_Id(LocationsTestConstants.CITY_ID))
                .thenReturn(List.of(event));

        when(eventMapper.toDto(event))
                .thenReturn(viewDto);

        List<ViewEventDTO> result =
                eventService.getEventsByCityId(
                        LocationsTestConstants.CITY_ID);

        assertEquals(1, result.size());
        assertEquals(viewDto, result.get(0));

        verify(eventRepository)
                .findByCity_Id((LocationsTestConstants.CITY_ID));

        verify(eventMapper).toDto(event);
    }

    @Test
    void getById_whenExists_returnsDto() {

        when(eventRepository.findById(event.getId()))
                .thenReturn(Optional.of(event));

        when(eventMapper.toDto(event))
                .thenReturn(viewDto);

        ViewEventDTO result = eventService.getById(event.getId());

        assertEquals(viewDto, result);

        verify(eventRepository).findById(event.getId());
        verify(eventMapper).toDto(event);
    }

    @Test
    void getById_whenMissing_throwsException() {

        when(eventRepository.findById(event.getId()))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> eventService.getById(event.getId())
        );

        verify(eventRepository).findById(event.getId());
    }

    @Test
    void create_whenValid_savesAndReturnsDto() {

        Campaign campaign = new Campaign();
        campaign.setUuid(createDto.getCampaignUuid());

        when(eventMapper.toEntity(createDto)).thenReturn(event);
        when(campaignRepository.getReferenceById(createDto.getCampaignUuid()))
                .thenReturn(campaign);

        when(eventRepository.save(event)).thenReturn(event);
        when(eventMapper.toDto(event)).thenReturn(viewDto);

        ViewEventDTO result = eventService.create(createDto);

        assertEquals(viewDto, result);

        verify(eventMapper).toEntity(createDto);
        verify(campaignRepository).getReferenceById(createDto.getCampaignUuid());
        verify(eventRepository).save(event);
        verify(eventMapper).toDto(event);
    }

    @Test
    void update_whenValid_updatesAndReturnsDto() {

        Campaign campaign = new Campaign();
        campaign.setUuid(updateDto.getCampaignUuid());

        when(eventRepository.findById(updateDto.getId()))
                .thenReturn(Optional.of(event));

        when(campaignRepository.getReferenceById(updateDto.getCampaignUuid()))
                .thenReturn(campaign);

        when(eventRepository.save(event)).thenReturn(event);
        when(eventMapper.toDto(event)).thenReturn(viewDto);

        ViewEventDTO result = eventService.update(updateDto);

        assertEquals(viewDto, result);

        verify(eventRepository).findById(updateDto.getId());
        verify(eventMapper).updateEventFromDto(updateDto, event);
        verify(campaignRepository).getReferenceById(updateDto.getCampaignUuid());
        verify(eventRepository).save(event);
        verify(eventMapper).toDto(event);
    }

    @Test
    void update_whenMissing_throwsException() {

        when(eventRepository.findById(updateDto.getId()))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> eventService.update(updateDto)
        );

        verify(eventRepository).findById(updateDto.getId());
    }

    @Test
    void delete_callsRepository() {

        eventService.delete(event.getId());

        verify(eventRepository).deleteById(event.getId());
    }
}
