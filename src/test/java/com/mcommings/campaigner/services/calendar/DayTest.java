package com.mcommings.campaigner.services.calendar;

import com.mcommings.campaigner.models.calendar.Day;
import com.mcommings.campaigner.models.repositories.calendar.IDayRepository;
import com.mcommings.campaigner.models.repositories.calendar.IWeekRepository;
import com.mcommings.campaigner.services.calendar.DayService;
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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class DayTest {

    @Mock
    private IDayRepository dayRepository;
    @Mock
    private IWeekRepository weekRepository;

    @InjectMocks
    private DayService dayService;

    @Test
    public void whenThereAreDays_getDays_ReturnsDays() {
        List<Day> days = new ArrayList<>();
        days.add(new Day(1, "Day 1", "Description 1", 1));
        days.add(new Day(2, "Day 2", "Description 2", 1));
        days.add(new Day(3, "Day 3", "Description 3", 1));
        when(dayRepository.findAll()).thenReturn(days);

        List<Day> result = dayService.getDays();

        Assertions.assertEquals(3, result.size());
        Assertions.assertEquals(days, result);
    }

    @Test
    public void whenThereAreNoDays_getDays_ReturnsNothing() {
        List<Day> days = new ArrayList<>();
        when(dayRepository.findAll()).thenReturn(days);

        List<Day> result = dayService.getDays();

        Assertions.assertEquals(0, result.size());
        Assertions.assertEquals(days, result);
    }

    @Test
    public void whenDayIsValid_saveDay_SavesTheDay() {
        Day day = new Day(1, "Day 1", "Description 1", 1);

        when(weekRepository.existsById(1)).thenReturn(true);
        when(dayRepository.saveAndFlush(day)).thenReturn(day);

        assertDoesNotThrow(() -> dayService.saveDay(day));

        verify(dayRepository, times(1)).saveAndFlush(day);
    }

    @Test
    public void whenDayNameIsInvalid_saveDay_ThrowsIllegalArgumentException() {
        Day dayWithEmptyName = new Day(1, "", "Description 1", 1);
        Day dayWithNullName = new Day(2, null, "Description 2", 1);

        assertThrows(IllegalArgumentException.class, () -> dayService.saveDay(dayWithEmptyName));
        assertThrows(IllegalArgumentException.class, () -> dayService.saveDay(dayWithNullName));
    }

    @Test
    public void whenDayHasInvalidForeignKeys_saveDay_ThrowsDataIntegrityViolationException() {
        Day day = new Day(1, "Day 1", "Description 1", 1);

        when(weekRepository.existsById(1)).thenReturn(false);
        when(dayRepository.saveAndFlush(day)).thenReturn(day);

        assertThrows(DataIntegrityViolationException.class, () -> dayService.saveDay(day));
    }

    @Test
    public void whenDayIdExists_deleteDay_DeletesTheDay() {
        int dayId = 1;
        when(dayRepository.existsById(dayId)).thenReturn(true);
        assertDoesNotThrow(() -> dayService.deleteDay(dayId));
        verify(dayRepository, times(1)).deleteById(dayId);
    }

    @Test
    public void whenDayIdDoesNotExist_deleteDay_ThrowsIllegalArgumentException() {
        int dayId = 9000;
        when(dayRepository.existsById(dayId)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> dayService.deleteDay(dayId));
    }

    //TODO: test that deleteDay doesn't delete when it's a foreign key

    @Test
    public void whenDayIdWithValidFKIsFound_updateDay_UpdatesTheDay() {
        int dayId = 2;

        Day day = new Day(dayId, "Test Day Name", "Test Description", 1);
        Day dayToUpdate = new Day(dayId, "Updated Day Name", "Updated Description", 2);
        List<CrudRepository> repositories = new ArrayList<>(Arrays.asList(weekRepository));

        when(dayRepository.existsById(dayId)).thenReturn(true);
        when(dayRepository.findById(dayId)).thenReturn(Optional.of(day));
        when(weekRepository.existsById(1)).thenReturn(true);
        when(weekRepository.existsById(2)).thenReturn(true);

        dayService.updateDay(dayId, dayToUpdate);

        verify(dayRepository).findById(dayId);

        Day result = dayRepository.findById(dayId).get();
        Assertions.assertEquals(dayToUpdate.getName(), result.getName());
        Assertions.assertEquals(dayToUpdate.getDescription(), result.getDescription());
        Assertions.assertEquals(dayToUpdate.getFk_week(), result.getFk_week());
    }

    @Test
    public void whenDayIdWithInvalidFKIsFound_updateDay_ThrowsDataIntegrityViolationException() {
        int dayId = 2;

        Day day = new Day(dayId, "Test Day Name", "Test Description", 1);
        Day dayToUpdate = new Day(dayId, "Updated Day Name", "Updated Description", 4);
        List<CrudRepository> repositories = new ArrayList<>(Arrays.asList(weekRepository));

        when(dayRepository.existsById(dayId)).thenReturn(true);
        when(dayRepository.findById(dayId)).thenReturn(Optional.of(day));
        when(weekRepository.existsById(1)).thenReturn(false);

        assertThrows(DataIntegrityViolationException.class, () -> dayService.updateDay(dayId, dayToUpdate));
    }

    @Test
    public void whenDayIdIsNotFound_updateDay_ThrowsIllegalArgumentException() {
        int dayId = 1;
        Day day = new Day(dayId, "Old Day Name", "Old Description", 3);

        when(dayRepository.existsById(dayId)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> dayService.updateDay(dayId, day));
    }
}
