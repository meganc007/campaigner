package com.mcommings.campaigner.admin.services.interfaces;

import com.mcommings.campaigner.admin.entities.User;

import java.util.List;
import java.util.UUID;

public interface IUser {

    List<User> getUsers();

    void saveUser(User user);

    void deleteUser(UUID uuid);

    void updateUser(UUID uuid, User user);
}
