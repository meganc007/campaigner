package com.mcommings.campaigner.interfaces;

import com.mcommings.campaigner.models.SettlementType;

import java.util.List;

public interface ISettlementType {
    List<SettlementType> getSettlementTypes();

    void saveSettlementType(SettlementType settlementType);

    void deleteSettlementType(int settlementTypeId);

    void updateSettlementType(int settlementTypeId, SettlementType settlementType);
}
