package com.mcommings.campaigner.admin.services.interfaces;

import com.mcommings.campaigner.admin.entities.Permission;

import java.util.List;

public interface IPermission {

    List<Permission> getPermissions();

    void savePermission(Permission permission);

    void deletePermission(int id);

    void updatePermission(int id, Permission permission);
}
