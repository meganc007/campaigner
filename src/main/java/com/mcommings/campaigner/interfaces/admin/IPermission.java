package com.mcommings.campaigner.interfaces.admin;

import com.mcommings.campaigner.entities.admin.Permission;

import java.util.List;

public interface IPermission {

    List<Permission> getPermissions();

    void savePermission(Permission permission);

    void deletePermission(int id);

    void updatePermission(int id, Permission permission);
}
