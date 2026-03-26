package com.mcommings.campaigner.services.calendar;

import com.mcommings.campaigner.modules.calendar.dtos.weeks.CreateWeekDTO;
import com.mcommings.campaigner.modules.calendar.dtos.weeks.UpdateWeekDTO;
import com.mcommings.campaigner.modules.calendar.dtos.weeks.ViewWeekDTO;
import com.mcommings.campaigner.modules.calendar.entities.Week;
import com.mcommings.campaigner.modules.calendar.mappers.WeekMapper;
import com.mcommings.campaigner.modules.calendar.repositories.IWeekRepository;
import com.mcommings.campaigner.modules.calendar.services.WeekService;
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
public class WeekTest {

    @Mock
    private IWeekRepository weekRepository;

    @Mock
    private ICampaignRepository campaignRepository;
    
    @Mock
    private WeekMapper weekMapper;

    @InjectMocks
    private WeekService weekService;

    private Week week;
    private ViewWeekDTO viewDto;
    private CreateWeekDTO createDto;
    private UpdateWeekDTO updateDto;

    @BeforeEach
    void setUp() {
        week = CalendarTestDataFactory.week();
        viewDto = CalendarTestDataFactory.viewWeekDTO();
        createDto = CalendarTestDataFactory.createWeekDTO();
        updateDto = CalendarTestDataFactory.updateWeekDTO();
    }

    @Test
    void getAll_returnsMappedDtos() {

        when(weekRepository.findAll()).thenReturn(List.of(week));
        when(weekMapper.toDto(week)).thenReturn(viewDto);

        List<ViewWeekDTO> result = weekService.getAll();

        assertEquals(1, result.size());
        assertEquals(viewDto, result.get(0));

        verify(weekRepository).findAll();
        verify(weekMapper).toDto(week);
    }

    @Test
    void getWeeksByCampaignUUID_returnsMappedList() {

        when(weekRepository.findByCampaign_Uuid(CalendarTestConstants.CAMPAIGN_UUID))
                .thenReturn(List.of(week));

        when(weekMapper.toDto(week))
                .thenReturn(viewDto);

        List<ViewWeekDTO> result =
                weekService.getWeeksByCampaignUUID(
                        CalendarTestConstants.CAMPAIGN_UUID);

        assertEquals(1, result.size());
        assertEquals(viewDto, result.get(0));

        verify(weekRepository)
                .findByCampaign_Uuid(CalendarTestConstants.CAMPAIGN_UUID);

        verify(weekMapper).toDto(week);
    }

    @Test
    void getWeeksByMonthId_returnsMappedList() {

        when(weekRepository.findByMonth_Id(CalendarTestConstants.MONTH_ID))
                .thenReturn(List.of(week));

        when(weekMapper.toDto(week))
                .thenReturn(viewDto);

        List<ViewWeekDTO> result =
                weekService.getWeeksByMonthId(
                        CalendarTestConstants.MONTH_ID);

        assertEquals(1, result.size());
        assertEquals(viewDto, result.get(0));

        verify(weekRepository)
                .findByMonth_Id((CalendarTestConstants.MONTH_ID));

        verify(weekMapper).toDto(week);
    }

    @Test
    void getById_whenExists_returnsDto() {

        when(weekRepository.findById(week.getId()))
                .thenReturn(Optional.of(week));

        when(weekMapper.toDto(week))
                .thenReturn(viewDto);

        ViewWeekDTO result = weekService.getById(week.getId());

        assertEquals(viewDto, result);

        verify(weekRepository).findById(week.getId());
        verify(weekMapper).toDto(week);
    }

    @Test
    void getById_whenMissing_throwsException() {

        when(weekRepository.findById(week.getId()))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> weekService.getById(week.getId())
        );

        verify(weekRepository).findById(week.getId());
    }

    @Test
    void create_whenValid_savesAndReturnsDto() {

        Campaign campaign = new Campaign();
        campaign.setUuid(createDto.getCampaignUuid());

        when(weekMapper.toEntity(createDto)).thenReturn(week);
        when(campaignRepository.getReferenceById(createDto.getCampaignUuid()))
                .thenReturn(campaign);

        when(weekRepository.save(week)).thenReturn(week);
        when(weekMapper.toDto(week)).thenReturn(viewDto);

        ViewWeekDTO result = weekService.create(createDto);

        assertEquals(viewDto, result);

        verify(weekMapper).toEntity(createDto);
        verify(campaignRepository).getReferenceById(createDto.getCampaignUuid());
        verify(weekRepository).save(week);
        verify(weekMapper).toDto(week);
    }

    @Test
    void update_whenValid_updatesAndReturnsDto() {

        Campaign campaign = new Campaign();
        campaign.setUuid(updateDto.getCampaignUuid());

        when(weekRepository.findById(updateDto.getId()))
                .thenReturn(Optional.of(week));

        when(campaignRepository.getReferenceById(updateDto.getCampaignUuid()))
                .thenReturn(campaign);

        when(weekRepository.save(week)).thenReturn(week);
        when(weekMapper.toDto(week)).thenReturn(viewDto);

        ViewWeekDTO result = weekService.update(updateDto);

        assertEquals(viewDto, result);

        verify(weekRepository).findById(updateDto.getId());
        verify(weekMapper).updateWeekFromDto(updateDto, week);
        verify(campaignRepository).getReferenceById(updateDto.getCampaignUuid());
        verify(weekRepository).save(week);
        verify(weekMapper).toDto(week);
    }

    @Test
    void update_whenMissing_throwsException() {

        when(weekRepository.findById(updateDto.getId()))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> weekService.update(updateDto)
        );

        verify(weekRepository).findById(updateDto.getId());
    }

    @Test
    void delete_callsRepository() {

        weekService.delete(week.getId());

        verify(weekRepository).deleteById(week.getId());
    }
}
