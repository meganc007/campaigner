package com.mcommings.campaigner.services.admin;

import com.mcommings.campaigner.entities.RepositoryHelper;
import com.mcommings.campaigner.entities.admin.PermissionType;
import com.mcommings.campaigner.interfaces.admin.IPermissionType;
import com.mcommings.campaigner.repositories.admin.IPermissionRepository;
import com.mcommings.campaigner.repositories.admin.IPermissionTypeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mcommings.campaigner.enums.ErrorMessage.*;
import static com.mcommings.campaigner.enums.ForeignKey.FK_PERMISSION_TYPE;

@Service
public class PermissionTypeService implements IPermissionType {

    private final IPermissionTypeRepository permissionTypeRepository;
    private final IPermissionRepository permissionRepository;

    @Autowired
    public PermissionTypeService(IPermissionTypeRepository permissionTypeRepository, IPermissionRepository permissionRepository) {
        this.permissionTypeRepository = permissionTypeRepository;
        this.permissionRepository = permissionRepository;
    }

    @Override
    public List<PermissionType> getPermissionTypes() {
        return permissionTypeRepository.findAll();
    }

    @Override
    @Transactional
    public void savePermissionType(PermissionType permissionType) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.nameIsNullOrEmpty(permissionType)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (RepositoryHelper.nameAlreadyExists(permissionTypeRepository, permissionType)) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }

        permissionTypeRepository.saveAndFlush(permissionType);
    }

    @Override
    @Transactional
    public void deletePermissionType(int permissionTypeId) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(permissionTypeRepository, permissionTypeId)) {
            throw new IllegalArgumentException(DELETE_NOT_FOUND.message);
        }
        if (RepositoryHelper.isForeignKey(getReposWherePermissionTypeIsAForeignKey(), FK_PERMISSION_TYPE.columnName, permissionTypeId)) {
            throw new DataIntegrityViolationException(DELETE_FOREIGN_KEY.message);
        }
        permissionTypeRepository.deleteById(permissionTypeId);
    }

    @Override
    @Transactional
    public void updatePermissionType(int id, PermissionType permissionType) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(permissionTypeRepository, id)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        PermissionType permissionTypeToUpdate = RepositoryHelper.getById(permissionTypeRepository, id);
        permissionTypeToUpdate.setName(permissionType.getName());
        permissionTypeToUpdate.setDescription(permissionType.getDescription());
    }

    private List<CrudRepository> getReposWherePermissionTypeIsAForeignKey() {
        return new ArrayList<>(Arrays.asList(permissionRepository));
    }
}
