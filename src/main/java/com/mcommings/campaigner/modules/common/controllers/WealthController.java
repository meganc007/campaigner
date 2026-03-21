package com.mcommings.campaigner.modules.common.controllers;

import com.mcommings.campaigner.modules.common.dtos.wealth.CreateWealthDTO;
import com.mcommings.campaigner.modules.common.dtos.wealth.UpdateWealthDTO;
import com.mcommings.campaigner.modules.common.dtos.wealth.ViewWealthDTO;
import com.mcommings.campaigner.modules.common.services.WealthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/wealth")
public class WealthController {
    private final WealthService wealthService;

    @GetMapping
    public List<ViewWealthDTO> getWealths() {
        return wealthService.getAll();
    }

    @GetMapping(path = "/{wealthId}")
    public ViewWealthDTO getWealth(@PathVariable int wealthId) {
        return wealthService.getById(wealthId);
    }

    @PostMapping
    public ViewWealthDTO createWealth(@Valid @RequestBody CreateWealthDTO wealth) {
        return wealthService.create(wealth);
    }

    @PutMapping
    public void updateWealth(@Valid @RequestBody UpdateWealthDTO wealth) {
        wealthService.update(wealth);
    }

    @DeleteMapping(path = "/{wealthId}")
    public void deleteWealth(@PathVariable int wealthId) {
        wealthService.delete(wealthId);
    }
}
