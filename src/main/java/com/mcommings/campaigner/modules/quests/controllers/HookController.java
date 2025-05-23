package com.mcommings.campaigner.modules.quests.controllers;

import com.mcommings.campaigner.modules.quests.dtos.HookDTO;
import com.mcommings.campaigner.modules.quests.services.interfaces.IHook;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.mcommings.campaigner.enums.ErrorMessage.ID_NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/hooks")
public class HookController {

    private final IHook hookService;

    @GetMapping
    public List<HookDTO> getHooks() {
        return hookService.getHooks();
    }

    @GetMapping(path = "/{hookId}")
    public HookDTO getHook(@PathVariable("hookId") int hookId) {
        return hookService.getHook(hookId).orElseThrow(() -> new IllegalArgumentException(ID_NOT_FOUND.message));
    }

    @GetMapping(path = "/campaign/{uuid}")
    public List<HookDTO> getHooksByCampaignUUID(@PathVariable("uuid") UUID uuid) {
        return hookService.getHooksByCampaignUUID(uuid);
    }

    @GetMapping(path = "/description/{keyword}")
    public List<HookDTO> getHooksWhereDescriptionContainsKeyword(@PathVariable("keyword") String keyword) {
        return hookService.getHooksWhereDescriptionContainsKeyword(keyword);
    }

    @PostMapping
    public void saveHook(@Valid @RequestBody HookDTO hook) {
        hookService.saveHook(hook);
    }

    @DeleteMapping(path = "{hookId}")
    public void deleteHook(@PathVariable("hookId") int hookId) {
        hookService.deleteHook(hookId);
    }

    @PutMapping(path = "{hookId}")
    public void updateHook(@PathVariable("hookId") int hookId, @RequestBody HookDTO hook) {
        hookService.updateHook(hookId, hook);
    }
}
