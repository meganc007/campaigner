package com.mcommings.campaigner.interfaces.locations;

import com.mcommings.campaigner.entities.locations.SettlementType;

import java.util.List;

public interface ISettlementType {
    List<SettlementType> getSettlementTypes();

    void saveSettlementType(SettlementType settlementType);

    void deleteSettlementType(int settlementTypeId);

    void updateSettlementType(int settlementTypeId, SettlementType settlementType);
}
