package com.mcommings.campaigner.controllers;

import com.mcommings.campaigner.models.Wealth;
import com.mcommings.campaigner.services.WealthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
