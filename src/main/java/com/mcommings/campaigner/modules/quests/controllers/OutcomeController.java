package com.mcommings.campaigner.modules.quests.controllers;

import com.mcommings.campaigner.modules.quests.entities.Outcome;
import com.mcommings.campaigner.modules.quests.services.OutcomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "api/quests/outcomes")
public class OutcomeController {

    private final OutcomeService outcomeService;

    @Autowired
    public OutcomeController(OutcomeService outcomeService) {
        this.outcomeService = outcomeService;
    }

    @GetMapping
    public List<Outcome> getOutcomes() {
        return outcomeService.getOutcomes();
    }

    @GetMapping(path = "/campaign/{uuid}")
    public List<Outcome> getOutcomesByCampaignUUID(UUID uuid) {
        return outcomeService.getOutcomesByCampaignUUID(uuid);
    }

    @PostMapping
    public void saveOutcome(@RequestBody Outcome outcome) {
        outcomeService.saveOutcome(outcome);
    }

    @DeleteMapping(path = "{outcomeId}")
    public void deleteOutcome(@PathVariable("outcomeId") int outcomeId) {
        outcomeService.deleteOutcome(outcomeId);
    }

    @PutMapping(path = "{outcomeId}")
    public void updateOutcome(@PathVariable("outcomeId") int outcomeId, @RequestBody Outcome outcome) {
        outcomeService.updateOutcome(outcomeId, outcome);
    }
}
