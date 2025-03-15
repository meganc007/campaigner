package com.mcommings.campaigner.admin.services.interfaces;

import com.mcommings.campaigner.admin.entities.Role;

import java.util.List;

public interface IRole {

    List<Role> getRoles();

    void saveRole(Role role);

    void deleteRole(int roleId);

    void updateRole(int roleId, Role role);
}
