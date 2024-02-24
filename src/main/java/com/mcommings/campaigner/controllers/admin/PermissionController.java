package com.mcommings.campaigner.controllers.admin;

import com.mcommings.campaigner.models.admin.Permission;
import com.mcommings.campaigner.services.CampaignService;
import com.mcommings.campaigner.services.admin.PermissionService;
import com.mcommings.campaigner.services.admin.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/admin/permissions")
public class PermissionController {

    private final PermissionService permissionService;
    private final CampaignService campaignService;
    private final UserService userService;

    @Autowired
    public PermissionController(PermissionService permissionService, CampaignService campaignService,
                                UserService userService) {
        this.permissionService = permissionService;
        this.campaignService = campaignService;
        this.userService = userService;
    }

    @GetMapping
    public List<Permission> getPermissions() {
        return permissionService.getPermissions();
    }

    @PostMapping
    public void savePermission(@RequestBody Permission permission) {
        permissionService.savePermission(permission);
    }

    @DeleteMapping(path = "{id}")
    public void deletePermission(@PathVariable("id") int id) {
        permissionService.deletePermission(id);
    }

    @PutMapping(path = "{id}")
    public void updatePermission(@PathVariable("id") int id, @RequestBody Permission permission) {
        permissionService.updatePermission(id, permission);
    }
}
