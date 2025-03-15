package com.mcommings.campaigner.services.calendar;

import com.mcommings.campaigner.calendar.entities.CelestialEvent;
import com.mcommings.campaigner.calendar.entities.Day;
import com.mcommings.campaigner.calendar.entities.Week;
import com.mcommings.campaigner.calendar.repositories.ICelestialEventRepository;
import com.mcommings.campaigner.calendar.repositories.IDayRepository;
import com.mcommings.campaigner.calendar.repositories.IMonthRepository;
import com.mcommings.campaigner.calendar.repositories.IWeekRepository;
import com.mcommings.campaigner.calendar.services.WeekService;
import com.mcommings.campaigner.common.entities.Event;
import com.mcommings.campaigner.common.entities.RepositoryHelper;
import com.mcommings.campaigner.common.repositories.IEventRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.CrudRepository;

import java.util.*;

import static com.mcommings.campaigner.enums.ForeignKey.FK_WEEK;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class WeekTest {

    @Mock
    private IWeekRepository weekRepository;
    @Mock
    private IDayRepository dayRepository;
    @Mock
    private IMonthRepository monthRepository;
    @Mock
    private ICelestialEventRepository celestialEventRepository;
    @Mock
    private IEventRepository eventRepository;

    @InjectMocks
    private WeekService weekService;

    @Test
    public void whenThereAreWeeks_getWeeks_ReturnsWeeks() {
        List<Week> weeks = new ArrayList<>();
        UUID campaign = UUID.randomUUID();
        weeks.add(new Week(1, "Description 1", 1, 1, campaign));
        weeks.add(new Week(2, "Description 2", 1, 2, campaign));
        weeks.add(new Week(3, "Description 3", 1, 3, campaign));
        when(weekRepository.findAll()).thenReturn(weeks);

        List<Week> result = weekService.getWeeks();

        Assertions.assertEquals(3, result.size());
        Assertions.assertEquals(weeks, result);
    }

    @Test
    public void whenThereAreNoWeeks_getWeeks_ReturnsNothing() {
        List<Week> weeks = new ArrayList<>();
        when(weekRepository.findAll()).thenReturn(weeks);

        List<Week> result = weekService.getWeeks();

        Assertions.assertEquals(0, result.size());
        Assertions.assertEquals(weeks, result);
    }

    @Test
    public void whenCampaignUUIDIsValid_getWeeksByCampaignUUID_ReturnsWeeks() {
        List<Week> weeks = new ArrayList<>();
        UUID campaign = UUID.randomUUID();
        weeks.add(new Week(1, "Description 1", 1, 1, campaign));
        weeks.add(new Week(2, "Description 2", 1, 2, campaign));
        weeks.add(new Week(3, "Description 3", 1, 3, campaign));
        when(weekRepository.findByfk_campaign_uuid(campaign)).thenReturn(weeks);

        List<Week> results = weekService.getWeeksByCampaignUUID(campaign);

        Assertions.assertEquals(3, results.size());
        Assertions.assertEquals(weeks, results);
    }

    @Test
    public void whenCampaignUUIDIsInvalid_getWeeksByCampaignUUID_ReturnsNothing() {
        UUID campaign = UUID.randomUUID();
        List<Week> weeks = new ArrayList<>();
        when(weekRepository.findByfk_campaign_uuid(campaign)).thenReturn(weeks);

        List<Week> result = weekService.getWeeksByCampaignUUID(campaign);

        Assertions.assertEquals(0, result.size());
        Assertions.assertEquals(weeks, result);
    }

    @Test
    public void whenWeekIsValid_saveWeek_SavesTheWeek() {
        Week week = new Week(1, "Description 1", 1, 2, UUID.randomUUID());

        when(monthRepository.existsById(2)).thenReturn(true);
        when(weekRepository.saveAndFlush(week)).thenReturn(week);

        assertDoesNotThrow(() -> weekService.saveWeek(week));

        verify(weekRepository, times(1)).saveAndFlush(week);
    }

    @Test
    public void whenWeekHasInvalidForeignKey_saveWeek_ThrowsDataIntegrityViolationException() {
        Week week = new Week(1, "Description 1", 1, 1, UUID.randomUUID());

        when(monthRepository.existsById(1)).thenReturn(false);
        when(weekRepository.saveAndFlush(week)).thenReturn(week);

        assertThrows(DataIntegrityViolationException.class, () -> weekService.saveWeek(week));

    }

    @Test
    public void whenWeekIdExists_deleteWeek_DeletesTheWeek() {
        int weekId = 1;
        when(weekRepository.existsById(weekId)).thenReturn(true);
        assertDoesNotThrow(() -> weekService.deleteWeek(weekId));
        verify(weekRepository, times(1)).deleteById(weekId);
    }

    @Test
    public void whenWeekIdDoesNotExist_deleteWeek_ThrowsIllegalArgumentException() {
        int weekId = 9000;
        when(weekRepository.existsById(weekId)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> weekService.deleteWeek(weekId));
    }

    @Test
    public void whenWeekIdIsAForeignKey_deleteWeek_ThrowsDataIntegrityViolationException() {
        int weekId = 1;
        Day day = new Day(1, "Day", "Description", weekId, UUID.randomUUID());
        List<CrudRepository> repositories = new ArrayList<>(Arrays.asList(dayRepository));
        List<Day> days = new ArrayList<>(Arrays.asList(day));

        CelestialEvent celestialEvent = new CelestialEvent(1, "Celestial Event", "Description",
                1, 1, 1, weekId, 1, 1, UUID.randomUUID());
        List<CelestialEvent> celestialEvents = new ArrayList<>(Arrays.asList(celestialEvent));

        Event event = new Event(1, "Name", "Description", 1, 1, weekId, 1,
                1, 1, 1, UUID.randomUUID());
        List<Event> events = new ArrayList<>(Arrays.asList(event));

        when(weekRepository.existsById(weekId)).thenReturn(true);
        when(dayRepository.findByfk_week(weekId)).thenReturn(days);
        when(celestialEventRepository.findByfk_week(weekId)).thenReturn(celestialEvents);
        when(eventRepository.findByfk_week(weekId)).thenReturn(events);

        boolean actual = RepositoryHelper.isForeignKey(repositories, FK_WEEK.columnName, weekId);
        Assertions.assertTrue(actual);
        assertThrows(DataIntegrityViolationException.class, () -> weekService.deleteWeek(weekId));
    }

    @Test
    public void whenWeekIdWithValidFKIsFound_updateWeek_UpdatesTheWeek() {
        int weekId = 2;
        UUID campaign = UUID.randomUUID();

        Week week = new Week(weekId, "Test Description", 1, 1, campaign);
        Week weekToUpdate = new Week(weekId, "Updated Description", 2, 2, campaign);

        when(weekRepository.existsById(weekId)).thenReturn(true);
        when(weekRepository.findById(weekId)).thenReturn(Optional.of(week));
        when(monthRepository.existsById(1)).thenReturn(true);
        when(monthRepository.existsById(2)).thenReturn(true);

        weekService.updateWeek(weekId, weekToUpdate);

        verify(weekRepository).findById(weekId);

        Week result = weekRepository.findById(weekId).get();
        Assertions.assertEquals(weekToUpdate.getDescription(), result.getDescription());
        Assertions.assertEquals(weekToUpdate.getWeek_number(), result.getWeek_number());
        Assertions.assertEquals(weekToUpdate.getFk_month(), result.getFk_month());
    }

    @Test
    public void whenWeekIdWithInvalidFKIsFound_updateWeek_ThrowsDataIntegrityViolationException() {
        int weekId = 1;
        UUID campaign = UUID.randomUUID();

        Week week = new Week(weekId, "Test Description", 1, 1, campaign);
        Week weekToUpdate = new Week(weekId, "Updated Description", 1, 2, campaign);

        when(weekRepository.existsById(weekId)).thenReturn(true);
        when(weekRepository.findById(weekId)).thenReturn(Optional.of(week));
        when(monthRepository.existsById(2)).thenReturn(false);

        assertThrows(DataIntegrityViolationException.class, () -> weekService.updateWeek(weekId, weekToUpdate));
    }

    @Test
    public void whenWeekIdIsNotFound_updateWeek_ThrowsIllegalArgumentException() {
        int weekId = 1;
        Week week = new Week(weekId, "Old Description", 1, 1, UUID.randomUUID());

        when(weekRepository.existsById(weekId)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> weekService.updateWeek(weekId, week));
    }

    @Test
    public void whenSomeWeekFieldsChanged_updateWeek_OnlyUpdatesChangedFields() {
        int weekId = 1;
        Week week = new Week(weekId, "Old Week Description", 1, 2, UUID.randomUUID());

        String newDescription = "New Week description";
        int newMonth = 3;

        Week weekToUpdate = new Week();
        weekToUpdate.setDescription(newDescription);
        weekToUpdate.setFk_month(newMonth);

        when(weekRepository.existsById(weekId)).thenReturn(true);
        when(monthRepository.existsById(2)).thenReturn(true);
        when(monthRepository.existsById(newMonth)).thenReturn(true);
        when(weekRepository.findById(weekId)).thenReturn(Optional.of(week));

        weekService.updateWeek(weekId, weekToUpdate);

        verify(weekRepository).findById(weekId);

        Week result = weekRepository.findById(weekId).get();
        Assertions.assertEquals(newDescription, result.getDescription());
        Assertions.assertEquals(week.getWeek_number(), result.getWeek_number());
        Assertions.assertEquals(newMonth, result.getFk_month());
    }
}
