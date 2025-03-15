package com.mcommings.campaigner.services.admin;

import com.mcommings.campaigner.modules.admin.entities.Permission;
import com.mcommings.campaigner.modules.admin.entities.PermissionType;
import com.mcommings.campaigner.modules.admin.repositories.IPermissionRepository;
import com.mcommings.campaigner.modules.admin.repositories.IPermissionTypeRepository;
import com.mcommings.campaigner.modules.admin.services.PermissionTypeService;
import com.mcommings.campaigner.modules.common.entities.RepositoryHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.CrudRepository;

import java.util.*;

import static com.mcommings.campaigner.enums.ForeignKey.FK_PERMISSION_TYPE;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class PermissionTypeTest {

    @Mock
    private IPermissionTypeRepository permissionTypeRepository;

    @Mock
    private IPermissionRepository permissionRepository;

    @InjectMocks
    private PermissionTypeService permissionTypeService;

    @Test
    public void whenThereArePermissionTypes_getPermissionTypes_ReturnsPermissionTypes() {
        List<PermissionType> permissionTypes = new ArrayList<>();
        permissionTypes.add(new PermissionType(1, "PermissionType 1", "Description 1"));
        permissionTypes.add(new PermissionType(2, "PermissionType 2", "Description 2"));
        permissionTypes.add(new PermissionType(3, "PermissionType 3", "Description 3"));
        when(permissionTypeRepository.findAll()).thenReturn(permissionTypes);

        List<PermissionType> result = permissionTypeService.getPermissionTypes();

        Assertions.assertEquals(3, result.size());
        Assertions.assertEquals(permissionTypes, result);
    }

    @Test
    public void whenThereAreNoPermissionTypes_getPermissionTypes_ReturnsNothing() {
        List<PermissionType> permissionTypes = new ArrayList<>();
        when(permissionTypeRepository.findAll()).thenReturn(permissionTypes);

        List<PermissionType> result = permissionTypeService.getPermissionTypes();

        Assertions.assertEquals(0, result.size());
        Assertions.assertEquals(permissionTypes, result);
    }

    @Test
    public void whenPermissionTypeIsValid_savePermissionType_SavesThePermissionType() {
        PermissionType permissionType = new PermissionType(1, "PermissionType 1", "Description 1");
        when(permissionTypeRepository.saveAndFlush(permissionType)).thenReturn(permissionType);

        assertDoesNotThrow(() -> permissionTypeService.savePermissionType(permissionType));

        verify(permissionTypeRepository, times(1)).saveAndFlush(permissionType);
    }

    @Test
    public void whenPermissionTypeNameIsInvalid_savePermissionType_ThrowsIllegalArgumentException() {
        PermissionType permissionTypeWithEmptyName = new PermissionType(1, "", "Description 1");
        PermissionType permissionTypeWithNullName = new PermissionType(2, null, "Description 2");

        assertThrows(IllegalArgumentException.class, () -> permissionTypeService.savePermissionType(permissionTypeWithEmptyName));
        assertThrows(IllegalArgumentException.class, () -> permissionTypeService.savePermissionType(permissionTypeWithNullName));
    }

    @Test
    public void whenPermissionTypeNameAlreadyExists_savePermissionType_ThrowsDataIntegrityViolationException() {
        PermissionType permissionType = new PermissionType(1, "PermissionType 1", "Description 1");
        PermissionType permissionTypeWithDuplicatedName = new PermissionType(2, "PermissionType 1", "Description 2");

        when(permissionTypeRepository.existsById(1)).thenReturn(true);
        when(permissionTypeRepository.existsById(2)).thenReturn(true);

        when(permissionTypeRepository.saveAndFlush(permissionType)).thenReturn(permissionType);
        when(permissionTypeRepository.saveAndFlush(permissionTypeWithDuplicatedName)).thenThrow(DataIntegrityViolationException.class);

        assertDoesNotThrow(() -> permissionTypeService.savePermissionType(permissionType));
        assertThrows(DataIntegrityViolationException.class, () -> permissionTypeService.savePermissionType(permissionTypeWithDuplicatedName));
    }

    @Test
    public void whenPermissionTypeIdExists_deletePermissionType_DeletesThePermissionType() {
        int permissionTypeId = 1;
        when(permissionTypeRepository.existsById(permissionTypeId)).thenReturn(true);
        assertDoesNotThrow(() -> permissionTypeService.deletePermissionType(permissionTypeId));
        verify(permissionTypeRepository, times(1)).deleteById(permissionTypeId);
    }

    @Test
    public void whenPermissionTypeIdDoesNotExist_deletePermissionType_ThrowsIllegalArgumentException() {
        int permissionTypeId = 9000;
        when(permissionTypeRepository.existsById(permissionTypeId)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> permissionTypeService.deletePermissionType(permissionTypeId));
    }

    @Test
    public void whenPermissionTypeIdIsAForeignKey_deletePermissionType_ThrowsDataIntegrityViolationException() {
        int permissionTypeId = 1;
        Permission permission = new Permission(1, 1, UUID.randomUUID(), UUID.randomUUID());
        List<CrudRepository> repositories = new ArrayList<>(Arrays.asList(permissionRepository));
        List<Permission> permissions = new ArrayList<>(Arrays.asList(permission));

        when(permissionTypeRepository.existsById(permissionTypeId)).thenReturn(true);
        when(permissionRepository.findByfk_permission_type(permissionTypeId)).thenReturn(permissions);

        boolean actual = RepositoryHelper.isForeignKey(repositories, FK_PERMISSION_TYPE.columnName, permissionTypeId);
        Assertions.assertTrue(actual);
        assertThrows(DataIntegrityViolationException.class, () -> permissionTypeService.deletePermissionType(permissionTypeId));
    }

    @Test
    public void whenPermissionTypeIdIsFound_updatePermissionType_UpdatesThePermissionType() {
        int permissionTypeId = 1;

        PermissionType permissionType = new PermissionType(permissionTypeId, "Old PermissionType Name", "Old Description");
        PermissionType permissionTypeToUpdate = new PermissionType(permissionTypeId, "Updated PermissionType Name", "Updated Description");

        when(permissionTypeRepository.existsById(permissionTypeId)).thenReturn(true);
        when(permissionTypeRepository.findById(permissionTypeId)).thenReturn(Optional.of(permissionType));

        permissionTypeService.updatePermissionType(permissionTypeId, permissionTypeToUpdate);

        verify(permissionTypeRepository).findById(permissionTypeId);

        PermissionType result1 = permissionTypeRepository.findById(permissionTypeId).get();
        Assertions.assertEquals(permissionTypeToUpdate.getName(), result1.getName());
        Assertions.assertEquals(permissionTypeToUpdate.getDescription(), result1.getDescription());
    }

    @Test
    public void whenPermissionTypeIdIsNotFound_updatePermissionType_ThrowsIllegalArgumentException() {
        int permissionTypeId = 1;
        PermissionType permissionType = new PermissionType(permissionTypeId, "Old PermissionType Name", "Old Description");

        when(permissionTypeRepository.existsById(permissionTypeId)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> permissionTypeService.updatePermissionType(permissionTypeId, permissionType));
    }
}
