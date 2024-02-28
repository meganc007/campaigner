package com.mcommings.campaigner.services.admin;

import com.mcommings.campaigner.interfaces.admin.IRole;
import com.mcommings.campaigner.models.RepositoryHelper;
import com.mcommings.campaigner.models.admin.Role;
import com.mcommings.campaigner.repositories.admin.IRoleRepository;
import com.mcommings.campaigner.repositories.admin.IUserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mcommings.campaigner.enums.ErrorMessage.*;
import static com.mcommings.campaigner.enums.ForeignKey.FK_ROLE;

@Service
public class RoleService implements IRole {

    private final IRoleRepository roleRepository;
    private final IUserRepository userRepository;

    @Autowired
    public RoleService(IRoleRepository roleRepository, IUserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    @Override
    @Transactional
    public void saveRole(Role role) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.nameIsNullOrEmpty(role)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (RepositoryHelper.nameAlreadyExists(roleRepository, role)) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }

        roleRepository.saveAndFlush(role);
    }

    @Override
    @Transactional
    public void deleteRole(int roleId) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(roleRepository, roleId)) {
            throw new IllegalArgumentException(DELETE_NOT_FOUND.message);
        }
        if (RepositoryHelper.isForeignKey(getReposWhereRoleIsAForeignKey(), FK_ROLE.columnName, roleId)) {
            throw new DataIntegrityViolationException(DELETE_FOREIGN_KEY.message);
        }
        roleRepository.deleteById(roleId);
    }

    @Override
    @Transactional
    public void updateRole(int roleId, Role role) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(roleRepository, roleId)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        Role roleToUpdate = RepositoryHelper.getById(roleRepository, roleId);
        roleToUpdate.setName(role.getName());
        roleToUpdate.setDescription(role.getDescription());
    }

    private List<CrudRepository> getReposWhereRoleIsAForeignKey() {
        return new ArrayList<>(Arrays.asList(userRepository));
    }
}
