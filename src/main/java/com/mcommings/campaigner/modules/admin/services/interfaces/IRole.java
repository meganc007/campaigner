package com.mcommings.campaigner.modules.admin.services.interfaces;

import com.mcommings.campaigner.modules.admin.entities.Role;

import java.util.List;

public interface IRole {

    List<Role> getRoles();

    void saveRole(Role role);

    void deleteRole(int roleId);

    void updateRole(int roleId, Role role);
}
