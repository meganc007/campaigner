package com.mcommings.campaigner.modules.quests.services.interfaces;

import com.mcommings.campaigner.modules.quests.dtos.OutcomeDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IOutcome {

    List<OutcomeDTO> getOutcomes();

    Optional<OutcomeDTO> getOutcome(int outcomeId);

    List<OutcomeDTO> getOutcomesByCampaignUUID(UUID uuid);

    List<OutcomeDTO> getOutcomesWhereDescriptionContainsKeyword(String keyword);

    void saveOutcome(OutcomeDTO outcome);

    void deleteOutcome(int outcomeId);

    Optional<OutcomeDTO> updateOutcome(int outcomeId, OutcomeDTO outcome);
}
