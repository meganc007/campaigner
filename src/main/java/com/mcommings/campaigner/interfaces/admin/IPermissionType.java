package com.mcommings.campaigner.interfaces.admin;

import com.mcommings.campaigner.models.admin.PermissionType;

import java.util.List;

public interface IPermissionType {

    List<PermissionType> getPermissionTypes();

    void savePermissionType(PermissionType permissionType);

    void deletePermissionType(int id);

    void updatePermissionType(int id, PermissionType permissionType);
}
