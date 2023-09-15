package com.mcommings.campaigner.services.admin;

import com.mcommings.campaigner.models.admin.User;
import com.mcommings.campaigner.repositories.admin.IUserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserTest {

    @Mock
    private IUserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void whenThereAreUsers_getUsers_ReturnsUsers() {
        List<User> users = new ArrayList<>();
        users.add(new User("yoozername", "name@email.com", "Jane", "Doe", "DM"));
        users.add(new User("uzernom", "name@email.com", "Jack", "Doe", "DM"));

        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getUsers();

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(users, result);
    }

    @Test
    public void whenThereAreNoUsers_getUsers_ReturnsNothing() {
        List<User> users = new ArrayList<>();
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getUsers();

        Assertions.assertEquals(0, result.size());
        Assertions.assertEquals(users, result);
    }

    @Test
    public void whenUserIsValid_saveUser_SavesTheUser() {
        User user = new User("yoozername", "name@email.com", "Jane", "Doe", "DM");
        when(userRepository.saveAndFlush(user)).thenReturn(user);

        assertDoesNotThrow(() -> userService.saveUser(user));
        verify(userRepository, times(1)).saveAndFlush(user);
    }

    @Test
    public void whenUserNamesInvalid_saveUser_ThrowsIllegalArgumentException() {
        User userWithEmptyFirstName = new User("yoozername", "name@email.com", "", "Doe", "DM");
        User userWithEmptyLastName = new User("yoozername", "name@email.com", "Jane", "", "DM");

        User userWithNullLastName = new User("yoozername", "name@email.com", null, "Doe", "DM");
        User userWithNullFirstName = new User("yoozername", "name@email.com", "Jane", null, "DM");

        assertThrows(IllegalArgumentException.class, () -> userService.saveUser(userWithEmptyFirstName));
        assertThrows(IllegalArgumentException.class, () -> userService.saveUser(userWithEmptyLastName));
        assertThrows(IllegalArgumentException.class, () -> userService.saveUser(userWithNullFirstName));
        assertThrows(IllegalArgumentException.class, () -> userService.saveUser(userWithNullLastName));
    }

    @Test
    public void whenUserNameAlreadyExists_saveUser_ThrowsDataIntegrityViolationException() {
        User user = new User("yoozername", "name@email.com", "Jane", "Doe", "DM");
        User userWithDuplicatedName = new User("yoozername", "name@email.com", "Jane", "Doe", "DM");
        when(userRepository.saveAndFlush(user)).thenReturn(user);
        when(userRepository.saveAndFlush(userWithDuplicatedName)).thenThrow(DataIntegrityViolationException.class);

        assertDoesNotThrow(() -> userService.saveUser(user));
        assertThrows(DataIntegrityViolationException.class, () -> userService.saveUser(userWithDuplicatedName));
    }

    @Test
    public void whenUserIdExists_deleteUser_DeletesTheUser() {
        UUID uuid = UUID.randomUUID();
        User user = new User("yoozername", "name@email.com", "Jane", "Doe", "DM");
        user.setUuid(uuid);

        when(userRepository.existsByUuid(uuid)).thenReturn(true);
        when(userRepository.findByUuid(uuid)).thenReturn(Optional.of(user));

        assertDoesNotThrow(() -> userService.deleteUser(uuid));
        verify(userRepository, times(1)).deleteByUuid(uuid);
    }

    @Test
    public void whenUserIdDoesNotExist_deleteUser_ThrowsIllegalArgumentException() {
        UUID uuid = UUID.randomUUID();
        when(userRepository.existsByUuid(uuid)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> userService.deleteUser(uuid));
    }

    @Test
    public void whenUserIdIsFound_updateUser_UpdatesTheUser() {
        UUID uuid = UUID.randomUUID();
        User user = new User("yoozername", "name@email.com", "Jane", "Doe", "DM");
        user.setUuid(uuid);

        String newFirstName = "Jack";
        String newLastName = "Deer";

        User userToUpdate = new User("yoozername", "name@email.com", newFirstName, newLastName, "DM");

        when(userRepository.existsByUuid(uuid)).thenReturn(true);
        when(userRepository.findByUuid(uuid)).thenReturn(Optional.of(user));

        userService.updateUser(uuid, userToUpdate);

        verify(userRepository, times(2)).findByUuid(uuid);

        User result = userRepository.findByUuid(uuid).get();
        Assertions.assertEquals(userToUpdate.getUsername(), result.getUsername());
        Assertions.assertEquals(userToUpdate.getEmail(), result.getEmail());
        Assertions.assertEquals(newFirstName, result.getFirstName());
        Assertions.assertEquals(newLastName, result.getLastName());
        Assertions.assertEquals(userToUpdate.getRole(), result.getRole());

    }

    @Test
    public void whenUserIdIsNotFound_updateUser_ThrowsIllegalArgumentException() {
        UUID uuid = UUID.randomUUID();
        User user = new User("yoozername", "name@email.com", "Jane", "Doe", "DM");
        user.setUuid(uuid);

        when(userRepository.existsByUuid(uuid)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> userService.updateUser(uuid, user));
    }

    @Test
    public void whenSomeUserFieldsChanged_updateUser_OnlyUpdatesChangedFields() {
        UUID uuid = UUID.randomUUID();
        User user = new User("yoozername", "name@email.com", "Jane", "Doe", "DM");
        user.setUuid(uuid);
        User userToUpdate = new User();
        userToUpdate.setUsername("asdfjkl;");

        when(userRepository.existsByUuid(uuid)).thenReturn(true);
        when(userRepository.findByUuid(uuid)).thenReturn(Optional.of(user));

        userService.updateUser(uuid, userToUpdate);

        verify(userRepository, times(2)).findByUuid(uuid);

        User result = userRepository.findByUuid(uuid).get();
        Assertions.assertEquals("asdfjkl;", result.getUsername());
        Assertions.assertEquals(user.getEmail(), result.getEmail());
        Assertions.assertEquals(user.getFirstName(), result.getFirstName());
        Assertions.assertEquals(user.getLastName(), result.getLastName());
        Assertions.assertEquals(user.getRole(), result.getRole());
    }
}
