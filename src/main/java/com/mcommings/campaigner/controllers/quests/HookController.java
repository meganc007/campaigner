package com.mcommings.campaigner.controllers.quests;

import com.mcommings.campaigner.entities.quests.Hook;
import com.mcommings.campaigner.services.quests.HookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "api/quests/hooks")
public class HookController {

    private final HookService hookService;

    @Autowired
    public HookController(HookService hookService) {
        this.hookService = hookService;
    }

    @GetMapping
    public List<Hook> getHooks() {
        return hookService.getHooks();
    }

    @GetMapping(path = "/campaign/{uuid}")
    public List<Hook> getHooksByCampaignUUID(@PathVariable("uuid") UUID uuid) {
        return hookService.getHooksByCampaignUUID(uuid);
    }

    @PostMapping
    public void saveHook(@RequestBody Hook hook) {
        hookService.saveHook(hook);
    }

    @DeleteMapping(path = "{hookId}")
    public void deleteHook(@PathVariable("hookId") int hookId) {
        hookService.deleteHook(hookId);
    }

    @PutMapping(path = "{hookId}")
    public void updateHook(@PathVariable("hookId") int hookId, @RequestBody Hook hook) {
        hookService.updateHook(hookId, hook);
    }
}
