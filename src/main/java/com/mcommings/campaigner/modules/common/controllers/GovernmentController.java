package com.mcommings.campaigner.modules.common.controllers;

import com.mcommings.campaigner.modules.common.dtos.government.CreateGovernmentDTO;
import com.mcommings.campaigner.modules.common.dtos.government.UpdateGovernmentDTO;
import com.mcommings.campaigner.modules.common.dtos.government.ViewGovernmentDTO;
import com.mcommings.campaigner.modules.common.services.GovernmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/governments")
public class GovernmentController {

    private final GovernmentService governmentService;
    
    @GetMapping
    public List<ViewGovernmentDTO> getGovernments() {
        return governmentService.getAll();
    }

    @GetMapping(path = "/{governmentId}")
    public ViewGovernmentDTO getGovernment(@PathVariable int governmentId) {
        return governmentService.getById(governmentId);
    }
    
    @PostMapping
    public ViewGovernmentDTO createGovernment(@Valid @RequestBody CreateGovernmentDTO government) {
        return governmentService.create(government);
    }

    @PutMapping
    public void updateGovernment(@Valid @RequestBody UpdateGovernmentDTO government) {
        governmentService.update(government);
    }

    @DeleteMapping(path = "/{governmentId}")
    public void deleteGovernment(@PathVariable int governmentId) {
        governmentService.delete(governmentId);
    }

}
