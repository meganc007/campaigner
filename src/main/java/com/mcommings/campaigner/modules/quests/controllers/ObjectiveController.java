package com.mcommings.campaigner.modules.quests.controllers;

import com.mcommings.campaigner.modules.quests.entities.Objective;
import com.mcommings.campaigner.modules.quests.services.ObjectiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "api/quests/objectives")
public class ObjectiveController {

    private final ObjectiveService objectiveService;

    @Autowired
    public ObjectiveController(ObjectiveService objectiveService) {
        this.objectiveService = objectiveService;
    }

    @GetMapping
    public List<Objective> getObjectives() {
        return objectiveService.getObjectives();
    }

    @GetMapping(path = "/campaign/{uuid}")
    public List<Objective> getObjectivesByCampaignUUID(@PathVariable("uuid") UUID uuid) {
        return objectiveService.getObjectivesByCampaignUUID(uuid);
    }

    @PostMapping
    public void saveObjective(@RequestBody Objective objective) {
        objectiveService.saveObjective(objective);
    }

    @DeleteMapping(path = "{objectiveId}")
    public void deleteObjective(@PathVariable("objectiveId") int objectiveId) {
        objectiveService.deleteObjective(objectiveId);
    }

    @PutMapping(path = "{objectiveId}")
    public void updateObjective(@PathVariable("objectiveId") int objectiveId, @RequestBody Objective objective) {
        objectiveService.updateObjective(objectiveId, objective);
    }
}
