package com.mcommings.campaigner.modules.quests.services.interfaces;

import com.mcommings.campaigner.modules.quests.entities.Hook;

import java.util.List;
import java.util.UUID;

public interface IHook {

    List<Hook> getHooks();

    List<Hook> getHooksByCampaignUUID(UUID uuid);

    void saveHook(Hook hook);

    void deleteHook(int hookId);

    void updateHook(int hookId, Hook hook);
}
