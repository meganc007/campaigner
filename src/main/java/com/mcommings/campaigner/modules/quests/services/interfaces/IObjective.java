package com.mcommings.campaigner.modules.quests.services.interfaces;

import com.mcommings.campaigner.modules.quests.dtos.ObjectiveDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IObjective {

    List<ObjectiveDTO> getObjectives();

    Optional<ObjectiveDTO> getObjective(int objectiveId);

    List<ObjectiveDTO> getObjectivesByCampaignUUID(UUID uuid);

    List<ObjectiveDTO> getObjectivesWhereDescriptionContainsKeyword(String keyword);

    void saveObjective(ObjectiveDTO objective);

    void deleteObjective(int objectiveId);

    Optional<ObjectiveDTO> updateObjective(int objectiveId, ObjectiveDTO objective);
}
