package com.mcommings.campaigner.interfaces.quests;

import com.mcommings.campaigner.models.quests.Objective;

import java.util.List;

public interface IObjective {

    List<Objective> getObjectives();

    void saveObjective(Objective objective);

    void deleteObjective(int objectiveId);

    void updateObjective(int objectiveId, Objective objective);
}
