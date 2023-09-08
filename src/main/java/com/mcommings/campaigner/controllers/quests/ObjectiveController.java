package com.mcommings.campaigner.controllers.quests;

import com.mcommings.campaigner.models.quests.Objective;
import com.mcommings.campaigner.services.quests.ObjectiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
