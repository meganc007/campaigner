package com.mcommings.campaigner.interfaces.quests;

import com.mcommings.campaigner.models.quests.Outcome;

import java.util.List;
import java.util.UUID;

public interface IOutcome {

    List<Outcome> getOutcomes();

    List<Outcome> getOutcomesByCampaignUUID(UUID uuid);

    void saveOutcome(Outcome outcome);

    void deleteOutcome(int outcomeId);

    void updateOutcome(int outcomeId, Outcome outcome);
}
