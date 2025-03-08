package com.mcommings.campaigner.interfaces.admin;

import com.mcommings.campaigner.entities.admin.Role;

import java.util.List;

public interface IRole {

    List<Role> getRoles();

    void saveRole(Role role);

    void deleteRole(int roleId);

    void updateRole(int roleId, Role role);
}
