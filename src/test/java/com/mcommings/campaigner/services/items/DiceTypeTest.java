package com.mcommings.campaigner.services.items;

import com.mcommings.campaigner.models.RepositoryHelper;
import com.mcommings.campaigner.models.items.DiceType;
import com.mcommings.campaigner.models.items.Weapon;
import com.mcommings.campaigner.repositories.items.IDiceTypeRepository;
import com.mcommings.campaigner.repositories.items.IWeaponRepository;
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

import static com.mcommings.campaigner.enums.ForeignKey.FK_DICE_TYPE;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class DiceTypeTest {

    @Mock
    private IDiceTypeRepository diceTypeRepository;
    @Mock
    private IWeaponRepository weaponRepository;

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

    @Test
    public void whenDiceTypeIdIsAForeignKey_deleteDiceType_ThrowsDataIntegrityViolationException() {
        int diceTypeId = 1;

        Weapon weapon = new Weapon(1, "Weapon", "Description", "Rare", 32, 20,
                12, 20.0f, 2, 2, diceTypeId, 6, 0,
                true, false, "Notes");
        List<CrudRepository> repositories = new ArrayList<>(Arrays.asList(weaponRepository));
        List<Weapon> weapons = new ArrayList<>(Arrays.asList(weapon));

        when(diceTypeRepository.existsById(diceTypeId)).thenReturn(true);
        when(weaponRepository.findByfk_dice_type(diceTypeId)).thenReturn(weapons);

        boolean actual = RepositoryHelper.isForeignKey(repositories, FK_DICE_TYPE.columnName, diceTypeId);
        Assertions.assertTrue(actual);
        assertThrows(DataIntegrityViolationException.class, () -> diceTypeService.deleteDiceType(diceTypeId));
    }

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

    @Test
    public void whenSomeDiceTypeFieldsChanged_updateDiceType_OnlyUpdatesChangedFields() {
        int diceTypeId = 1;
        DiceType diceType = new DiceType(diceTypeId, "Name", "Description", 2);

        String newDescription = "New description";
        int newMaxRoll = -1;

        DiceType diceTypeToUpdate = new DiceType();
        diceTypeToUpdate.setDescription(newDescription);
        diceTypeToUpdate.setMax_roll(newMaxRoll);

        when(diceTypeRepository.existsById(diceTypeId)).thenReturn(true);
        when(diceTypeRepository.findById(diceTypeId)).thenReturn(Optional.of(diceType));

        diceTypeService.updateDiceType(diceTypeId, diceTypeToUpdate);

        verify(diceTypeRepository).findById(diceTypeId);

        DiceType result = diceTypeRepository.findById(diceTypeId).get();
        Assertions.assertEquals(diceType.getName(), result.getName());
        Assertions.assertEquals(newDescription, result.getDescription());
        Assertions.assertEquals(diceType.getMax_roll(), result.getMax_roll());
    }
}
