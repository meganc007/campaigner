package com.mcommings.campaigner.controllers;

import com.mcommings.campaigner.models.Wealth;
import com.mcommings.campaigner.services.WealthService;
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
    List<Wealth> getWealth() {
        return wealthService.getWealth();
    }

    @PostMapping
    void saveWealth(@RequestBody Wealth wealth) {
        wealthService.saveWealth(wealth);
    }

    @DeleteMapping(path = "{wealthId}")
    void deleteWealth(@PathVariable int wealthId) {
        wealthService.deleteWealth(wealthId);
    }

    @PutMapping(path = "{wealthId}")
    void updateWealth(@PathVariable int wealthId, @RequestBody Wealth wealth) {
        wealthService.updateWealth(wealthId, wealth);
    }
}
