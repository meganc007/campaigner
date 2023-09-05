package com.mcommings.campaigner.services.items;

import com.mcommings.campaigner.models.items.WeaponType;
import com.mcommings.campaigner.repositories.items.IWeaponTypeRepository;
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
public class WeaponTypeTest {

    @Mock
    private IWeaponTypeRepository weaponTypeRepository;

    @InjectMocks
    private WeaponTypeService weaponTypeService;

    @Test
    public void whenThereAreWeaponTypes_getWeaponTypes_ReturnsWeaponTypes() {
        List<WeaponType> weaponTypes = new ArrayList<>();
        weaponTypes.add(new WeaponType(1, "WeaponType 1", "Description 1"));
        weaponTypes.add(new WeaponType(2, "WeaponType 2", "Description 2"));

        when(weaponTypeRepository.findAll()).thenReturn(weaponTypes);

        List<WeaponType> result = weaponTypeService.getWeaponTypes();

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(weaponTypes, result);
    }

    @Test
    public void whenThereAreNoWeaponTypes_getWeaponTypes_ReturnsNothing() {
        List<WeaponType> weaponTypes = new ArrayList<>();
        when(weaponTypeRepository.findAll()).thenReturn(weaponTypes);

        List<WeaponType> result = weaponTypeService.getWeaponTypes();

        Assertions.assertEquals(0, result.size());
        Assertions.assertEquals(weaponTypes, result);
    }

    @Test
    public void whenWeaponTypeIsValid_saveWeaponType_SavesTheWeaponType() {
        WeaponType weaponType = new WeaponType(1, "WeaponType 1", "Description 1");
        when(weaponTypeRepository.saveAndFlush(weaponType)).thenReturn(weaponType);

        assertDoesNotThrow(() -> weaponTypeService.saveWeaponType(weaponType));
        verify(weaponTypeRepository, times(1)).saveAndFlush(weaponType);
    }

    @Test
    public void whenWeaponTypeNameIsInvalid_saveWeaponType_ThrowsIllegalArgumentException() {
        WeaponType weaponTypeWithEmptyName = new WeaponType(1, "", "Description 1");
        WeaponType weaponTypeWithNullName = new WeaponType(2, null, "Description 2");

        assertThrows(IllegalArgumentException.class, () -> weaponTypeService.saveWeaponType(weaponTypeWithEmptyName));
        assertThrows(IllegalArgumentException.class, () -> weaponTypeService.saveWeaponType(weaponTypeWithNullName));
    }

    @Test
    public void whenWeaponTypeNameAlreadyExists_saveWeaponType_ThrowsDataIntegrityViolationException() {
        WeaponType weaponType = new WeaponType(1, "WeaponType 1", "Description 1");
        WeaponType weaponTypeWithDuplicatedName = new WeaponType(2, "WeaponType 1", "Description 2");
        when(weaponTypeRepository.saveAndFlush(weaponType)).thenReturn(weaponType);
        when(weaponTypeRepository.saveAndFlush(weaponTypeWithDuplicatedName)).thenThrow(DataIntegrityViolationException.class);

        assertDoesNotThrow(() -> weaponTypeService.saveWeaponType(weaponType));
        assertThrows(DataIntegrityViolationException.class, () -> weaponTypeService.saveWeaponType(weaponTypeWithDuplicatedName));
    }

    @Test
    public void whenWeaponTypeIdExists_deleteWeaponType_DeletesTheWeaponType() {
        int weaponTypeId = 1;
        when(weaponTypeRepository.existsById(weaponTypeId)).thenReturn(true);
        assertDoesNotThrow(() -> weaponTypeService.deleteWeaponType(weaponTypeId));
        verify(weaponTypeRepository, times(1)).deleteById(weaponTypeId);
    }

    @Test
    public void whenWeaponTypeIdDoesNotExist_deleteWeaponType_ThrowsIllegalArgumentException() {
        int weaponTypeId = 9000;
        when(weaponTypeRepository.existsById(weaponTypeId)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> weaponTypeService.deleteWeaponType(weaponTypeId));
    }

//    TODO: fix this test when classes that use WeaponType as a fk are added
//    @Test
//    public void whenWeaponTypeIdIsAForeignKey_deleteWeaponType_ThrowsDataIntegrityViolationException() {
//        int weaponTypeId = 1;
//        Region region = new Region(1, "Region", "Description", 1, weaponTypeId);
//        List<CrudRepository> repositories = new ArrayList<>(Arrays.asList(regionRepository));
//        List<Region> regions = new ArrayList<>(Arrays.asList(region));
//
//        when(weaponTypeRepository.existsById(weaponTypeId)).thenReturn(true);
//        when(regionRepository.findByfk_weaponType(weaponTypeId)).thenReturn(regions);
//
//        boolean actual = RepositoryHelper.isForeignKey(repositories, FK_WEAPONTYPE.columnName, weaponTypeId);
//        Assertions.assertTrue(actual);
//        assertThrows(DataIntegrityViolationException.class, () -> weaponTypeService.deleteWeaponType(weaponTypeId));
//    }

    @Test
    public void whenWeaponTypeIdIsFound_updateWeaponType_UpdatesTheWeaponType() {
        int weaponTypeId = 1;
        WeaponType weaponType = new WeaponType(weaponTypeId, "Old WeaponType Name", "Old Description");
        WeaponType weaponTypeToUpdate = new WeaponType(weaponTypeId, "Updated WeaponType Name", "Updated Description");

        when(weaponTypeRepository.existsById(weaponTypeId)).thenReturn(true);
        when(weaponTypeRepository.findById(weaponTypeId)).thenReturn(Optional.of(weaponType));

        weaponTypeService.updateWeaponType(weaponTypeId, weaponTypeToUpdate);

        verify(weaponTypeRepository).findById(weaponTypeId);

        WeaponType result = weaponTypeRepository.findById(weaponTypeId).get();
        Assertions.assertEquals(weaponTypeToUpdate.getName(), result.getName());
        Assertions.assertEquals(weaponTypeToUpdate.getDescription(), result.getDescription());
    }

    @Test
    public void whenWeaponTypeIdIsNotFound_updateWeaponType_ThrowsIllegalArgumentException() {
        int weaponTypeId = 1;
        WeaponType weaponType = new WeaponType(weaponTypeId, "Old WeaponType Name", "Old Description");

        when(weaponTypeRepository.existsById(weaponTypeId)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> weaponTypeService.updateWeaponType(weaponTypeId, weaponType));
    }
}
