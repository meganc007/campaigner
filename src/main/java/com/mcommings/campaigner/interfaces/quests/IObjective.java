package com.mcommings.campaigner.interfaces.quests;

import com.mcommings.campaigner.entities.quests.Objective;

import java.util.List;
import java.util.UUID;

public interface IObjective {

    List<Objective> getObjectives();

    List<Objective> getObjectivesByCampaignUUID(UUID uuid);

    void saveObjective(Objective objective);

    void deleteObjective(int objectiveId);

    void updateObjective(int objectiveId, Objective objective);
}
