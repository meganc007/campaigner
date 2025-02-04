package com.mcommings.campaigner.interfaces.admin;

import com.mcommings.campaigner.entities.admin.User;

import java.util.List;
import java.util.UUID;

public interface IUser {

    List<User> getUsers();

    void saveUser(User user);

    void deleteUser(UUID uuid);

    void updateUser(UUID uuid, User user);
}
