package com.mcommings.campaigner.services.admin;

import com.mcommings.campaigner.interfaces.admin.IUser;
import com.mcommings.campaigner.models.admin.User;
import com.mcommings.campaigner.repositories.admin.IUserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.mcommings.campaigner.enums.ErrorMessage.*;
import static java.util.Objects.isNull;

@Service
public class UserService implements IUser {

    private final IUserRepository userRepository;

    @Autowired
    public UserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public void saveUser(User user) throws IllegalArgumentException, DataIntegrityViolationException {
        if (nameIsNullOrEmpty(user.getFirstName()) || nameIsNullOrEmpty(user.getLastName())) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (userAlreadyExists(user)) {
            throw new DataIntegrityViolationException(USER_EXISTS.message);
        }

        userRepository.saveAndFlush(user);
    }

    @Override
    @Transactional
    public void deleteUser(UUID uuid) throws IllegalArgumentException, DataIntegrityViolationException {
        if (cannotFindUuid(uuid)) {
            throw new IllegalArgumentException(DELETE_NOT_FOUND.message);
        }
        userRepository.deleteByUuid(uuid);
    }

    @Override
    @Transactional
    public void updateUser(UUID uuid, User user) throws IllegalArgumentException, DataIntegrityViolationException {
        if (cannotFindUuid(uuid)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        User userToUpdate = getByUuid(uuid);
        if (user.getUsername() != null) userToUpdate.setUsername(user.getUsername());
        if (user.getEmail() != null) userToUpdate.setEmail(user.getEmail());
        if (user.getFirstName() != null) userToUpdate.setFirstName(user.getFirstName());
        if (user.getLastName() != null) userToUpdate.setLastName(user.getLastName());
        if (user.getRole() != null) userToUpdate.setRole(user.getRole());
    }

    private boolean nameIsNullOrEmpty(String name) {
        return isNull(name) || name.isEmpty();
    }

    private boolean userAlreadyExists(User user) {
        return userRepository.userExists(user).isPresent();
    }

    private boolean cannotFindUuid(UUID uuid) {
        return !userRepository.findByUuid(uuid).isPresent();
    }

    private User getByUuid(UUID uuid) {
        return userRepository.findByUuid(uuid).get();
    }
}
