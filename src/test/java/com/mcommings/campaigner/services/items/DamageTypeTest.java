package com.mcommings.campaigner.services.items;

import com.mcommings.campaigner.models.items.DamageType;
import com.mcommings.campaigner.repositories.items.IDamageTypeRepository;
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
public class DamageTypeTest {

    @Mock
    private IDamageTypeRepository damageTypeRepository;

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

//    TODO: fix this test when classes that use DamageType as a fk are added
//    @Test
//    public void whenDamageTypeIdIsAForeignKey_deleteDamageType_ThrowsDataIntegrityViolationException() {
//        int damageTypeId = 1;
//        Region region = new Region(1, "Region", "Description", 1, damageTypeId);
//        List<CrudRepository> repositories = new ArrayList<>(Arrays.asList(regionRepository));
//        List<Region> regions = new ArrayList<>(Arrays.asList(region));
//
//        when(damageTypeRepository.existsById(damageTypeId)).thenReturn(true);
//        when(regionRepository.findByfk_damageType(damageTypeId)).thenReturn(regions);
//
//        boolean actual = RepositoryHelper.isForeignKey(repositories, FK_DAMAGETYPE.columnName, damageTypeId);
//        Assertions.assertTrue(actual);
//        assertThrows(DataIntegrityViolationException.class, () -> damageTypeService.deleteDamageType(damageTypeId));
//    }

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
}
