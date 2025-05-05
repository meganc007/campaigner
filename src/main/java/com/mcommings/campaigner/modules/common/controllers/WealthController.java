package com.mcommings.campaigner.modules.common.controllers;

import com.mcommings.campaigner.modules.common.dtos.WealthDTO;
import com.mcommings.campaigner.modules.common.services.interfaces.IWealth;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/wealth")
public class WealthController {
    private final IWealth wealthService;

    @GetMapping
    public List<WealthDTO> getWealth() {

        return wealthService.getWealth();
    }

    @PostMapping
    public void saveWealth(@Valid @RequestBody WealthDTO wealth) {
        wealthService.saveWealth(wealth);
    }

    @DeleteMapping(path = "{wealthId}")
    public void deleteWealth(@PathVariable int wealthId) {
        wealthService.deleteWealth(wealthId);
    }

    @PutMapping(path = "{wealthId}")
    public void updateWealth(@PathVariable int wealthId, @RequestBody WealthDTO wealth) {
        wealthService.updateWealth(wealthId, wealth);
    }
}
