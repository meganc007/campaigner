package com.mcommings.campaigner.services.items;

import com.mcommings.campaigner.interfaces.items.IDiceType;
import com.mcommings.campaigner.models.RepositoryHelper;
import com.mcommings.campaigner.models.items.DiceType;
import com.mcommings.campaigner.repositories.items.IDiceTypeRepository;
import com.mcommings.campaigner.repositories.items.IWeaponRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mcommings.campaigner.enums.ErrorMessage.*;
import static com.mcommings.campaigner.enums.ForeignKey.FK_DICE_TYPE;

@Service
public class DiceTypeService implements IDiceType {

    private final IDiceTypeRepository diceTypeRepository;
    private final IWeaponRepository weaponRepository;

    @Autowired
    public DiceTypeService(IDiceTypeRepository diceTypeRepository, IWeaponRepository weaponRepository) {
        this.diceTypeRepository = diceTypeRepository;
        this.weaponRepository = weaponRepository;
    }

    @Override
    public List<DiceType> getDiceTypes() {
        return diceTypeRepository.findAll();
    }

    @Override
    @Transactional
    public void saveDiceType(DiceType diceType) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.nameIsNullOrEmpty(diceType)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (RepositoryHelper.nameAlreadyExists(diceTypeRepository, diceType)) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }

        diceTypeRepository.saveAndFlush(diceType);
    }

    @Override
    @Transactional
    public void deleteDiceType(int diceTypeId) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(diceTypeRepository, diceTypeId)) {
            throw new IllegalArgumentException(DELETE_NOT_FOUND.message);
        }
        if (RepositoryHelper.isForeignKey(getReposWhereDiceTypeIsAForeignKey(), FK_DICE_TYPE.columnName, diceTypeId)) {
            throw new DataIntegrityViolationException(DELETE_FOREIGN_KEY.message);
        }

        diceTypeRepository.deleteById(diceTypeId);
    }

    @Override
    @Transactional
    public void updateDiceType(int diceTypeId, DiceType diceType) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(diceTypeRepository, diceTypeId)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        DiceType diceTypeToUpdate = RepositoryHelper.getById(diceTypeRepository, diceTypeId);
        if (diceType.getName() != null) diceTypeToUpdate.setName(diceType.getName());
        if (diceType.getDescription() != null) diceTypeToUpdate.setDescription(diceType.getDescription());
        if (diceType.getMax_roll() >= 0) diceTypeToUpdate.setMax_roll(diceType.getMax_roll());
    }

    private List<CrudRepository> getReposWhereDiceTypeIsAForeignKey() {
        return new ArrayList<>(Arrays.asList(weaponRepository));
    }
}
