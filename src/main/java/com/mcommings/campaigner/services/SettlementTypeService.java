package com.mcommings.campaigner.services;

import com.mcommings.campaigner.interfaces.ISettlementType;
import com.mcommings.campaigner.models.SettlementType;
import com.mcommings.campaigner.models.repositories.ISettlementTypeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SettlementTypeService implements ISettlementType {

    private final ISettlementTypeRepository settlementTypeRepository;

    @Autowired
   public SettlementTypeService(ISettlementTypeRepository settlementTypeRepository) {this.settlementTypeRepository = settlementTypeRepository;}

    @Override
    public List<SettlementType> getSettlementTypes() {
        return settlementTypeRepository.findAll();
    }

    @Override
    @Transactional
    public void saveSettlementType(SettlementType settlementType) {

    }

    @Override
    @Transactional
    public void deleteSettlementType(int settlementTypeId) {

    }

    @Override
    @Transactional
    public void updateSettlementType(int settlementTypeId, SettlementType settlementType) {

    }
}
