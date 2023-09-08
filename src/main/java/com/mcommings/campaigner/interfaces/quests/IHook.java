package com.mcommings.campaigner.interfaces.quests;

import com.mcommings.campaigner.models.quests.Hook;

import java.util.List;

public interface IHook {

    List<Hook> getHooks();

    void saveHook(Hook hook);

    void deleteHook(int hookId);

    void updateHook(int hookId, Hook hook);
}
