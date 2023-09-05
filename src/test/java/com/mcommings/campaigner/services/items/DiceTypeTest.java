package com.mcommings.campaigner.services.items;

import com.mcommings.campaigner.models.items.DiceType;
import com.mcommings.campaigner.repositories.items.IDiceTypeRepository;
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
public class DiceTypeTest {

    @Mock
    private IDiceTypeRepository diceTypeRepository;

    @InjectMocks
    private DiceTypeService diceTypeService;

    @Test
    public void whenThereAreDiceTypes_getDiceTypes_ReturnsDiceTypes() {
        List<DiceType> diceTypes = new ArrayList<>();
        diceTypes.add(new DiceType(1, "DiceType 1", "Description 1", 4));
        diceTypes.add(new DiceType(2, "DiceType 2", "Description 2", 6));

        when(diceTypeRepository.findAll()).thenReturn(diceTypes);

        List<DiceType> result = diceTypeService.getDiceTypes();

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(diceTypes, result);
    }

    @Test
    public void whenThereAreNoDiceTypes_getDiceTypes_ReturnsNothing() {
        List<DiceType> diceTypes = new ArrayList<>();
        when(diceTypeRepository.findAll()).thenReturn(diceTypes);

        List<DiceType> result = diceTypeService.getDiceTypes();

        Assertions.assertEquals(0, result.size());
        Assertions.assertEquals(diceTypes, result);
    }

    @Test
    public void whenDiceTypeIsValid_saveDiceType_SavesTheDiceType() {
        DiceType diceType = new DiceType(1, "DiceType 1", "Description 1", 4);
        when(diceTypeRepository.saveAndFlush(diceType)).thenReturn(diceType);

        assertDoesNotThrow(() -> diceTypeService.saveDiceType(diceType));
        verify(diceTypeRepository, times(1)).saveAndFlush(diceType);
    }

    @Test
    public void whenDiceTypeNameIsInvalid_saveDiceType_ThrowsIllegalArgumentException() {
        DiceType diceTypeWithEmptyName = new DiceType(1, "", "Description 1", 4);
        DiceType diceTypeWithNullName = new DiceType(2, null, "Description 2", 4);

        assertThrows(IllegalArgumentException.class, () -> diceTypeService.saveDiceType(diceTypeWithEmptyName));
        assertThrows(IllegalArgumentException.class, () -> diceTypeService.saveDiceType(diceTypeWithNullName));
    }

    @Test
    public void whenDiceTypeNameAlreadyExists_saveDiceType_ThrowsDataIntegrityViolationException() {
        DiceType diceType = new DiceType(1, "DiceType 1", "Description 1", 6);
        DiceType diceTypeWithDuplicatedName = new DiceType(2, "DiceType 1", "Description 2", 6);
        when(diceTypeRepository.saveAndFlush(diceType)).thenReturn(diceType);
        when(diceTypeRepository.saveAndFlush(diceTypeWithDuplicatedName)).thenThrow(DataIntegrityViolationException.class);

        assertDoesNotThrow(() -> diceTypeService.saveDiceType(diceType));
        assertThrows(DataIntegrityViolationException.class, () -> diceTypeService.saveDiceType(diceTypeWithDuplicatedName));
    }

    @Test
    public void whenDiceTypeIdExists_deleteDiceType_DeletesTheDiceType() {
        int diceTypeId = 1;
        when(diceTypeRepository.existsById(diceTypeId)).thenReturn(true);
        assertDoesNotThrow(() -> diceTypeService.deleteDiceType(diceTypeId));
        verify(diceTypeRepository, times(1)).deleteById(diceTypeId);
    }

    @Test
    public void whenDiceTypeIdDoesNotExist_deleteDiceType_ThrowsIllegalArgumentException() {
        int diceTypeId = 9000;
        when(diceTypeRepository.existsById(diceTypeId)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> diceTypeService.deleteDiceType(diceTypeId));
    }

//    TODO: fix this test when classes that use DiceType as a fk are added
//    @Test
//    public void whenDiceTypeIdIsAForeignKey_deleteDiceType_ThrowsDataIntegrityViolationException() {
//        int diceTypeId = 1;
//        Region region = new Region(1, "Region", "Description", 1, diceTypeId);
//        List<CrudRepository> repositories = new ArrayList<>(Arrays.asList(regionRepository));
//        List<Region> regions = new ArrayList<>(Arrays.asList(region));
//
//        when(diceTypeRepository.existsById(diceTypeId)).thenReturn(true);
//        when(regionRepository.findByfk_diceType(diceTypeId)).thenReturn(regions);
//
//        boolean actual = RepositoryHelper.isForeignKey(repositories, FK_DICETYPE.columnName, diceTypeId);
//        Assertions.assertTrue(actual);
//        assertThrows(DataIntegrityViolationException.class, () -> diceTypeService.deleteDiceType(diceTypeId));
//    }

    @Test
    public void whenDiceTypeIdIsFound_updateDiceType_UpdatesTheDiceType() {
        int diceTypeId = 1;
        DiceType diceType = new DiceType(diceTypeId, "Old DiceType Name", "Old Description", 4);
        DiceType diceTypeToUpdate = new DiceType(diceTypeId, "Updated DiceType Name", "Updated Description", 6);

        when(diceTypeRepository.existsById(diceTypeId)).thenReturn(true);
        when(diceTypeRepository.findById(diceTypeId)).thenReturn(Optional.of(diceType));

        diceTypeService.updateDiceType(diceTypeId, diceTypeToUpdate);

        verify(diceTypeRepository).findById(diceTypeId);

        DiceType result = diceTypeRepository.findById(diceTypeId).get();
        Assertions.assertEquals(diceTypeToUpdate.getName(), result.getName());
        Assertions.assertEquals(diceTypeToUpdate.getDescription(), result.getDescription());
        Assertions.assertEquals(diceTypeToUpdate.getMax_roll(), result.getMax_roll());
    }

    @Test
    public void whenDiceTypeIdIsNotFound_updateDiceType_ThrowsIllegalArgumentException() {
        int diceTypeId = 1;
        DiceType diceType = new DiceType(diceTypeId, "Old DiceType Name", "Old Description", 4);

        when(diceTypeRepository.existsById(diceTypeId)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> diceTypeService.updateDiceType(diceTypeId, diceType));
    }
}
