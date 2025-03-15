package com.mcommings.campaigner.modules.admin.controllers;

import com.mcommings.campaigner.modules.admin.entities.User;
import com.mcommings.campaigner.modules.admin.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "api/admin/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @PostMapping
    public void saveUser(@RequestBody User user) {
        userService.saveUser(user);
    }

    @DeleteMapping(path = "{uuid}")
    public void deleteUser(@PathVariable("uuid") UUID uuid) {
        userService.deleteUser(uuid);
    }

    @PutMapping(path = "{uuid}")
    public void updateUser(@PathVariable("uuid") UUID uuid, @RequestBody User user) {
        userService.updateUser(uuid, user);
    }
}
