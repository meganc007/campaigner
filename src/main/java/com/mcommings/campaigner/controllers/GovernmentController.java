package com.mcommings.campaigner.controllers;

import com.mcommings.campaigner.entities.Government;
import com.mcommings.campaigner.services.GovernmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/governments")
public class GovernmentController {

    private final GovernmentService governmentService;

    @Autowired
    public GovernmentController (GovernmentService governmentService) {this.governmentService = governmentService;}

    @GetMapping
    public List<Government> getGovernments() {
        return governmentService.getGovernments();
    }

    @PostMapping
    public void saveGovernment(@RequestBody Government government) {
        governmentService.saveGovernment(government);
    }

    @DeleteMapping(path = "{governmentId}")
    public void deleteGovernment(@PathVariable("governmentId") int governmentId) {
        governmentService.deleteGovernment(governmentId);
    }

    @PutMapping(path = "{governmentId}")
    public void updateGovernment(@PathVariable("governmentId") int governmentId, @RequestBody Government government) {
        governmentService.updateGovernment(governmentId, government);
    }
}
