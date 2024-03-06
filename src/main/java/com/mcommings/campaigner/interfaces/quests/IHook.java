package com.mcommings.campaigner.interfaces.quests;

import com.mcommings.campaigner.models.quests.Hook;

import java.util.List;
import java.util.UUID;

public interface IHook {

    List<Hook> getHooks();

    List<Hook> getHooksByCampaignUUID(UUID uuid);

    void saveHook(Hook hook);

    void deleteHook(int hookId);

    void updateHook(int hookId, Hook hook);
}
