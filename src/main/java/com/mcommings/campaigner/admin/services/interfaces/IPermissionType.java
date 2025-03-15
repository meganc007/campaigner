package com.mcommings.campaigner.admin.services.interfaces;

import com.mcommings.campaigner.admin.entities.PermissionType;

import java.util.List;

public interface IPermissionType {

    List<PermissionType> getPermissionTypes();

    void savePermissionType(PermissionType permissionType);

    void deletePermissionType(int id);

    void updatePermissionType(int id, PermissionType permissionType);
}
