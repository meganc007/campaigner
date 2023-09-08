package com.mcommings.campaigner.controllers.quests;

import com.mcommings.campaigner.models.quests.Outcome;
import com.mcommings.campaigner.services.quests.OutcomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
