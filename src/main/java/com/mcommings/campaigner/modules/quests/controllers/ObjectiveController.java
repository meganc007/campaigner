package com.mcommings.campaigner.modules.quests.controllers;

import com.mcommings.campaigner.modules.quests.dtos.ObjectiveDTO;
import com.mcommings.campaigner.modules.quests.services.interfaces.IObjective;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.mcommings.campaigner.enums.ErrorMessage.ID_NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/objectives")
public class ObjectiveController {

    private final IObjective objectiveService;

    @GetMapping
    public List<ObjectiveDTO> getObjectives() {
        return objectiveService.getObjectives();
    }

    @GetMapping(path = "/{objectiveId}")
    public ObjectiveDTO getObjective(@PathVariable("objectiveId") int objectiveId) {
        return objectiveService.getObjective(objectiveId).orElseThrow(() -> new IllegalArgumentException(ID_NOT_FOUND.message));
    }

    @GetMapping(path = "/campaign/{uuid}")
    public List<ObjectiveDTO> getObjectivesByCampaignUUID(@PathVariable("uuid") UUID uuid) {
        return objectiveService.getObjectivesByCampaignUUID(uuid);
    }

    @GetMapping(path = "/description/{keyword}")
    public List<ObjectiveDTO> getObjectivesWhereDescriptionContainsKeyword(@PathVariable("keyword") String keyword) {
        return objectiveService.getObjectivesWhereDescriptionContainsKeyword(keyword);
    }

    @PostMapping
    public void saveObjective(@Valid @RequestBody ObjectiveDTO objective) {
        objectiveService.saveObjective(objective);
    }

    @DeleteMapping(path = "{objectiveId}")
    public void deleteObjective(@PathVariable("objectiveId") int objectiveId) {
        objectiveService.deleteObjective(objectiveId);
    }

    @PutMapping(path = "{objectiveId}")
    public void updateObjective(@PathVariable("objectiveId") int objectiveId, @RequestBody ObjectiveDTO objective) {
        objectiveService.updateObjective(objectiveId, objective);
    }
}
