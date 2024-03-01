package com.mcommings.campaigner.services.admin;

import com.mcommings.campaigner.models.RepositoryHelper;
import com.mcommings.campaigner.models.admin.Role;
import com.mcommings.campaigner.models.admin.User;
import com.mcommings.campaigner.repositories.admin.IRoleRepository;
import com.mcommings.campaigner.repositories.admin.IUserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.mcommings.campaigner.enums.ForeignKey.FK_ROLE;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class RoleTest {

    @Mock
    private IRoleRepository roleRepository;

    @Mock
    private IUserRepository userRepository;

    @InjectMocks
    private RoleService roleService;

    @Test
    public void whenThereAreRoles_getRoles_ReturnsRoles() {
        List<Role> roles = new ArrayList<>();
        roles.add(new Role(1, "Role 1", "Description 1"));
        roles.add(new Role(2, "Role 2", "Description 2"));
        roles.add(new Role(3, "Role 3", "Description 3"));
        when(roleRepository.findAll()).thenReturn(roles);

        List<Role> result = roleService.getRoles();

        Assertions.assertEquals(3, result.size());
        Assertions.assertEquals(roles, result);
    }

    @Test
    public void whenThereAreNoRoles_getRoles_ReturnsNothing() {
        List<Role> roles = new ArrayList<>();
        when(roleRepository.findAll()).thenReturn(roles);

        List<Role> result = roleService.getRoles();

        Assertions.assertEquals(0, result.size());
        Assertions.assertEquals(roles, result);
    }

    @Test
    public void whenRoleIsValid_saveRole_SavesTheRole() {
        Role role = new Role(1, "Role 1", "Description 1");
        when(roleRepository.saveAndFlush(role)).thenReturn(role);

        assertDoesNotThrow(() -> roleService.saveRole(role));

        verify(roleRepository, times(1)).saveAndFlush(role);
    }

    @Test
    public void whenRoleNameIsInvalid_saveRole_ThrowsIllegalArgumentException() {
        Role roleWithEmptyName = new Role(1, "", "Description 1");
        Role roleWithNullName = new Role(2, null, "Description 2");

        assertThrows(IllegalArgumentException.class, () -> roleService.saveRole(roleWithEmptyName));
        assertThrows(IllegalArgumentException.class, () -> roleService.saveRole(roleWithNullName));
    }

    @Test
    public void whenRoleNameAlreadyExists_saveRole_ThrowsDataIntegrityViolationException() {
        Role role = new Role(1, "Role 1", "Description 1");
        Role roleWithDuplicatedName = new Role(2, "Role 1", "Description 2");

        when(roleRepository.existsById(1)).thenReturn(true);
        when(roleRepository.existsById(2)).thenReturn(true);

        when(roleRepository.saveAndFlush(role)).thenReturn(role);
        when(roleRepository.saveAndFlush(roleWithDuplicatedName)).thenThrow(DataIntegrityViolationException.class);

        assertDoesNotThrow(() -> roleService.saveRole(role));
        assertThrows(DataIntegrityViolationException.class, () -> roleService.saveRole(roleWithDuplicatedName));
    }

    @Test
    public void whenRoleIdExists_deleteRole_DeletesTheRole() {
        int roleId = 1;
        when(roleRepository.existsById(roleId)).thenReturn(true);
        assertDoesNotThrow(() -> roleService.deleteRole(roleId));
        verify(roleRepository, times(1)).deleteById(roleId);
    }

    @Test
    public void whenRoleIdDoesNotExist_deleteRole_ThrowsIllegalArgumentException() {
        int roleId = 9000;
        when(roleRepository.existsById(roleId)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> roleService.deleteRole(roleId));
    }

    @Test
    public void whenRoleIdIsAForeignKey_deleteRole_ThrowsDataIntegrityViolationException() {
        int roleId = 1;
        User user = new User("yoozername", "name@email.com", "Jane", "Doe", roleId);
        List<CrudRepository> repositories = new ArrayList<>(Arrays.asList(userRepository));
        List<User> users = new ArrayList<>(Arrays.asList(user));

        when(roleRepository.existsById(roleId)).thenReturn(true);
        when(userRepository.findByfk_role(roleId)).thenReturn(users);

        boolean actual = RepositoryHelper.isForeignKey(repositories, FK_ROLE.columnName, roleId);
        Assertions.assertTrue(actual);
        assertThrows(DataIntegrityViolationException.class, () -> roleService.deleteRole(roleId));
    }

    @Test
    public void whenRoleIdIsFound_updateRole_UpdatesTheRole() {
        int roleId = 1;

        Role role = new Role(roleId, "Old Role Name", "Old Description");
        Role roleToUpdate = new Role(roleId, "Updated Role Name", "Updated Description");

        when(roleRepository.existsById(roleId)).thenReturn(true);
        when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));

        roleService.updateRole(roleId, roleToUpdate);

        verify(roleRepository).findById(roleId);

        Role result1 = roleRepository.findById(roleId).get();
        Assertions.assertEquals(roleToUpdate.getName(), result1.getName());
        Assertions.assertEquals(roleToUpdate.getDescription(), result1.getDescription());
    }

    @Test
    public void whenRoleIdIsNotFound_updateRole_ThrowsIllegalArgumentException() {
        int roleId = 1;
        Role role = new Role(roleId, "Old Role Name", "Old Description");

        when(roleRepository.existsById(roleId)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> roleService.updateRole(roleId, role));
    }
}
