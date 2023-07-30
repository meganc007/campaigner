package com.mcommings.campaigner.services;

import com.mcommings.campaigner.interfaces.ISettlementType;
import com.mcommings.campaigner.models.repositories.ISettlementTypeRepository;
import com.mcommings.campaigner.models.SettlementType;
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
}
