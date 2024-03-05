package com.mcommings.campaigner.services.calendar;

import com.mcommings.campaigner.models.Event;
import com.mcommings.campaigner.models.RepositoryHelper;
import com.mcommings.campaigner.models.calendar.CelestialEvent;
import com.mcommings.campaigner.models.calendar.Month;
import com.mcommings.campaigner.models.calendar.Week;
import com.mcommings.campaigner.repositories.IEventRepository;
import com.mcommings.campaigner.repositories.calendar.ICelestialEventRepository;
import com.mcommings.campaigner.repositories.calendar.IMonthRepository;
import com.mcommings.campaigner.repositories.calendar.IWeekRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.CrudRepository;

import java.util.*;

import static com.mcommings.campaigner.enums.ForeignKey.FK_MONTH;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class MonthTest {

    @Mock
    private IMonthRepository monthRepository;
    @Mock
    private IWeekRepository weekRepository;
    @Mock
    private ICelestialEventRepository celestialEventRepository;
    @Mock
    private IEventRepository eventRepository;

    @InjectMocks
    private MonthService monthService;

    @Test
    public void whenThereAreMonths_getMonths_ReturnsMonths() {
        List<Month> months = new ArrayList<>();
        UUID campaign = UUID.randomUUID();
        months.add(new Month(1, "Month 1", "Description 1", campaign));
        months.add(new Month(2, "Month 2", "Description 2", campaign));
        months.add(new Month(3, "Month 3", "Description 3", "fall", campaign));
        when(monthRepository.findAll()).thenReturn(months);

        List<Month> result = monthService.getMonths();

        Assertions.assertEquals(3, result.size());
        Assertions.assertEquals(months, result);
    }

    @Test
    public void whenThereAreNoMonths_getMonths_ReturnsNothing() {
        List<Month> months = new ArrayList<>();
        when(monthRepository.findAll()).thenReturn(months);

        List<Month> result = monthService.getMonths();

        Assertions.assertEquals(0, result.size());
        Assertions.assertEquals(months, result);
    }

    @Test
    public void whenCampaignUUIDIsValid_getMonthsByCampaignUUID_ReturnsMonths() {
        List<Month> months = new ArrayList<>();
        UUID campaign = UUID.randomUUID();
        months.add(new Month(1, "Month 1", "Description 1", campaign));
        months.add(new Month(2, "Month 2", "Description 2", campaign));
        months.add(new Month(3, "Month 3", "Description 3", "fall", campaign));
        when(monthRepository.findByfk_campaign_uuid(campaign)).thenReturn(months);

        List<Month> results = monthService.getMonthsByCampaignUUID(campaign);

        Assertions.assertEquals(3, results.size());
        Assertions.assertEquals(months, results);
    }

    @Test
    public void whenCampaignUUIDIsInvalid_getMonthsByCampaignUUID_ReturnsNothing() {
        UUID campaign = UUID.randomUUID();
        List<Month> months = new ArrayList<>();
        when(monthRepository.findByfk_campaign_uuid(campaign)).thenReturn(months);

        List<Month> result = monthService.getMonthsByCampaignUUID(campaign);

        Assertions.assertEquals(0, result.size());
        Assertions.assertEquals(months, result);
    }

    @Test
    public void whenMonthIsValid_saveMonth_SavesTheMonth() {
        Month month = new Month(1, "Month 1", "Description 1", "fall", UUID.randomUUID());
        when(monthRepository.saveAndFlush(month)).thenReturn(month);

        assertDoesNotThrow(() -> monthService.saveMonth(month));

        verify(monthRepository, times(1)).saveAndFlush(month);
    }

    @Test
    public void whenMonthNameIsInvalid_saveMonth_ThrowsIllegalArgumentException() {
        Month monthWithEmptyName = new Month(1, "", "Description 1", UUID.randomUUID());
        Month monthWithNullName = new Month(2, null, "Description 2", UUID.randomUUID());

        assertThrows(IllegalArgumentException.class, () -> monthService.saveMonth(monthWithEmptyName));
        assertThrows(IllegalArgumentException.class, () -> monthService.saveMonth(monthWithNullName));
    }

    @Test
    public void whenMonthNameAlreadyExists_saveMonth_ThrowsDataIntegrityViolationException() {
        Month month = new Month(1, "Month 1", "Description 1", UUID.randomUUID());
        Month monthWithDuplicatedName = new Month(2, "Month 1", "Description 2", "fall", UUID.randomUUID());

        when(monthRepository.existsById(1)).thenReturn(true);
        when(monthRepository.existsById(2)).thenReturn(true);

        when(monthRepository.saveAndFlush(month)).thenReturn(month);
        when(monthRepository.saveAndFlush(monthWithDuplicatedName)).thenThrow(DataIntegrityViolationException.class);

        assertDoesNotThrow(() -> monthService.saveMonth(month));
        assertThrows(DataIntegrityViolationException.class, () -> monthService.saveMonth(monthWithDuplicatedName));
    }

    @Test
    public void whenMonthIdExists_deleteMonth_DeletesTheMonth() {
        int monthId = 1;
        when(monthRepository.existsById(monthId)).thenReturn(true);
        assertDoesNotThrow(() -> monthService.deleteMonth(monthId));
        verify(monthRepository, times(1)).deleteById(monthId);
    }

    @Test
    public void whenMonthIdDoesNotExist_deleteMonth_ThrowsIllegalArgumentException() {
        int monthId = 9000;
        when(monthRepository.existsById(monthId)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> monthService.deleteMonth(monthId));
    }

    @Test
    public void whenMonthIdIsAForeignKey_deleteMonth_ThrowsDataIntegrityViolationException() {
        int monthId = 1;
        Week week = new Week(1, "Description", 1, monthId);
        List<CrudRepository> repositories = new ArrayList<>(Arrays.asList(weekRepository, celestialEventRepository));
        List<Week> weeks = new ArrayList<>(Arrays.asList(week));

        CelestialEvent celestialEvent = new CelestialEvent(1, "Name", "Description", 1, 1,
                monthId, 1, 1, 1);
        List<CelestialEvent> celestialEvents = new ArrayList<>(Arrays.asList(celestialEvent));

        Event event = new Event(1, "Name", "Description", 1, monthId, 1, 1,
                1, 1, 1, UUID.randomUUID());
        List<Event> events = new ArrayList<>(Arrays.asList(event));

        when(monthRepository.existsById(monthId)).thenReturn(true);
        when(weekRepository.findByfk_month(monthId)).thenReturn(weeks);
        when(celestialEventRepository.findByfk_month(monthId)).thenReturn(celestialEvents);
        when(eventRepository.findByfk_month(monthId)).thenReturn(events);

        boolean actual = RepositoryHelper.isForeignKey(repositories, FK_MONTH.columnName, monthId);
        Assertions.assertTrue(actual);
        assertThrows(DataIntegrityViolationException.class, () -> monthService.deleteMonth(monthId));
    }

    @Test
    public void whenMonthIdIsFound_updateMonth_UpdatesTheMonth() {
        int monthId = 1;
        UUID campaign = UUID.randomUUID();
        Month month = new Month(monthId, "Old Month Name", "Old Description", campaign);
        Month monthToUpdate = new Month(monthId, "Updated Month Name", "Updated Description", campaign);

        when(monthRepository.existsById(monthId)).thenReturn(true);
        when(monthRepository.findById(monthId)).thenReturn(Optional.of(month));

        monthService.updateMonth(monthId, monthToUpdate);

        verify(monthRepository).findById(monthId);

        Month result = monthRepository.findById(monthId).get();
        Assertions.assertEquals(monthToUpdate.getName(), result.getName());
        Assertions.assertEquals(monthToUpdate.getDescription(), result.getDescription());
    }

    @Test
    public void whenMonthIdIsNotFound_updateMonth_ThrowsIllegalArgumentException() {
        int monthId = 1;
        Month month = new Month(monthId, "Old Month Name", "Old Description", UUID.randomUUID());

        when(monthRepository.existsById(monthId)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> monthService.updateMonth(monthId, month));
    }

    @Test
    public void whenSomeMonthFieldsChanged_updateMonth_OnlyUpdatesChangedFields() {
        int monthId = 1;
        Month month = new Month(monthId, "Old Month Name", "Old Description", "SUMMER", UUID.randomUUID());

        String fall = "FALL";

        Month monthToUpdate = new Month();
        monthToUpdate.setSeason(fall);

        when(monthRepository.existsById(monthId)).thenReturn(true);
        when(monthRepository.findById(monthId)).thenReturn(Optional.of(month));

        monthService.updateMonth(monthId, monthToUpdate);

        verify(monthRepository).findById(monthId);

        Month result = monthRepository.findById(monthId).get();
        Assertions.assertEquals(month.getName(), result.getName());
        Assertions.assertEquals(month.getDescription(), result.getDescription());
        Assertions.assertEquals(fall, result.getSeason());
    }
}
