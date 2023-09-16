package com.mcommings.campaigner.services.admin;

import com.mcommings.campaigner.interfaces.admin.IPermission;
import com.mcommings.campaigner.models.RepositoryHelper;
import com.mcommings.campaigner.models.admin.Permission;
import com.mcommings.campaigner.repositories.ICampaignRepository;
import com.mcommings.campaigner.repositories.admin.IPermissionRepository;
import com.mcommings.campaigner.repositories.admin.IUserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.mcommings.campaigner.enums.ErrorMessage.*;

@Service
public class PermissionService implements IPermission {

    private final IPermissionRepository permissionRepository;
    private final ICampaignRepository campaignRepository;
    private final IUserRepository userRepository;

    @Autowired
    public PermissionService(IPermissionRepository permissionRepository, ICampaignRepository campaignRepository,
                             IUserRepository userRepository) {
        this.permissionRepository = permissionRepository;
        this.campaignRepository = campaignRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Permission> getPermissions() {
        return permissionRepository.findAll();
    }

    @Override
    @Transactional
    public void savePermission(Permission permission) throws IllegalArgumentException, DataIntegrityViolationException {
        if (permissionRepository.permissionAlreadyExists(permission).isPresent()) {
            throw new DataIntegrityViolationException(PERMISSION_EXISTS.message);
        }
        if (hasForeignKeys(permission) && campaignDoesNotExist(permission) &&
                userDoesNotExist(permission)) {
            throw new DataIntegrityViolationException(INSERT_FOREIGN_KEY.message);
        }

        permissionRepository.saveAndFlush(permission);
    }

    @Override
    @Transactional
    public void deletePermission(int permissionId) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(permissionRepository, permissionId)) {
            throw new IllegalArgumentException(DELETE_NOT_FOUND.message);
        }
        permissionRepository.deleteById(permissionId);
    }

    @Override
    @Transactional
    public void updatePermission(int permissionId, Permission permission) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(permissionRepository, permissionId)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        if (hasForeignKeys(permission) && campaignDoesNotExist(permission) &&
                userDoesNotExist(permission)) {
            throw new DataIntegrityViolationException(UPDATE_FOREIGN_KEY.message);
        }
        Permission permissionToUpdate = RepositoryHelper.getById(permissionRepository, permissionId);
        if (permission.getPermission() != null) permissionToUpdate.setPermission(permission.getPermission());
        if (permission.getFk_campaign_uuid() != null)
            permissionToUpdate.setFk_campaign_uuid(permission.getFk_campaign_uuid());
        if (permission.getFk_user_uuid() != null) permissionToUpdate.setFk_user_uuid(permission.getFk_user_uuid());
    }

    private boolean hasForeignKeys(Permission permission) {
        return permission.getPermission() != null ||
                permission.getFk_campaign_uuid() != null ||
                permission.getFk_user_uuid() != null;
    }

    private boolean campaignDoesNotExist(Permission permission) {
        return !campaignRepository.existsByUuid(permission.getFk_campaign_uuid());
    }

    private boolean userDoesNotExist(Permission permission) {
        return !userRepository.existsByUuid(permission.getFk_user_uuid());
    }
}
