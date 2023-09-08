package com.mcommings.campaigner.interfaces.quests;

import com.mcommings.campaigner.models.quests.Outcome;

import java.util.List;

public interface IOutcome {

    List<Outcome> getOutcomes();

    void saveOutcome(Outcome outcome);

    void deleteOutcome(int outcomeId);

    void updateOutcome(int outcomeId, Outcome outcome);
}
