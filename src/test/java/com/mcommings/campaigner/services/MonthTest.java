package com.mcommings.campaigner.services;

import com.mcommings.campaigner.models.Month;
import com.mcommings.campaigner.models.repositories.IMonthRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class MonthTest {

    @Mock
    private IMonthRepository monthRepository;

    @InjectMocks
    private MonthService monthService;

    @Test
    public void whenThereAreMonths_getMonths_ReturnsMonths() {
        List<Month> months = new ArrayList<>();
        months.add(new Month(1, "Month 1", "Description 1"));
        months.add(new Month(2, "Month 2", "Description 2"));
        months.add(new Month(3, "Month 3", "Description 3", "fall"));
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
    public void whenMonthIsValid_saveMonth_SavesTheMonth() {
        Month month = new Month(1, "Month 1", "Description 1", "fall");
        when(monthRepository.saveAndFlush(month)).thenReturn(month);

        assertDoesNotThrow(() -> monthService.saveMonth(month));

        verify(monthRepository, times(1)).saveAndFlush(month);
    }

    @Test
    public void whenMonthNameIsInvalid_saveMonth_ThrowsIllegalArgumentException() {
        Month monthWithEmptyName = new Month(1, "", "Description 1");
        Month monthWithNullName = new Month(2, null, "Description 2");

        assertThrows(IllegalArgumentException.class, () -> monthService.saveMonth(monthWithEmptyName));
        assertThrows(IllegalArgumentException.class, () -> monthService.saveMonth(monthWithNullName));
    }

    @Test
    public void whenMonthNameAlreadyExists_saveMonth_ThrowsDataIntegrityViolationException() {
        Month month = new Month(1, "Month 1", "Description 1");
        Month monthWithDuplicatedName = new Month(2, "Month 1", "Description 2", "fall");

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

//    TODO: test that deleteMonth doesn't delete when it's a foreign key

    @Test
    public void whenMonthIdIsFound_updateMonth_UpdatesTheMonth() {
        int monthId = 1;
        Month month = new Month(monthId, "Old Month Name", "Old Description");
        Month monthToUpdate = new Month(monthId, "Updated Month Name", "Updated Description");

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
        Month month = new Month(monthId, "Old Month Name", "Old Description");

        when(monthRepository.existsById(monthId)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> monthService.updateMonth(monthId, month));
    }
}