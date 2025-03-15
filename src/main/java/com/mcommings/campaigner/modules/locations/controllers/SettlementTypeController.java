package com.mcommings.campaigner.modules.locations.controllers;

import com.mcommings.campaigner.modules.locations.dtos.SettlementTypeDTO;
import com.mcommings.campaigner.modules.locations.services.interfaces.ISettlementType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.mcommings.campaigner.enums.ErrorMessage.ID_NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/settlementtypes")
public class SettlementTypeController {

    private final ISettlementType settlementTypeService;

    @GetMapping
    public List<SettlementTypeDTO> getSettlementTypes() {
        return settlementTypeService.getSettlementTypes();
    }

    @GetMapping(path = "/{settlementTypeId}")
    public SettlementTypeDTO getSettlementType(@PathVariable("settlementTypeId") int settlementTypeId) {
        return settlementTypeService.getSettlementType(settlementTypeId).orElseThrow(() -> new IllegalArgumentException(ID_NOT_FOUND.message));
    }

    @PostMapping
    public void saveSettlementType(@Valid @RequestBody SettlementTypeDTO settlementType) {
        settlementTypeService.saveSettlementType(settlementType);
    }

    @DeleteMapping(path = "{settlementTypeId}")
    public void deleteSettlementType(@PathVariable("settlementTypeId") int settlementTypeId) {
        settlementTypeService.deleteSettlementType(settlementTypeId);
    }

    @PutMapping(path = "{settlementTypeId}")
    public void updateSettlementType(@PathVariable("settlementTypeId") int settlementTypeId, @RequestBody SettlementTypeDTO settlementType) {
        settlementTypeService.updateSettlementType(settlementTypeId, settlementType);
    }
}