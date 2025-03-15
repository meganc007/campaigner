package com.mcommings.campaigner.services.admin;

import com.mcommings.campaigner.common.repositories.ICampaignRepository;
import com.mcommings.campaigner.entities.admin.Permission;
import com.mcommings.campaigner.repositories.admin.IPermissionRepository;
import com.mcommings.campaigner.repositories.admin.IUserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class PermissionTest {

    @Mock
    private IPermissionRepository permissionRepository;
    @Mock
    private ICampaignRepository campaignRepository;
    @Mock
    private IUserRepository userRepository;

    @InjectMocks
    private PermissionService permissionService;

    @Test
    public void whenThereArePermissions_getPermissions_ReturnsPermissions() {
        List<Permission> permissions = new ArrayList<>();
        UUID campaignUuid = UUID.randomUUID();
        UUID userUuid = UUID.randomUUID();
        permissions.add(new Permission(1, 1, campaignUuid, userUuid));
        permissions.add(new Permission(2, 2, campaignUuid, userUuid));
        permissions.add(new Permission(3, 3, campaignUuid, userUuid));
        when(permissionRepository.findAll()).thenReturn(permissions);

        List<Permission> result = permissionService.getPermissions();

        Assertions.assertEquals(3, result.size());
        Assertions.assertEquals(permissions, result);
    }

    @Test
    public void whenThereAreNoPermissions_getPermissions_ReturnsNothing() {
        List<Permission> permissions = new ArrayList<>();
        when(permissionRepository.findAll()).thenReturn(permissions);

        List<Permission> result = permissionService.getPermissions();

        Assertions.assertEquals(0, result.size());
        Assertions.assertEquals(permissions, result);
    }

    @Test
    public void whenPermissionIsValid_savePermission_SavesThePermission() {
        UUID campaignUuid = UUID.randomUUID();
        UUID userUuid = UUID.randomUUID();
        Permission permission = new Permission(1, 1, campaignUuid, userUuid);

        when(campaignRepository.existsByUuid(campaignUuid)).thenReturn(true);
        when(userRepository.existsByUuid(userUuid)).thenReturn(true);
        when(permissionRepository.saveAndFlush(permission)).thenReturn(permission);

        assertDoesNotThrow(() -> permissionService.savePermission(permission));

        verify(permissionRepository, times(1)).saveAndFlush(permission);
    }

    @Test
    public void whenPermissionAlreadyExists_savePermission_ThrowsDataIntegrityViolationException() {
        UUID campaignUuid = UUID.randomUUID();
        UUID userUuid = UUID.randomUUID();

        Permission permission = new Permission(1, 1, campaignUuid, userUuid);
        Permission permissionCopy = new Permission(2, 1, campaignUuid, userUuid);

        when(campaignRepository.existsByUuid(campaignUuid)).thenReturn(true);
        when(userRepository.existsByUuid(userUuid)).thenReturn(true);

        when(permissionRepository.saveAndFlush(permission)).thenReturn(permission);
        when(permissionRepository.saveAndFlush(permissionCopy)).thenThrow(DataIntegrityViolationException.class);

        assertDoesNotThrow(() -> permissionService.savePermission(permission));
        assertThrows(DataIntegrityViolationException.class, () -> permissionService.savePermission(permissionCopy));
    }

    @Test
    public void whenPermissionIdExists_deletePermission_DeletesThePermission() {
        int permissionId = 1;
        when(permissionRepository.existsById(permissionId)).thenReturn(true);
        assertDoesNotThrow(() -> permissionService.deletePermission(permissionId));
        verify(permissionRepository, times(1)).deleteById(permissionId);
    }

    @Test
    public void whenPermissionIdDoesNotExist_deletePermission_ThrowsIllegalArgumentException() {
        int permissionId = 9000;
        when(permissionRepository.existsById(permissionId)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> permissionService.deletePermission(permissionId));
    }

    @Test
    public void whenPermissionIdWithValidFKIsFound_updatePermission_UpdatesThePermission() {
        int permissionId = 1;
        UUID campaignUuid = UUID.randomUUID();
        UUID userUuid = UUID.randomUUID();

        UUID updateCampaignUuid = UUID.randomUUID();
        UUID updateUserUuid = UUID.randomUUID();

        Permission permission = new Permission(permissionId, 1, campaignUuid, userUuid);
        Permission update = new Permission(permissionId, 1, updateCampaignUuid, updateUserUuid);

        when(permissionRepository.existsById(permissionId)).thenReturn(true);
        when(permissionRepository.findById(permissionId)).thenReturn(Optional.of(permission));

        when(campaignRepository.existsByUuid(campaignUuid)).thenReturn(true);
        when(userRepository.existsByUuid(userUuid)).thenReturn(true);

        when(campaignRepository.existsByUuid(updateCampaignUuid)).thenReturn(true);
        when(userRepository.existsByUuid(updateUserUuid)).thenReturn(true);

        permissionService.updatePermission(permissionId, update);

        verify(permissionRepository).findById(permissionId);

        Permission result = permissionRepository.findById(permissionId).get();
        Assertions.assertEquals(update.getId(), result.getId());
        Assertions.assertEquals(update.getFk_campaign_uuid(), result.getFk_campaign_uuid());
        Assertions.assertEquals(update.getFk_user_uuid(), result.getFk_user_uuid());
    }

    @Test
    public void whenPermissionIdWithInvalidFKIsFound_updatePermission_ThrowsDataIntegrityViolationException() {
        int permissionId = 1;
        UUID campaignUuid = UUID.randomUUID();
        UUID userUuid = UUID.randomUUID();

        UUID updateCampaignUuid = UUID.randomUUID();
        UUID updateUserUuid = UUID.randomUUID();

        Permission permission = new Permission(permissionId, 1, campaignUuid, userUuid);
        Permission update = new Permission(permissionId, 1, updateCampaignUuid, updateUserUuid);

        when(permissionRepository.existsById(permissionId)).thenReturn(true);
        when(permissionRepository.findById(permissionId)).thenReturn(Optional.of(permission));

        when(campaignRepository.existsByUuid(campaignUuid)).thenReturn(true);
        when(userRepository.existsByUuid(userUuid)).thenReturn(true);
        when(campaignRepository.existsByUuid(updateCampaignUuid)).thenReturn(false);
        when(userRepository.existsByUuid(updateUserUuid)).thenReturn(false);

        assertThrows(DataIntegrityViolationException.class, () -> permissionService.updatePermission(permissionId, update));
    }

    @Test
    public void whenPermissionIdIsNotFound_updatePermission_ThrowsIllegalArgumentException() {
        int permissionId = 1;
        UUID campaignUuid = UUID.randomUUID();
        UUID userUuid = UUID.randomUUID();
        Permission permission = new Permission(permissionId, 1, campaignUuid, userUuid);

        when(permissionRepository.existsById(permissionId)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> permissionService.updatePermission(permissionId, permission));
    }

    @Test
    public void whenSomePermissionFieldsChanged_updatePermission_OnlyUpdatesChangedFields() {
        int permissionId = 1;
        UUID campaignUuid = UUID.randomUUID();
        UUID userUuid = UUID.randomUUID();
        Permission permission = new Permission(permissionId, 1, campaignUuid, userUuid);

        UUID newCampaignUuid = UUID.randomUUID();
        Permission permissionToUpdate = new Permission();
        permissionToUpdate.setFk_campaign_uuid(newCampaignUuid);

        when(permissionRepository.existsById(permissionId)).thenReturn(true);
        when(campaignRepository.existsByUuid(campaignUuid)).thenReturn(true);
        when(campaignRepository.existsByUuid(newCampaignUuid)).thenReturn(true);
        when(userRepository.existsByUuid(userUuid)).thenReturn(true);

        when(permissionRepository.findById(permissionId)).thenReturn(Optional.of(permission));

        permissionService.updatePermission(permissionId, permissionToUpdate);

        verify(permissionRepository).findById(permissionId);

        Permission result = permissionRepository.findById(permissionId).get();
        Assertions.assertEquals(permission.getFk_user_uuid(), result.getFk_user_uuid());
        Assertions.assertEquals(newCampaignUuid, result.getFk_campaign_uuid());
    }
}
