package com.mcommings.campaigner.modules.quests.services.interfaces;

import com.mcommings.campaigner.modules.quests.dtos.HookDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IHook {

    List<HookDTO> getHooks();

    Optional<HookDTO> getHook(int hookId);

    List<HookDTO> getHooksByCampaignUUID(UUID uuid);

    List<HookDTO> getHooksWhereDescriptionContainsKeyword(String keyword);

    void saveHook(HookDTO hook);

    void deleteHook(int hookId);

    Optional<HookDTO> updateHook(int hookId, HookDTO hook);
}
