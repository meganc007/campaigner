package com.mcommings.campaigner.modules.admin.controllers;

import com.mcommings.campaigner.modules.admin.entities.PermissionType;
import com.mcommings.campaigner.modules.admin.services.PermissionTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/admin/permissiontypes")
public class PermissionTypeController {

    private final PermissionTypeService permissionTypeService;

    @Autowired
    public PermissionTypeController(PermissionTypeService permissionTypeService) {
        this.permissionTypeService = permissionTypeService;
    }

    @GetMapping
    public List<PermissionType> getPermissionTypes() {
        return permissionTypeService.getPermissionTypes();
    }

    @PostMapping
    public void savePermissionType(@RequestBody PermissionType permissionType) {
        permissionTypeService.savePermissionType(permissionType);
    }

    @DeleteMapping(path = "{permissionTypeId}")
    public void deletePermissionType(@PathVariable("permissionTypeId") int permissionTypeId) {
        permissionTypeService.deletePermissionType(permissionTypeId);
    }

    @PutMapping(path = "{permissionTypeId}")
    public void updatePermissionType(@PathVariable("permissionTypeId") int permissionTypeId, @RequestBody PermissionType permissionType) {
        permissionTypeService.updatePermissionType(permissionTypeId, permissionType);
    }
}
