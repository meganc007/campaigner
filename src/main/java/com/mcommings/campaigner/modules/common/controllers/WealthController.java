package com.mcommings.campaigner.modules.common.controllers;

import com.mcommings.campaigner.modules.common.entities.Wealth;
import com.mcommings.campaigner.modules.common.services.WealthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/common/wealth")
public class WealthController {
    private final WealthService wealthService;

    @Autowired
    public WealthController(WealthService wealthService) {this.wealthService = wealthService;}

    @GetMapping
    public List<Wealth> getWealth() {
        return wealthService.getWealth();
    }

    @PostMapping
    public void saveWealth(@RequestBody Wealth wealth) {
        wealthService.saveWealth(wealth);
    }

    @DeleteMapping(path = "{wealthId}")
    public void deleteWealth(@PathVariable int wealthId) {
        wealthService.deleteWealth(wealthId);
    }

    @PutMapping(path = "{wealthId}")
    public void updateWealth(@PathVariable int wealthId, @RequestBody Wealth wealth) {
        wealthService.updateWealth(wealthId, wealth);
    }
}
