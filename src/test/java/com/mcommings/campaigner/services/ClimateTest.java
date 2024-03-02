package com.mcommings.campaigner.services;

import com.mcommings.campaigner.models.Climate;
import com.mcommings.campaigner.models.RepositoryHelper;
import com.mcommings.campaigner.models.locations.Region;
import com.mcommings.campaigner.repositories.IClimateRepository;
import com.mcommings.campaigner.repositories.locations.IRegionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.CrudRepository;

import java.util.*;

import static com.mcommings.campaigner.enums.ForeignKey.FK_CLIMATE;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ClimateTest {

    @Mock
    private IClimateRepository climateRepository;
    @Mock
    private IRegionRepository regionRepository;

    @InjectMocks
    private ClimateService climateService;

    @Test
    public void whenThereAreClimates_getClimates_ReturnsClimates() {
        List<Climate> climates = new ArrayList<>();
        climates.add(new Climate(1, "Climate 1", "Description 1"));
        climates.add(new Climate(2, "Climate 2", "Description 2"));

        when(climateRepository.findAll()).thenReturn(climates);

        List<Climate> result = climateService.getClimates();

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(climates, result);
    }

    @Test
    public void whenThereAreNoClimates_getClimates_ReturnsNothing() {
        List<Climate> climates = new ArrayList<>();
        when(climateRepository.findAll()).thenReturn(climates);

        List<Climate> result = climateService.getClimates();

        Assertions.assertEquals(0, result.size());
        Assertions.assertEquals(climates, result);
    }

    @Test
    public void whenClimateIsValid_saveClimate_SavesTheClimate() {
        Climate climate = new Climate(1, "Climate 1", "Description 1");
        when(climateRepository.saveAndFlush(climate)).thenReturn(climate);

        assertDoesNotThrow(() -> climateService.saveClimate(climate));
        verify(climateRepository, times(1)).saveAndFlush(climate);
    }

    @Test
    public void whenClimateNameIsInvalid_saveClimate_ThrowsIllegalArgumentException() {
        Climate climateWithEmptyName = new Climate(1, "", "Description 1");
        Climate climateWithNullName = new Climate(2, null, "Description 2");

        assertThrows(IllegalArgumentException.class, () -> climateService.saveClimate(climateWithEmptyName));
        assertThrows(IllegalArgumentException.class, () -> climateService.saveClimate(climateWithNullName));
    }

    @Test
    public void whenClimateNameAlreadyExists_saveClimate_ThrowsDataIntegrityViolationException() {
        Climate climate = new Climate(1, "Climate 1", "Description 1");
        Climate climateWithDuplicatedName = new Climate(2, "Climate 1", "Description 2");
        when(climateRepository.saveAndFlush(climate)).thenReturn(climate);
        when(climateRepository.saveAndFlush(climateWithDuplicatedName)).thenThrow(DataIntegrityViolationException.class);

        assertDoesNotThrow(() -> climateService.saveClimate(climate));
        assertThrows(DataIntegrityViolationException.class, () -> climateService.saveClimate(climateWithDuplicatedName));
    }

    @Test
    public void whenClimateIdExists_deleteClimate_DeletesTheClimate() {
        int climateId = 1;
        when(climateRepository.existsById(climateId)).thenReturn(true);
        assertDoesNotThrow(() -> climateService.deleteClimate(climateId));
        verify(climateRepository, times(1)).deleteById(climateId);
    }

    @Test
    public void whenClimateIdDoesNotExist_deleteClimate_ThrowsIllegalArgumentException() {
        int climateId = 9000;
        when(climateRepository.existsById(climateId)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> climateService.deleteClimate(climateId));
    }

    @Test
    public void whenClimateIdIsAForeignKey_deleteClimate_ThrowsDataIntegrityViolationException() {
        int climateId = 1;
        Region region = new Region(1, "Region", "Description", UUID.randomUUID(), 1, climateId);
        List<CrudRepository> repositories = new ArrayList<>(Arrays.asList(regionRepository));
        List<Region> regions = new ArrayList<>(Arrays.asList(region));

        when(climateRepository.existsById(climateId)).thenReturn(true);
        when(regionRepository.findByfk_climate(climateId)).thenReturn(regions);

        boolean actual = RepositoryHelper.isForeignKey(repositories, FK_CLIMATE.columnName, climateId);
        Assertions.assertTrue(actual);
        assertThrows(DataIntegrityViolationException.class, () -> climateService.deleteClimate(climateId));
    }

    @Test
    public void whenClimateIdIsFound_updateClimate_UpdatesTheClimate() {
        int climateId = 1;
        Climate climate = new Climate(climateId, "Old Climate Name", "Old Description");
        Climate climateToUpdate = new Climate(climateId, "Updated Climate Name", "Updated Description");

        when(climateRepository.existsById(climateId)).thenReturn(true);
        when(climateRepository.findById(climateId)).thenReturn(Optional.of(climate));

        climateService.updateClimate(climateId, climateToUpdate);

        verify(climateRepository).findById(climateId);

        Climate result = climateRepository.findById(climateId).get();
        Assertions.assertEquals(climateToUpdate.getName(), result.getName());
        Assertions.assertEquals(climateToUpdate.getDescription(), result.getDescription());
    }

    @Test
    public void whenClimateIdIsNotFound_updateClimate_ThrowsIllegalArgumentException() {
        int climateId = 1;
        Climate climate = new Climate(climateId, "Old Climate Name", "Old Description");

        when(climateRepository.existsById(climateId)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> climateService.updateClimate(climateId, climate));
    }
    
    @Test
    public void whenSomeClimateFieldsChanged_updateClimate_OnlyUpdatesChangedFields() {
        int climateId = 1;
        Climate climate = new Climate(climateId, "Old Climate Name", "Old Description");
        Climate climateToUpdate = new Climate();
        climateToUpdate.setDescription("New climate description");

        when(climateRepository.existsById(climateId)).thenReturn(true);
        when(climateRepository.findById(climateId)).thenReturn(Optional.of(climate));

        climateService.updateClimate(climateId, climateToUpdate);

        verify(climateRepository).findById(climateId);

        Climate result = climateRepository.findById(climateId).get();
        Assertions.assertEquals("Old Climate Name", result.getName());
        Assertions.assertEquals(climateToUpdate.getDescription(), result.getDescription());
    }
}
