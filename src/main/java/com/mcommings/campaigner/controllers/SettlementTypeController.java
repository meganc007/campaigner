package com.mcommings.campaigner.controllers;

import com.mcommings.campaigner.models.SettlementType;
import com.mcommings.campaigner.services.SettlementTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "api/locations/settlement-types")
public class SettlementTypeController {

    private final SettlementTypeService settlementTypeService;

    @Autowired
    public SettlementTypeController (SettlementTypeService settlementTypeService) {this.settlementTypeService = settlementTypeService;}

    @GetMapping
    public List<SettlementType> getSettlementTypes() {
        return settlementTypeService.getSettlementTypes();
    }
}
