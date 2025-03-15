package com.mcommings.campaigner.services.items;

import com.mcommings.campaigner.entities.RepositoryHelper;
import com.mcommings.campaigner.items.entities.DamageType;
import com.mcommings.campaigner.items.entities.Weapon;
import com.mcommings.campaigner.items.repositories.IDamageTypeRepository;
import com.mcommings.campaigner.items.repositories.IWeaponRepository;
import com.mcommings.campaigner.items.services.DamageTypeService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.CrudRepository;

import java.util.*;

import static com.mcommings.campaigner.enums.ForeignKey.FK_DAMAGE_TYPE;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class DamageTypeTest {

    @Mock
    private IDamageTypeRepository damageTypeRepository;
    @Mock
    private IWeaponRepository weaponRepository;

    @InjectMocks
    private DamageTypeService damageTypeService;

    @Test
    public void whenThereAreDamageTypes_getDamageTypes_ReturnsDamageTypes() {
        List<DamageType> damageTypes = new ArrayList<>();
        damageTypes.add(new DamageType(1, "DamageType 1", "Description 1"));
        damageTypes.add(new DamageType(2, "DamageType 2", "Description 2"));

        when(damageTypeRepository.findAll()).thenReturn(damageTypes);

        List<DamageType> result = damageTypeService.getDamageTypes();

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(damageTypes, result);
    }

    @Test
    public void whenThereAreNoDamageTypes_getDamageTypes_ReturnsNothing() {
        List<DamageType> damageTypes = new ArrayList<>();
        when(damageTypeRepository.findAll()).thenReturn(damageTypes);

        List<DamageType> result = damageTypeService.getDamageTypes();

        Assertions.assertEquals(0, result.size());
        Assertions.assertEquals(damageTypes, result);
    }

    @Test
    public void whenDamageTypeIsValid_saveDamageType_SavesTheDamageType() {
        DamageType damageType = new DamageType(1, "DamageType 1", "Description 1");
        when(damageTypeRepository.saveAndFlush(damageType)).thenReturn(damageType);

        assertDoesNotThrow(() -> damageTypeService.saveDamageType(damageType));
        verify(damageTypeRepository, times(1)).saveAndFlush(damageType);
    }

    @Test
    public void whenDamageTypeNameIsInvalid_saveDamageType_ThrowsIllegalArgumentException() {
        DamageType damageTypeWithEmptyName = new DamageType(1, "", "Description 1");
        DamageType damageTypeWithNullName = new DamageType(2, null, "Description 2");

        assertThrows(IllegalArgumentException.class, () -> damageTypeService.saveDamageType(damageTypeWithEmptyName));
        assertThrows(IllegalArgumentException.class, () -> damageTypeService.saveDamageType(damageTypeWithNullName));
    }

    @Test
    public void whenDamageTypeNameAlreadyExists_saveDamageType_ThrowsDataIntegrityViolationException() {
        DamageType damageType = new DamageType(1, "DamageType 1", "Description 1");
        DamageType damageTypeWithDuplicatedName = new DamageType(2, "DamageType 1", "Description 2");
        when(damageTypeRepository.saveAndFlush(damageType)).thenReturn(damageType);
        when(damageTypeRepository.saveAndFlush(damageTypeWithDuplicatedName)).thenThrow(DataIntegrityViolationException.class);

        assertDoesNotThrow(() -> damageTypeService.saveDamageType(damageType));
        assertThrows(DataIntegrityViolationException.class, () -> damageTypeService.saveDamageType(damageTypeWithDuplicatedName));
    }

    @Test
    public void whenDamageTypeIdExists_deleteDamageType_DeletesTheDamageType() {
        int damageTypeId = 1;
        when(damageTypeRepository.existsById(damageTypeId)).thenReturn(true);
        assertDoesNotThrow(() -> damageTypeService.deleteDamageType(damageTypeId));
        verify(damageTypeRepository, times(1)).deleteById(damageTypeId);
    }

    @Test
    public void whenDamageTypeIdDoesNotExist_deleteDamageType_ThrowsIllegalArgumentException() {
        int damageTypeId = 9000;
        when(damageTypeRepository.existsById(damageTypeId)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> damageTypeService.deleteDamageType(damageTypeId));
    }

    @Test
    public void whenDamageTypeIdIsAForeignKey_deleteDamageType_ThrowsDataIntegrityViolationException() {
        int damageTypeId = 1;
        Weapon weapon = new Weapon(1, "Weapon", "Description", "Rare", 32, 20,
                12, 20.0f, 2, damageTypeId, 2, 6, 0,
                true, false, "Notes", UUID.randomUUID());
        List<CrudRepository> repositories = new ArrayList<>(Arrays.asList(weaponRepository));
        List<Weapon> weapons = new ArrayList<>(Arrays.asList(weapon));

        when(damageTypeRepository.existsById(damageTypeId)).thenReturn(true);
        when(weaponRepository.findByfk_damage_type(damageTypeId)).thenReturn(weapons);

        boolean actual = RepositoryHelper.isForeignKey(repositories, FK_DAMAGE_TYPE.columnName, damageTypeId);
        Assertions.assertTrue(actual);
        assertThrows(DataIntegrityViolationException.class, () -> damageTypeService.deleteDamageType(damageTypeId));
    }

    @Test
    public void whenDamageTypeIdIsFound_updateDamageType_UpdatesTheDamageType() {
        int damageTypeId = 1;
        DamageType damageType = new DamageType(damageTypeId, "Old DamageType Name", "Old Description");
        DamageType damageTypeToUpdate = new DamageType(damageTypeId, "Updated DamageType Name", "Updated Description");

        when(damageTypeRepository.existsById(damageTypeId)).thenReturn(true);
        when(damageTypeRepository.findById(damageTypeId)).thenReturn(Optional.of(damageType));

        damageTypeService.updateDamageType(damageTypeId, damageTypeToUpdate);

        verify(damageTypeRepository).findById(damageTypeId);

        DamageType result = damageTypeRepository.findById(damageTypeId).get();
        Assertions.assertEquals(damageTypeToUpdate.getName(), result.getName());
        Assertions.assertEquals(damageTypeToUpdate.getDescription(), result.getDescription());
    }

    @Test
    public void whenDamageTypeIdIsNotFound_updateDamageType_ThrowsIllegalArgumentException() {
        int damageTypeId = 1;
        DamageType damageType = new DamageType(damageTypeId, "Old DamageType Name", "Old Description");

        when(damageTypeRepository.existsById(damageTypeId)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> damageTypeService.updateDamageType(damageTypeId, damageType));
    }

    @Test
    public void whenSomeDamageTypeFieldsChanged_updateDamageType_OnlyUpdatesChangedFields() {
        int damageTypeId = 1;
        DamageType damageType = new DamageType(damageTypeId, "Name", "Description");

        String newDescription = "New description";

        DamageType damageTypeToUpdate = new DamageType();
        damageTypeToUpdate.setDescription(newDescription);

        when(damageTypeRepository.existsById(damageTypeId)).thenReturn(true);
        when(damageTypeRepository.findById(damageTypeId)).thenReturn(Optional.of(damageType));

        damageTypeService.updateDamageType(damageTypeId, damageTypeToUpdate);

        verify(damageTypeRepository).findById(damageTypeId);

        DamageType result = damageTypeRepository.findById(damageTypeId).get();
        Assertions.assertEquals(damageType.getName(), result.getName());
        Assertions.assertEquals(newDescription, result.getDescription());
    }
}
