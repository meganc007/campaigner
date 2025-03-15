package com.mcommings.campaigner.quests.services.interfaces;

import com.mcommings.campaigner.quests.entities.Outcome;

import java.util.List;
import java.util.UUID;

public interface IOutcome {

    List<Outcome> getOutcomes();

    List<Outcome> getOutcomesByCampaignUUID(UUID uuid);

    void saveOutcome(Outcome outcome);

    void deleteOutcome(int outcomeId);

    void updateOutcome(int outcomeId, Outcome outcome);
}
