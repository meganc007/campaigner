package com.mcommings.campaigner.modules.locations.controllers;

import com.mcommings.campaigner.modules.locations.dtos.settlement_types.CreateSettlementTypeDTO;
import com.mcommings.campaigner.modules.locations.dtos.settlement_types.UpdateSettlementTypeDTO;
import com.mcommings.campaigner.modules.locations.dtos.settlement_types.ViewSettlementTypeDTO;
import com.mcommings.campaigner.modules.locations.services.SettlementTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/settlementtypes")
public class SettlementTypeController {

    private final SettlementTypeService settlementTypeService;

    @GetMapping
    public List<ViewSettlementTypeDTO> getSettlementTypes() {

        return settlementTypeService.getAll();
    }

    @GetMapping(path = "/{settlementTypeId}")
    public ViewSettlementTypeDTO getSettlementType(@PathVariable int settlementTypeId) {
        return settlementTypeService.getById(settlementTypeId);
    }

    @PostMapping
    public ViewSettlementTypeDTO saveSettlementType(@Valid @RequestBody CreateSettlementTypeDTO settlementType) {
        return settlementTypeService.create(settlementType);
    }

    @PutMapping
    public ViewSettlementTypeDTO updateSettlementType(@Valid @RequestBody UpdateSettlementTypeDTO settlementType) {
        return settlementTypeService.update(settlementType);
    }

    @DeleteMapping(path = "/{settlementTypeId}")
    public void deleteSettlementType(@PathVariable int settlementTypeId) {
        settlementTypeService.delete(settlementTypeId);
    }
}