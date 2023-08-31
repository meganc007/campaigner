package com.mcommings.campaigner.services;

import com.mcommings.campaigner.models.Government;
import com.mcommings.campaigner.models.RepositoryHelper;
import com.mcommings.campaigner.models.locations.City;
import com.mcommings.campaigner.repositories.IGovernmentRepository;
import com.mcommings.campaigner.repositories.locations.ICityRepository;
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

import static com.mcommings.campaigner.enums.ForeignKey.FK_GOVERNMENT;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class GovernmentTest {

    @Mock
    private IGovernmentRepository governmentRepository;

    @Mock
    ICityRepository cityRepository;

    @InjectMocks
    private GovernmentService governmentService;

    @Test
    public void whenThereAreGovernments_getGovernments_ReturnsGovernments() {
        List<Government> governments = new ArrayList<>();
        governments.add(new Government(1, "Government 1", "Description 1"));
        governments.add(new Government(2, "Government 2", "Description 2"));
        when(governmentRepository.findAll()).thenReturn(governments);

        List<Government> result = governmentService.getGovernments();

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(governments, result);
    }

    @Test
    public void whenThereAreNoGovernments_getGovernments_ReturnsNothing() {
        List<Government> governments = new ArrayList<>();
        when(governmentRepository.findAll()).thenReturn(governments);

        List<Government> result = governmentService.getGovernments();

        Assertions.assertEquals(0, result.size());
        Assertions.assertEquals(governments, result);
    }

    @Test
    public void whenGovernmentIsValid_saveGovernment_SavesTheGovernment() {
        Government government = new Government(1, "Government 1", "Description 1");
        when(governmentRepository.saveAndFlush(government)).thenReturn(government);

        assertDoesNotThrow(() -> governmentService.saveGovernment(government));
        verify(governmentRepository, times(1)).saveAndFlush(government);
    }

    @Test
    public void whenGovernmentNameIsInvalid_saveGovernment_ThrowsIllegalArgumentException() {
        Government governmentWithEmptyName = new Government(1, "", "Description 1");
        Government governmentWithNullName = new Government(1, null, "Description 2");

        assertThrows(IllegalArgumentException.class, () -> governmentService.saveGovernment(governmentWithEmptyName));
        assertThrows(IllegalArgumentException.class, () -> governmentService.saveGovernment(governmentWithNullName));
    }

    @Test
    public void whenGovernmentNameAlreadyExists_saveGovernment_ThrowsDataIntegrityViolationException() {
        Government government = new Government(1, "Government 1", "Description 1");
        Government governmentWithDuplicatedName = new Government(2, "Government 1", "Description 2");
        when(governmentRepository.saveAndFlush(government)).thenReturn(government);
        when(governmentRepository.saveAndFlush(governmentWithDuplicatedName)).thenThrow(DataIntegrityViolationException.class);

        assertDoesNotThrow(() -> governmentService.saveGovernment(government));
        assertThrows(DataIntegrityViolationException.class, () -> governmentService.saveGovernment(governmentWithDuplicatedName));
    }

    @Test
    public void whenGovernmentIdExists_deleteGovernment_DeletesTheGovernment() {
        int governmentId = 1;
        when(governmentRepository.existsById(governmentId)).thenReturn(true);
        assertDoesNotThrow(() -> governmentService.deleteGovernment(governmentId));
        verify(governmentRepository, times(1)).deleteById(governmentId);
    }

    @Test
    public void whenGovernmentIdDoesNotExist_deleteGovernment_ThrowsIllegalArgumentException() {
        int governmentId = 9000;
        when(governmentRepository.existsById(governmentId)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> governmentService.deleteGovernment(governmentId));
    }

    @Test
    public void whenGovernmentIdIsAForeignKey_deleteGovernment_ThrowsDataIntegrityViolationException() {
        int governmentId = 1;
        City city = new City(1, "City", "Description", 1, 1, 1, governmentId);
        List<CrudRepository> repositories = new ArrayList<>(Arrays.asList(cityRepository));
        List<City> cities = new ArrayList<>(Arrays.asList(city));

        when(governmentRepository.existsById(governmentId)).thenReturn(true);
        when(cityRepository.findByfk_government(governmentId)).thenReturn(cities);

        boolean actual = RepositoryHelper.isForeignKey(repositories, FK_GOVERNMENT.columnName, governmentId);
        Assertions.assertTrue(actual);
        assertThrows(DataIntegrityViolationException.class, () -> governmentService.deleteGovernment(governmentId));
    }

    @Test
    public void whenGovernmentIdIsFound_updateGovernment_UpdatesTheGovernment() {
        int governmentId = 1;
        Government government = new Government(governmentId, "Old Government Name", "Old Description");
        Government governmentToUpdate = new Government(governmentId, "Updated Government Name", "Updated Description");

        when(governmentRepository.existsById(governmentId)).thenReturn(true);
        when(governmentRepository.findById(governmentId)).thenReturn(Optional.of(government));

        governmentService.updateGovernment(governmentId, governmentToUpdate);

        verify(governmentRepository).findById(governmentId);

        Government result = governmentRepository.findById(governmentId).get();
        Assertions.assertEquals(governmentToUpdate.getName(), result.getName());
        Assertions.assertEquals(governmentToUpdate.getDescription(), result.getDescription());
    }

    @Test
    public void whenGovernmentIdIsNotFound_updateGovernment_ThrowsIllegalArgumentException() {
        int governmentId = 1;
        Government government = new Government(governmentId, "Old Government Name", "Old Description");

        when(governmentRepository.existsById(governmentId)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> governmentService.updateGovernment(governmentId, government));
    }
}
