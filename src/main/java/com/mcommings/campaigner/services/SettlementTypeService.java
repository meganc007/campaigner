package com.mcommings.campaigner.services;

import com.mcommings.campaigner.interfaces.ISettlementType;
import com.mcommings.campaigner.models.RepositoryHelper;
import com.mcommings.campaigner.models.SettlementType;
import com.mcommings.campaigner.models.repositories.ISettlementTypeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.mcommings.campaigner.enums.ErrorMessage.*;

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
    public void saveSettlementType(SettlementType settlementType) throws IllegalArgumentException, DataIntegrityViolationException {
        if(RepositoryHelper.nameIsNullOrEmpty(settlementType)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if(RepositoryHelper.nameAlreadyExists(settlementTypeRepository, settlementType)) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }

        settlementTypeRepository.saveAndFlush(settlementType);
    }

    @Override
    @Transactional
    public void deleteSettlementType(int settlementTypeId) throws IllegalArgumentException, DataIntegrityViolationException {
        if(RepositoryHelper.cannotFindId(settlementTypeRepository, settlementTypeId)) {
            throw new IllegalArgumentException(DELETE_NOT_FOUND.message);
        }
        //TODO: check if foreign key

        settlementTypeRepository.deleteById(settlementTypeId);
    }

    @Override
    @Transactional
    public void updateSettlementType(int settlementTypeId, SettlementType settlementType) {
        if(RepositoryHelper.cannotFindId(settlementTypeRepository, settlementTypeId)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        SettlementType settlementTypeToUpdate = RepositoryHelper.getById(settlementTypeRepository, settlementTypeId);
        settlementTypeToUpdate.setName(settlementType.getName());
        settlementTypeToUpdate.setDescription(settlementType.getDescription());
    }
}
