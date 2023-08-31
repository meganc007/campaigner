package com.mcommings.campaigner.controllers.locations;

import com.mcommings.campaigner.models.locations.SettlementType;
import com.mcommings.campaigner.services.locations.SettlementTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public void saveSettlementType(@RequestBody SettlementType settlementType) {
        settlementTypeService.saveSettlementType(settlementType);
    }

    @DeleteMapping(path = "{settlementTypeId}")
    public void deleteSettlementType(@PathVariable("settlementTypeId") int settlementTypeId) {
        settlementTypeService.deleteSettlementType(settlementTypeId);
    }

    @PutMapping(path = "{settlementTypeId}")
    public void updateSettlementType(@PathVariable("settlementTypeId") int settlementTypeId,
                                     @RequestBody SettlementType settlementType) {
        settlementTypeService.updateSettlementType(settlementTypeId, settlementType);
    }
}
