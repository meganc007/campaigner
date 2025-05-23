package com.mcommings.campaigner.modules.quests.controllers;

import com.mcommings.campaigner.modules.quests.dtos.OutcomeDTO;
import com.mcommings.campaigner.modules.quests.services.interfaces.IOutcome;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.mcommings.campaigner.enums.ErrorMessage.ID_NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/outcomes")
public class OutcomeController {

    private final IOutcome outcomeService;

    @GetMapping
    public List<OutcomeDTO> getOutcomes() {
        return outcomeService.getOutcomes();
    }

    @GetMapping(path = "/{outcomeId}")
    public OutcomeDTO getOutcome(@PathVariable("outcomeId") int outcomeId) {
        return outcomeService.getOutcome(outcomeId).orElseThrow(() -> new IllegalArgumentException(ID_NOT_FOUND.message));
    }

    @GetMapping(path = "/campaign/{uuid}")
    public List<OutcomeDTO> getOutcomesByCampaignUUID(@PathVariable("uuid") UUID uuid) {
        return outcomeService.getOutcomesByCampaignUUID(uuid);
    }

    @GetMapping(path = "/description/{keyword}")
    public List<OutcomeDTO> getOutcomesWhereDescriptionContainsKeyword(@PathVariable("keyword") String keyword) {
        return outcomeService.getOutcomesWhereDescriptionContainsKeyword(keyword);
    }

    @PostMapping
    public void saveOutcome(@Valid @RequestBody OutcomeDTO outcome) {
        outcomeService.saveOutcome(outcome);
    }

    @DeleteMapping(path = "{outcomeId}")
    public void deleteOutcome(@PathVariable("outcomeId") int outcomeId) {
        outcomeService.deleteOutcome(outcomeId);
    }

    @PutMapping(path = "{outcomeId}")
    public void updateOutcome(@PathVariable("outcomeId") int outcomeId, @RequestBody OutcomeDTO outcome) {
        outcomeService.updateOutcome(outcomeId, outcome);
    }
}
