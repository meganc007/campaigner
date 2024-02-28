package com.mcommings.campaigner.controllers.admin;

import com.mcommings.campaigner.models.admin.Role;
import com.mcommings.campaigner.services.admin.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/admin/roles")
public class RoleController {

    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public List<Role> getRoles() {
        return roleService.getRoles();
    }

    @PostMapping
    public void saveRole(@RequestBody Role role) {
        roleService.saveRole(role);
    }

    @DeleteMapping(path = {"roleId"})
    public void deleteRole(@PathVariable("roleId") int roleId) {
        roleService.deleteRole(roleId);
    }

    @PutMapping(path = {"roleId"})
    public void updateRole(@PathVariable("roleId") int roleId, @RequestBody Role role) {
        roleService.updateRole(roleId, role);
    }
}
