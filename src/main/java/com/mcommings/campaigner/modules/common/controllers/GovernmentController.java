package com.mcommings.campaigner.modules.common.controllers;

import com.mcommings.campaigner.modules.common.dtos.GovernmentDTO;
import com.mcommings.campaigner.modules.common.services.interfaces.IGovernment;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.mcommings.campaigner.enums.ErrorMessage.ID_NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/governments")
public class GovernmentController {

    private final IGovernment governmentService;
    
    @GetMapping
    public List<GovernmentDTO> getGovernments() {
        return governmentService.getGovernments();
    }

    @GetMapping(path = "/{governmentId}")
    public GovernmentDTO getGovernment(@PathVariable("governmentId") int governmentId) {
        return governmentService.getGovernment(governmentId).orElseThrow(() -> new IllegalArgumentException(ID_NOT_FOUND.message));
    }
    
    @PostMapping
    public void saveGovernment(@Valid @RequestBody GovernmentDTO government) {
        governmentService.saveGovernment(government);
    }

    @DeleteMapping(path = "{governmentId}")
    public void deleteGovernment(@PathVariable("governmentId") int governmentId) {
        governmentService.deleteGovernment(governmentId);
    }

    @PutMapping(path = "{governmentId}")
    public void updateGovernment(@PathVariable("governmentId") int governmentId, @RequestBody GovernmentDTO government) {
        governmentService.updateGovernment(governmentId, government);
    }
}
