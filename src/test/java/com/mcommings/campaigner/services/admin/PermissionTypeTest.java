package com.mcommings.campaigner.services.admin;

import com.mcommings.campaigner.models.admin.PermissionType;
import com.mcommings.campaigner.repositories.admin.IPermissionRepository;
import com.mcommings.campaigner.repositories.admin.IPermissionTypeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.ArrayList;
import java.util.List;

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
}
