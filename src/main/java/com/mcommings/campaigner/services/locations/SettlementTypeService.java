package com.mcommings.campaigner.services.locations;

import com.mcommings.campaigner.interfaces.locations.ISettlementType;
import com.mcommings.campaigner.models.RepositoryHelper;
import com.mcommings.campaigner.models.locations.SettlementType;
import com.mcommings.campaigner.repositories.locations.ICityRepository;
import com.mcommings.campaigner.repositories.locations.ISettlementTypeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mcommings.campaigner.enums.ErrorMessage.*;
import static com.mcommings.campaigner.enums.ForeignKey.FK_SETTLEMENT;

@Service
public class SettlementTypeService implements ISettlementType {

    private final ISettlementTypeRepository settlementTypeRepository;
    private final ICityRepository cityRepository;

    @Autowired
    public SettlementTypeService(ISettlementTypeRepository settlementTypeRepository, ICityRepository cityRepository) {
        this.settlementTypeRepository = settlementTypeRepository;
        this.cityRepository = cityRepository;
    }

    @Override
    public List<SettlementType> getSettlementTypes() {
        return settlementTypeRepository.findAll();
    }

    @Override
    @Transactional
    public void saveSettlementType(SettlementType settlementType) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.nameIsNullOrEmpty(settlementType)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (RepositoryHelper.nameAlreadyExists(settlementTypeRepository, settlementType)) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }

        settlementTypeRepository.saveAndFlush(settlementType);
    }

    @Override
    @Transactional
    public void deleteSettlementType(int settlementTypeId) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(settlementTypeRepository, settlementTypeId)) {
            throw new IllegalArgumentException(DELETE_NOT_FOUND.message);
        }
        if (RepositoryHelper.isForeignKey(getReposWhereSettlementTypeIsAForeignKey(), FK_SETTLEMENT.columnName, settlementTypeId)) {
            throw new DataIntegrityViolationException(DELETE_FOREIGN_KEY.message);
        }

        settlementTypeRepository.deleteById(settlementTypeId);
    }

    @Override
    @Transactional
    public void updateSettlementType(int settlementTypeId, SettlementType settlementType) {
        if (RepositoryHelper.cannotFindId(settlementTypeRepository, settlementTypeId)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        SettlementType settlementTypeToUpdate = RepositoryHelper.getById(settlementTypeRepository, settlementTypeId);
        settlementTypeToUpdate.setName(settlementType.getName());
        settlementTypeToUpdate.setDescription(settlementType.getDescription());
    }

    private List<CrudRepository> getReposWhereSettlementTypeIsAForeignKey() {
        return new ArrayList<>(Arrays.asList(cityRepository));
    }
}
