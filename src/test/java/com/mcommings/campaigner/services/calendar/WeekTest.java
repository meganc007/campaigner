package com.mcommings.campaigner.services.calendar;

import com.mcommings.campaigner.models.Event;
import com.mcommings.campaigner.models.RepositoryHelper;
import com.mcommings.campaigner.models.calendar.CelestialEvent;
import com.mcommings.campaigner.models.calendar.Day;
import com.mcommings.campaigner.models.calendar.Week;
import com.mcommings.campaigner.repositories.IEventRepository;
import com.mcommings.campaigner.repositories.calendar.ICelestialEventRepository;
import com.mcommings.campaigner.repositories.calendar.IDayRepository;
import com.mcommings.campaigner.repositories.calendar.IMonthRepository;
import com.mcommings.campaigner.repositories.calendar.IWeekRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
        weeks.add(new Week(1, "Description 1", 1, 1));
        weeks.add(new Week(2, "Description 2", 1, 2));
        weeks.add(new Week(3, "Description 3", 1, 3));
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
    public void whenWeekIsValid_saveWeek_SavesTheWeek() {
        Week week = new Week(1, "Description 1", 1, 2);

        when(monthRepository.existsById(2)).thenReturn(true);
        when(weekRepository.saveAndFlush(week)).thenReturn(week);

        assertDoesNotThrow(() -> weekService.saveWeek(week));

        verify(weekRepository, times(1)).saveAndFlush(week);
    }

    @Test
    public void whenWeekHasInvalidForeignKey_saveWeek_ThrowsDataIntegrityViolationException() {
        Week week = new Week(1, "Description 1", 1, 1);

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
        Day day = new Day(1, "Day", "Description", weekId);
        List<CrudRepository> repositories = new ArrayList<>(Arrays.asList(dayRepository));
        List<Day> days = new ArrayList<>(Arrays.asList(day));

        CelestialEvent celestialEvent = new CelestialEvent(1, "Celestial Event", "Description",
                1, 1, 1, weekId, 1, 1);
        List<CelestialEvent> celestialEvents = new ArrayList<>(Arrays.asList(celestialEvent));

        Event event = new Event(1, "Name", "Description", 1, 1, weekId, 1,
                1, 1, 1);
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

        Week week = new Week(weekId, "Test Description", 1, 1);
        Week weekToUpdate = new Week(weekId, "Updated Description", 2, 2);
        List<CrudRepository> repositories = new ArrayList<>(Arrays.asList(monthRepository));

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

        Week week = new Week(weekId, "Test Description", 1, 1);
        Week weekToUpdate = new Week(weekId, "Updated Description", 1, 2);
        List<CrudRepository> repositories = new ArrayList<>(Arrays.asList(monthRepository));

        when(weekRepository.existsById(weekId)).thenReturn(true);
        when(weekRepository.findById(weekId)).thenReturn(Optional.of(week));
        when(monthRepository.existsById(2)).thenReturn(false);

        assertThrows(DataIntegrityViolationException.class, () -> weekService.updateWeek(weekId, weekToUpdate));
    }

    @Test
    public void whenWeekIdIsNotFound_updateWeek_ThrowsIllegalArgumentException() {
        int weekId = 1;
        Week week = new Week(weekId, "Old Description", 1, 1);

        when(weekRepository.existsById(weekId)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> weekService.updateWeek(weekId, week));
    }
}
