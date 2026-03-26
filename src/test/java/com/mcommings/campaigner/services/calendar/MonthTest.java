package com.mcommings.campaigner.services.calendar;

import com.mcommings.campaigner.modules.calendar.dtos.months.CreateMonthDTO;
import com.mcommings.campaigner.modules.calendar.dtos.months.UpdateMonthDTO;
import com.mcommings.campaigner.modules.calendar.dtos.months.ViewMonthDTO;
import com.mcommings.campaigner.modules.calendar.entities.Month;
import com.mcommings.campaigner.modules.calendar.mappers.MonthMapper;
import com.mcommings.campaigner.modules.calendar.repositories.IMonthRepository;
import com.mcommings.campaigner.modules.calendar.services.MonthService;
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
public class MonthTest {

    @Mock
    private IMonthRepository monthRepository;

    @Mock
    private ICampaignRepository campaignRepository;
    
    @Mock
    private MonthMapper monthMapper;

    @InjectMocks
    private MonthService monthService;

    private Month month;
    private ViewMonthDTO viewDto;
    private CreateMonthDTO createDto;
    private UpdateMonthDTO updateDto;

    @BeforeEach
    void setUp() {
        month = CalendarTestDataFactory.month();
        viewDto = CalendarTestDataFactory.viewMonthDTO();
        createDto = CalendarTestDataFactory.createMonthDTO();
        updateDto = CalendarTestDataFactory.updateMonthDTO();
    }

    @Test
    void getAll_returnsMappedDtos() {

        when(monthRepository.findAll()).thenReturn(List.of(month));
        when(monthMapper.toDto(month)).thenReturn(viewDto);

        List<ViewMonthDTO> result = monthService.getAll();

        assertEquals(1, result.size());
        assertEquals(viewDto, result.get(0));

        verify(monthRepository).findAll();
        verify(monthMapper).toDto(month);
    }

    @Test
    void getMonthsByCampaignUUID_returnsMappedList() {

        when(monthRepository.findByCampaign_Uuid(CalendarTestConstants.CAMPAIGN_UUID))
                .thenReturn(List.of(month));

        when(monthMapper.toDto(month))
                .thenReturn(viewDto);

        List<ViewMonthDTO> result =
                monthService.getMonthsByCampaignUUID(
                        CalendarTestConstants.CAMPAIGN_UUID);

        assertEquals(1, result.size());
        assertEquals(viewDto, result.get(0));

        verify(monthRepository)
                .findByCampaign_Uuid(CalendarTestConstants.CAMPAIGN_UUID);

        verify(monthMapper).toDto(month);
    }

    @Test
    void getById_whenExists_returnsDto() {

        when(monthRepository.findById(month.getId()))
                .thenReturn(Optional.of(month));

        when(monthMapper.toDto(month))
                .thenReturn(viewDto);

        ViewMonthDTO result = monthService.getById(month.getId());

        assertEquals(viewDto, result);

        verify(monthRepository).findById(month.getId());
        verify(monthMapper).toDto(month);
    }

    @Test
    void getById_whenMissing_throwsException() {

        when(monthRepository.findById(month.getId()))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> monthService.getById(month.getId())
        );

        verify(monthRepository).findById(month.getId());
    }

    @Test
    void create_whenValid_savesAndReturnsDto() {

        Campaign campaign = new Campaign();
        campaign.setUuid(createDto.getCampaignUuid());

        when(monthMapper.toEntity(createDto)).thenReturn(month);
        when(campaignRepository.getReferenceById(createDto.getCampaignUuid()))
                .thenReturn(campaign);

        when(monthRepository.save(month)).thenReturn(month);
        when(monthMapper.toDto(month)).thenReturn(viewDto);

        ViewMonthDTO result = monthService.create(createDto);

        assertEquals(viewDto, result);

        verify(monthMapper).toEntity(createDto);
        verify(campaignRepository).getReferenceById(createDto.getCampaignUuid());
        verify(monthRepository).save(month);
        verify(monthMapper).toDto(month);
    }

    @Test
    void update_whenValid_updatesAndReturnsDto() {

        Campaign campaign = new Campaign();
        campaign.setUuid(updateDto.getCampaignUuid());

        when(monthRepository.findById(updateDto.getId()))
                .thenReturn(Optional.of(month));

        when(campaignRepository.getReferenceById(updateDto.getCampaignUuid()))
                .thenReturn(campaign);

        when(monthRepository.save(month)).thenReturn(month);
        when(monthMapper.toDto(month)).thenReturn(viewDto);

        ViewMonthDTO result = monthService.update(updateDto);

        assertEquals(viewDto, result);

        verify(monthRepository).findById(updateDto.getId());
        verify(monthMapper).updateMonthFromDto(updateDto, month);
        verify(campaignRepository).getReferenceById(updateDto.getCampaignUuid());
        verify(monthRepository).save(month);
        verify(monthMapper).toDto(month);
    }

    @Test
    void update_whenMissing_throwsException() {

        when(monthRepository.findById(updateDto.getId()))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> monthService.update(updateDto)
        );

        verify(monthRepository).findById(updateDto.getId());
    }

    @Test
    void delete_callsRepository() {

        monthService.delete(month.getId());

        verify(monthRepository).deleteById(month.getId());
    }
}
