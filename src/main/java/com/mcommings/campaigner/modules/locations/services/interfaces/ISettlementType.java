package com.mcommings.campaigner.modules.locations.services.interfaces;

import com.mcommings.campaigner.modules.locations.dtos.SettlementTypeDTO;

import java.util.List;
import java.util.Optional;

public interface ISettlementType {
    List<SettlementTypeDTO> getSettlementTypes();

    Optional<SettlementTypeDTO> getSettlementType(int settlementTypeId);

    void saveSettlementType(SettlementTypeDTO settlementType);

    void deleteSettlementType(int settlementTypeId);

    Optional<SettlementTypeDTO> updateSettlementType(int settlementTypeId, SettlementTypeDTO settlementType);
}
