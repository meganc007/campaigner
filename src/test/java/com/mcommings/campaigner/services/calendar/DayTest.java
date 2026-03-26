package com.mcommings.campaigner.services.calendar;

import com.mcommings.campaigner.modules.calendar.dtos.days.CreateDayDTO;
import com.mcommings.campaigner.modules.calendar.dtos.days.UpdateDayDTO;
import com.mcommings.campaigner.modules.calendar.dtos.days.ViewDayDTO;
import com.mcommings.campaigner.modules.calendar.entities.Day;
import com.mcommings.campaigner.modules.calendar.mappers.DayMapper;
import com.mcommings.campaigner.modules.calendar.repositories.IDayRepository;
import com.mcommings.campaigner.modules.calendar.services.DayService;
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
public class DayTest {

    @Mock
    private IDayRepository dayRepository;

    @Mock
    private ICampaignRepository campaignRepository;
    
    @Mock
    private DayMapper dayMapper;

    @InjectMocks
    private DayService dayService;

    private Day day;
    private ViewDayDTO viewDto;
    private CreateDayDTO createDto;
    private UpdateDayDTO updateDto;

    @BeforeEach
    void setUp() {
        day = CalendarTestDataFactory.day();
        viewDto = CalendarTestDataFactory.viewDayDTO();
        createDto = CalendarTestDataFactory.createDayDTO();
        updateDto = CalendarTestDataFactory.updateDayDTO();
    }

    @Test
    void getAll_returnsMappedDtos() {

        when(dayRepository.findAll()).thenReturn(List.of(day));
        when(dayMapper.toDto(day)).thenReturn(viewDto);

        List<ViewDayDTO> result = dayService.getAll();

        assertEquals(1, result.size());
        assertEquals(viewDto, result.get(0));

        verify(dayRepository).findAll();
        verify(dayMapper).toDto(day);
    }

    @Test
    void getDaysByCampaignUUID_returnsMappedList() {

        when(dayRepository.findByCampaign_Uuid(CalendarTestConstants.CAMPAIGN_UUID))
                .thenReturn(List.of(day));

        when(dayMapper.toDto(day))
                .thenReturn(viewDto);

        List<ViewDayDTO> result =
                dayService.getDaysByCampaignUUID(
                        CalendarTestConstants.CAMPAIGN_UUID);

        assertEquals(1, result.size());
        assertEquals(viewDto, result.get(0));

        verify(dayRepository)
                .findByCampaign_Uuid(CalendarTestConstants.CAMPAIGN_UUID);

        verify(dayMapper).toDto(day);
    }

    @Test
    void getDaysByWeekId_returnsMappedList() {

        when(dayRepository.findByWeek_Id(CalendarTestConstants.WEEK_ID))
                .thenReturn(List.of(day));

        when(dayMapper.toDto(day))
                .thenReturn(viewDto);

        List<ViewDayDTO> result =
                dayService.getDaysByWeekId(
                        CalendarTestConstants.WEEK_ID);

        assertEquals(1, result.size());
        assertEquals(viewDto, result.get(0));

        verify(dayRepository)
                .findByWeek_Id((CalendarTestConstants.WEEK_ID));

        verify(dayMapper).toDto(day);
    }

    @Test
    void getById_whenExists_returnsDto() {

        when(dayRepository.findById(day.getId()))
                .thenReturn(Optional.of(day));

        when(dayMapper.toDto(day))
                .thenReturn(viewDto);

        ViewDayDTO result = dayService.getById(day.getId());

        assertEquals(viewDto, result);

        verify(dayRepository).findById(day.getId());
        verify(dayMapper).toDto(day);
    }

    @Test
    void getById_whenMissing_throwsException() {

        when(dayRepository.findById(day.getId()))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> dayService.getById(day.getId())
        );

        verify(dayRepository).findById(day.getId());
    }

    @Test
    void create_whenValid_savesAndReturnsDto() {

        Campaign campaign = new Campaign();
        campaign.setUuid(createDto.getCampaignUuid());

        when(dayMapper.toEntity(createDto)).thenReturn(day);
        when(campaignRepository.getReferenceById(createDto.getCampaignUuid()))
                .thenReturn(campaign);

        when(dayRepository.save(day)).thenReturn(day);
        when(dayMapper.toDto(day)).thenReturn(viewDto);

        ViewDayDTO result = dayService.create(createDto);

        assertEquals(viewDto, result);

        verify(dayMapper).toEntity(createDto);
        verify(campaignRepository).getReferenceById(createDto.getCampaignUuid());
        verify(dayRepository).save(day);
        verify(dayMapper).toDto(day);
    }

    @Test
    void update_whenValid_updatesAndReturnsDto() {

        Campaign campaign = new Campaign();
        campaign.setUuid(updateDto.getCampaignUuid());

        when(dayRepository.findById(updateDto.getId()))
                .thenReturn(Optional.of(day));

        when(campaignRepository.getReferenceById(updateDto.getCampaignUuid()))
                .thenReturn(campaign);

        when(dayRepository.save(day)).thenReturn(day);
        when(dayMapper.toDto(day)).thenReturn(viewDto);

        ViewDayDTO result = dayService.update(updateDto);

        assertEquals(viewDto, result);

        verify(dayRepository).findById(updateDto.getId());
        verify(dayMapper).updateDayFromDto(updateDto, day);
        verify(campaignRepository).getReferenceById(updateDto.getCampaignUuid());
        verify(dayRepository).save(day);
        verify(dayMapper).toDto(day);
    }

    @Test
    void update_whenMissing_throwsException() {

        when(dayRepository.findById(updateDto.getId()))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> dayService.update(updateDto)
        );

        verify(dayRepository).findById(updateDto.getId());
    }

    @Test
    void delete_callsRepository() {

        dayService.delete(day.getId());

        verify(dayRepository).deleteById(day.getId());
    }
}
